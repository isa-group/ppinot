package es.us.isa.ppinot.evaluation.evaluators;

import es.us.isa.ppinot.evaluation.logs.*;
import es.us.isa.ppinot.handler.json.DateTimeDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.std.ToStringSerializer;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapBigLogProvider implements BigLogProvider {

    private final static Logger log = Logger.getLogger(MapLogProvider.class.getName());

    LogProvider logProvider;
    final DB db;
    final Map<String, byte[]> map;
    final ObjectMapper mapper;
    final ObjectReader reader;
    final EndMatcher endMatcher;


    public MapBigLogProvider(LogProvider logProvider, EndMatcher endMatcher) {
        this.logProvider = logProvider;
        this.endMatcher = endMatcher;
        this.db = DBMaker.tempFileDB().fileMmapEnable().concurrencyDisable().allocateIncrement(256 * 1024 * 1024).fileDeleteAfterClose().closeOnJvmShutdown().make();
        this.map = this.db.hashMap("instances")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.BYTE_ARRAY)
                .createOrOpen();
        this.mapper = new ObjectMapper();
        this.reader = this.mapper.reader(this.mapper.getTypeFactory().constructCollectionType(List.class, LogEntry.class));

    }

    @Override
    public LogProvider create(final Interval i, IntervalCondition condition) {
        if (IntervalCondition.START.equals(condition)) {
            return new MapLogProvider(i, startInInterval(i));
        } else if (IntervalCondition.ACTIVE.equals(condition)) {
            return new MapLogProvider(i, activeInInterval(i));
        } else {
            return new MapLogProvider(i, closedInInterval(i));
        }
    }

    LogInstance.Condition startInInterval(final Interval i) {
        return new LogInstance.Condition() {
            @Override
            public boolean test(LogInstance instance) {
                DateTime start = instance.getStart();
                return start != null && i.contains(start);
            }
        };
    }


    LogInstance.Condition closedInInterval(final Interval i) {
        return new LogInstance.Condition() {
            @Override
            public boolean test(LogInstance instance) {
                DateTime close = instance.getEnd();
                return close != null && i.contains(close);
            }
        };
    }

    LogInstance.Condition activeInInterval(final Interval i) {
        return new LogInstance.Condition() {
            @Override
            public boolean test(LogInstance instance) {
                DateTime start = instance.getStart();
                DateTime close = instance.getEnd();
                return i.getEnd().isAfter(start) && (close == null || i.getStart().isBefore(close));
            }
        };
    }

    @Override
    public void preprocessLog() {

        try {

            log.info("Preprocessing log...");
            DateTime start = DateTime.now();

            logProvider.registerListener(new LogListener() {
                public void update(LogEntry entry) {
                    if (map.containsKey(entry.getInstanceId())) {
                        byte[] entriesJson = map.get(entry.getInstanceId());
                        try {
                            List<LogEntry> entries = reader.readValue(entriesJson);
                            entries.add(entry);

                            entriesJson = mapper.writeValueAsBytes(entries);
                        } catch (IOException e) {
                            log.log(Level.WARNING, "Error preprocessing log", e);
                        }
                        map.put(entry.getInstanceId(), entriesJson);
                    } else {
                        List<LogEntry> entries = new ArrayList<LogEntry>();
                        entries.add(entry);
                        try {
                            map.put(entry.getInstanceId(), mapper.writeValueAsBytes(entries));
                        } catch (IOException e) {
                            log.log(Level.WARNING, "Error preprocessing log", e);
                        }
                    }
                }
            });

            logProvider.processLog();

            log.info("Finished preprocessing (Took " + new Duration(start, DateTime.now()).getMillis() + " ms)");

            start = DateTime.now();
            log.info("Preparing data...");

            for (String id : map.keySet()) {
                List<LogEntry> entries = reader.readValue(map.get(id));
                LogInstance instance = new LogInstance();
                instance.setInstanceId(id);
                Collections.sort(entries, new Comparator<LogEntry>() {
                    public int compare(LogEntry o1, LogEntry o2) {
                        return o1.getTimeStamp().compareTo(o2.getTimeStamp());
                    }
                });

                instance.setEntries(entries);
                instance.setStart(entries.get(0).getTimeStamp());
                LogEntry last = entries.get(entries.size() - 1);
                if (endMatcher.matches(last)) {
                    instance.setEnd(last.getTimeStamp());
                }

                map.put(id, mapper.writeValueAsBytes(instance));

            }

            log.info("Finished preparing data (Took " + new Duration(start, DateTime.now()).getMillis() + " ms)");


        } catch (IOException e) {
            log.log(Level.WARNING, "Error preprocessing log", e);
        }

    }

    @Override
    public void close() {
        db.close();
    }

    public static class LogInstance {
        public interface Condition {
            boolean test(LogInstance instance);
        }

        private String instanceId;
        private List<LogEntry> entries;
        @JsonSerialize(using = ToStringSerializer.class)
        @JsonDeserialize(using = DateTimeDeserializer.class)
        private DateTime start;
        @JsonSerialize(using = ToStringSerializer.class)
        @JsonDeserialize(using = DateTimeDeserializer.class)
        private DateTime end;

        public LogInstance(){}

        public String getInstanceId() {
            return instanceId;
        }

        public void setInstanceId(String instanceId) {
            this.instanceId = instanceId;
        }

        public List<LogEntry> getEntries() {
            return entries;
        }

        public void setEntries(List<LogEntry> entries) {
            this.entries = entries;
        }

        public DateTime getStart() {
            return start;
        }

        public void setStart(DateTime start) {
            this.start = start;
        }

        public DateTime getEnd() {
            return end;
        }

        public void setEnd(DateTime end) {
            this.end = end;
        }
    }

    private class MapLogProvider extends AbstractLogProvider {


        Interval interval;
        LogInstance.Condition predicate;
        List<String> firstFive = new ArrayList<String>();
        int counter = 0;

        public MapLogProvider(Interval interval, LogInstance.Condition predicate) {
            this.predicate = predicate;
            this.interval = interval;
        }

        @Override
        public void processLog() {
            for (String id : map.keySet()) {
                LogInstance instance = null;
                try {
                    instance = mapper.readValue(map.get(id), LogInstance.class);
                    if (predicate.test(instance)) {
                        counter++;
                        if (counter < 5) {
                            firstFive.add(id);
                        }
                        for (LogEntry entry : instance.getEntries()) {
                            updateListeners(entry);
                        }
                    }
                } catch (IOException e) {
                    log.log(Level.WARNING, "Error processing log for: " + interval, e);
                }

            }

            log.info("Selected " + counter + " instances for: " + interval.toString() + " " + firstFive.toString());
        }
    }
}