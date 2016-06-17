package es.us.isa.ppinot.evaluation.logs;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

/**
 * JSONLogProvider
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class JSONLogProvider extends AbstractLogProvider {

    private Reader reader;
    private JSONLogEntryLoader loader;
    private ObjectMapper mapper;

    public JSONLogProvider(Reader reader, JSONLogEntryLoader loader) {
        this.reader = reader;
        this.loader = loader;
        this.mapper = new ObjectMapper();
    }

    @Override
    public void processLog() {
        JsonNode node = null;
        try {
            node = mapper.readTree(reader);
        } catch (IOException e) {
            throw new RuntimeException("Unable to parse JSON", e);
        }

        Iterator<JsonNode> it = node.getElements();

        while (it.hasNext()) {
            JsonNode n = it.next();
            updateListeners(loader.loadEvent(n));
        }
    }
}
