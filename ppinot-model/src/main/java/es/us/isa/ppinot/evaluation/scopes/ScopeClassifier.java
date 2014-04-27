package es.us.isa.ppinot.evaluation.scopes;

import es.us.isa.ppinot.evaluation.MeasureScope;
import es.us.isa.ppinot.evaluation.logs.LogEntry;
import es.us.isa.ppinot.evaluation.logs.LogListener;
import es.us.isa.ppinot.evaluation.matchers.StateMatcher;
import es.us.isa.ppinot.model.state.GenericState;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * ScopeClassifier
 * Copyright (C) 2013 Universidad de Sevilla
 *
 * @author resinas
 */
public abstract class ScopeClassifier implements LogListener {

    private Map<String, ProcessInstance> instances;


    protected ScopeClassifier() {
        this.instances = new HashMap<String, ProcessInstance>();
    }

    public abstract Collection<MeasureScope> listScopes();

    protected abstract void instanceEnded(ProcessInstance instance);

    @Override
    public void update(LogEntry entry) {
        if (startsProcess(entry)) {
            ProcessInstance instance = new ProcessInstance(entry.getProcessId(), entry.getInstanceId(), entry.getTimeStamp());
            instances.put(measureIdOf(entry), instance);
        } else if (endsProcess(entry)) {
            ProcessInstance instance = instances.get(measureIdOf(entry));
            if (instance != null) {
                instance.ends(entry.getTimeStamp());
                instanceEnded(instance);
            }
        }
    }

    private boolean endsProcess(LogEntry entry) {
        return LogEntry.ElementType.process.equals(entry.getElementType()) &&
                StateMatcher.matches(entry.getEventType(), GenericState.END);
    }

    private boolean startsProcess(LogEntry entry) {
        return LogEntry.ElementType.process.equals(entry.getElementType()) &&
                StateMatcher.matches(entry.getEventType(), GenericState.START);
    }

    private String measureIdOf(LogEntry entry) {
        return entry.getProcessId() + "#" + entry.getInstanceId();
    }

    protected class ProcessInstance {
        private String processId;
        private String instanceId;
        private DateTime start;
        private DateTime end;

        protected ProcessInstance(String processId, String instanceId, DateTime start) {
            this.processId = processId;
            this.instanceId = instanceId;
            this.start = start;
        }

        private void ends(DateTime end) {
            this.end = end;
        }

        protected Interval getInterval() {
            return new Interval(start, end);
        }

        protected String getProcessId() {
            return processId;
        }

        protected String getInstanceId() {
            return instanceId;
        }

        protected DateTime getStart() {
            return start;
        }

        protected DateTime getEnd() {
            return end;
        }
    }
}
