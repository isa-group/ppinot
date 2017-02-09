package es.us.isa.ppinot.model.state;

import es.us.isa.ppinot.model.condition.TimeInstantCondition;

import java.sql.Time;

/**
 * ComplexState
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class ComplexState implements RuntimeState {

    public static enum Type {FOLLOWS, LEADSTO, LEADSTOCYCLIC}

    private TimeInstantCondition first;
    private TimeInstantCondition last;
    private Type type;
    
    public ComplexState() {
    }

    public ComplexState(TimeInstantCondition first, TimeInstantCondition last, Type type) {
        this.first = first;
        this.last = last;
        this.type = type;
    }

    public TimeInstantCondition getFirst() {
        return first;
    }

    public TimeInstantCondition getLast() {
        return last;
    }

    public Type getType() {
        return type;
    }
}
