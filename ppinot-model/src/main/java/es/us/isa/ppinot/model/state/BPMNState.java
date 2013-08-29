package es.us.isa.ppinot.model.state;

// estados en los que puede estar un elemento BPMN
public enum BPMNState implements RuntimeState {
    READY, ACTIVE, WITHDRAWN, COMPLETING, COMPLETED, FAILING, FAILED, TERMINATING, TERMINATED, COMPENSATING, COMPENSATED,
    EXECUTING, DELETED,
}
