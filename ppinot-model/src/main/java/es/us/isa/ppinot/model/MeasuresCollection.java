package es.us.isa.ppinot.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * MeasuresCollection
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class MeasuresCollection {
    private String name;
    private String description;

    private List<MeasureDefinition> definitions;

    public MeasuresCollection() {
        this.definitions = new ArrayList<MeasureDefinition>();
    }

    public MeasuresCollection(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<MeasureDefinition> getDefinitions() {
        return definitions;
    }

    public void addDefinitions(List<MeasureDefinition> definitions) {
        this.definitions.addAll(definitions);
    }

    public void addDefinition(MeasureDefinition definition) {
        this.definitions.add(definition);
    }

    public MeasureDefinition getById(String id) {
        MeasureDefinition definition = null;

        for (MeasureDefinition def : definitions) {
            Map<String, MeasureDefinition> allIds = def.getAllIds();
            if (allIds.containsKey(id)) {
                definition = allIds.get(id);
                break;
            }
        }

        return definition;
    }
}
