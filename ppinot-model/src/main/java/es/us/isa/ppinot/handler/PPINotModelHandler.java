package es.us.isa.ppinot.handler;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.handler.ModelHandler;
import es.us.isa.ppinot.model.PPISet;

import java.util.Collection;

public interface PPINotModelHandler extends Bpmn20ModelHandler {
    public Collection<PPISet> getPPISets();

    public void setPPISets(Collection<PPISet> ppiSets);

    public PPISet getPPISet(String processId);

    public void setPPISet(PPISet ppiSet);

}
