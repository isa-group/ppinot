package es.us.isa.ppinot.handler.transformation;

import es.us.isa.ppinot.model.state.BPMNState;
import es.us.isa.ppinot.model.state.DataObjectState;
import es.us.isa.ppinot.model.state.GenericState;
import es.us.isa.ppinot.model.state.RuntimeState;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * StateTransformer
 *
 * @author resinas
 */
public class StateTransformer {
    public static RuntimeState transform(String state) {
        RuntimeState result = null;
        try{
            result = GenericState.valueOf(state.toUpperCase());
            if (result == null) {
                result = BPMNState.valueOf(state.toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            // ignores
        }


        return result;
    }

    public static RuntimeState[] transformDataState(String states) {
        List<RuntimeState> result = new ArrayList<RuntimeState>();

        if (states != null) {
            for (String s : states.split(",")) {
                result.add(new DataObjectState(s.trim()));
            }
        }

        return result.toArray(new RuntimeState[1]);
    }

    public static String transformDataState(Set<RuntimeState> states) {
        StringBuilder builder = new StringBuilder();
        if (states != null) {
            Iterator<RuntimeState> it = states.iterator();
            if (it.hasNext()) {
                builder.append(it.next());
            }
            while (it.hasNext()) {
                builder.append(", ").append(it.next());
            }
        }
        return builder.toString();
    }
}
