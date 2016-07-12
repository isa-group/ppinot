package es.us.isa.ppinot.evaluation.logs;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.MappingJsonFactory;
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

    private JSONLogEntryLoader loader;
    private JsonParser parser;

    public JSONLogProvider(Reader reader, JSONLogEntryLoader loader) {
        this.loader = loader;
        try {
            this.parser = new MappingJsonFactory().createJsonParser(reader);
        } catch (IOException e) {
            throw new RuntimeException("JSON Stream cannot be read");
        }
    }

    @Override
    public void processLog() {
        try {
            if (parser.nextToken() != JsonToken.START_ARRAY) {
                return;
            }

            while (parser.nextToken() == JsonToken.START_OBJECT) {
                JsonNode n = parser.readValueAs(JsonNode.class);
                updateListeners(loader.loadEvent(n));
            }

            parser.close();

        } catch (IOException e) {
            throw new RuntimeException("Unable to parse JSON", e);
        }
    }
}
