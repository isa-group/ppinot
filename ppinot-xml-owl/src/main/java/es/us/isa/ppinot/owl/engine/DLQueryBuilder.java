package es.us.isa.ppinot.owl.engine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * User: resinas
 * Date: 30/01/13
 * Time: 21:13
 */
public class DLQueryBuilder {

    private String query;
    private Map<String, String> parameters;
    private static final String PARAMCHAR = "?";

    public DLQueryBuilder(String query) {
        this.query = query;
        parameters = new HashMap<String, String>();
    }

    public DLQueryBuilder setParameter(String parameter, String value) {
        parameters.put(parameter, "<"+value+">");
        return this;
    }

    public DLQueryBuilder setParameter(String parameter, Collection<String> elements) {
        parameters.put(parameter, toDLSet(elements));
        return this;
    }

    @Override
    public String toString() {
        String finalQuery = query;

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            finalQuery = finalQuery.replaceAll("\\" + PARAMCHAR + entry.getKey(), entry.getValue());
        }

        return finalQuery;
    }


    public static String toDLSet(Collection<String> elements) {
    	
/*
    	String[] tmp = new ArrayList<String>(elements).toArray();
    	StringUtils.join(tmp, ',');
*/    	
        StringBuilder sb = new StringBuilder("{");
        Iterator<String> it = elements.iterator();
        while(it.hasNext()) {
            sb.append("<").append(it.next()).append(">");
            if (it.hasNext()) {
                sb.append(", ");
            }
        }

        sb.append("}");

        return sb.toString();
    }
}
