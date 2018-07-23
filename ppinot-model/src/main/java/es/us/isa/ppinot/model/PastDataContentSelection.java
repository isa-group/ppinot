package es.us.isa.ppinot.model;

/**
 * PastDataContentSelection
 * Copyright (C) 2017 Universidad de Sevilla
 *
 * @author resinas
 */
public class PastDataContentSelection extends DataContentSelection {
    private String separator = "-";

    public PastDataContentSelection() {
        super();
    }

    public PastDataContentSelection(String selection, String dataobjectId) {
        super(selection, dataobjectId);
    }

    public PastDataContentSelection(String selection, String dataobjectId, String separator) {
        super(selection, dataobjectId);
        this.separator = separator;
    }

    public String getSeparator() {
        return separator;
    }
}
