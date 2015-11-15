package es.us.isa.ppinot.handler;

import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.MeasuresCollection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * URLMeasuresCollectionHandler
 * Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class URLMeasuresCollectionHandler {
    private Map<URL, MeasuresCollection> collectionCache;

    public URLMeasuresCollectionHandler() {
        this.collectionCache = new HashMap<URL, MeasuresCollection>();
    }

    public MeasureDefinition load(URL url) {
        String reference = url.getRef();
        if (reference == null || reference.isEmpty()) {
            return null;
        }

        MeasuresCollection definitions = loadCollection(url);

        return definitions.getById(reference);
    }

    public MeasuresCollection loadCollection(URL url) {
        URL cleanedURL = url;
        try {
            cleanedURL = cleanURL(url);

        } catch(Exception e) {

        }

        MeasuresCollection measuresCollection = collectionCache.get(cleanedURL);

        if (measuresCollection == null) {
            measuresCollection = downloadCollection(cleanedURL);
            collectionCache.put(cleanedURL, measuresCollection);
        }

        return measuresCollection;
    }

    private URL cleanURL(URL url) throws MalformedURLException {
        return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile());
    }

    public MeasuresCollection downloadCollection(URL url) {
        MeasuresCollection measureDefinitions = new MeasuresCollection();
        URLConnection urlConn = null;
        InputStreamReader in = null;
        try {
            urlConn = url.openConnection();
            if (urlConn != null)
                urlConn.setReadTimeout(60 * 1000);
            if (urlConn != null && urlConn.getInputStream() != null) {
                in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
                BufferedReader bufferedReader = new BufferedReader(in);

                JSONMeasuresCollectionHandler handler = new JSONMeasuresCollectionHandler();
                handler.load(bufferedReader);
                measureDefinitions = handler.getCollection();

                bufferedReader.close();
                in.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception while calling URL:" + url, e);
        }

        return measureDefinitions;
    }

}
