package es.us.isa.ppinot.evaluation.selectors;

import es.us.isa.ppinot.model.DataContentSelection;
import es.us.isa.ppinot.model.PastDataContentSelection;

/**
 * DataContentSelectorFactory
 * Copyright (C) 2017 Universidad de Sevilla
 *
 * @author resinas
 */
public class DataContentSelectorFactory {

    public static DataContentSelector create(DataContentSelection dataContentSelection) {
        if (dataContentSelection instanceof PastDataContentSelection) {
            return new PastDataContentSelector(dataContentSelection);
        } else {
            return new RegularDataContentSelector(dataContentSelection);
        }
    }
}
