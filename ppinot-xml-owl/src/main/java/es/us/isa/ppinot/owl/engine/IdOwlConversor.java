package es.us.isa.ppinot.owl.engine;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedObject;

import java.util.HashSet;
import java.util.Set;

/**
 * User: resinas
 * Date: 29/01/13
 * Time: 09:49
 */
public class IdOwlConversor {

    public Set<String> toId(Set<? extends OWLNamedObject> entities) {
        Set<String> result = new HashSet<String>();
        for(OWLNamedObject i: entities) {
            result.add(i.getIRI().getFragment());
        }
        return result;

    }

    public Set<String> toOwlId(IRI prefix, Set<String> ids) {
        Set<String> result = new HashSet<String>();
        for(String id: ids) {
            result.add(prefix.toString() + "#" + id);
        }
        return result;

    }

}
