package es.us.isa.ppinot.evaluation.logs;

import java.util.*;

/**
 * StatsLogListener
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class StatsLogListener implements LogListener {
    private Map<String, Map<String, Integer>> counter = new HashMap<String, Map<String, Integer>>();
    private Set<String> keys;
    private long numEntries;
    private Set<String> instances;

    public StatsLogListener(String... keys) {
        this.keys = new HashSet<String>(Arrays.asList(keys));
        this.numEntries = 0;
        this.instances = new HashSet<>();
    }

    public void update(LogEntry entry) {
        Map<String, Object> data = entry.getData();
        Set<String> currentKeys = keys;
        if (currentKeys.isEmpty()) {
            currentKeys = data.keySet();
        }
        for (String key : currentKeys) {
            updateCountWith(data.get(key).toString(), getOrCreateNewCounter(key));
        }
        numEntries++;
        if (!instances.contains(entry.getInstanceId())) {
            instances.add(entry.getInstanceId());
        }
    }

    private Map<String, Integer> getOrCreateNewCounter(String key) {
        Map<String, Integer> result = counter.get(key);
        if (result == null) {
            result = new HashMap<String, Integer>();
            counter.put(key, result);
        }

        return result;
    }

    private void updateCountWith(String estado, Map<String, Integer> counter) {
        Integer count = counter.get(estado);
        if (count == null) {
            count = 0;
        }
        counter.put(estado, count + 1);
    }

    public Map<String, Map<String, Integer>> getCounter() {
        return counter;
    }

    public void printStats() {
        System.out.println("Num entries: " + numEntries);
        System.out.println("Num instances: " + instances.size());
        for (String key : counter.keySet()) {
            System.out.println(key + ":");
            System.out.println("--------------------");
            printStats(counter.get(key));
        }
    }

    private void printStats(Map<String, Integer> statsEstado) {
        for (String key : statsEstado.keySet()) {
            System.out.println(key + ": " + statsEstado.get(key));
        }
    }

}
