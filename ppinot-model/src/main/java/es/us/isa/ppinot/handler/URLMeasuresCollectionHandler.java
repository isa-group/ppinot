package es.us.isa.ppinot.handler;

import es.us.isa.ppinot.model.MeasureDefinition;
import es.us.isa.ppinot.model.MeasuresCollection;
import es.us.isa.ppinot.model.schedule.Holidays;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * URLMeasuresCollectionHandler Copyright (C) 2015 Universidad de Sevilla
 *
 * @author resinas
 */
public class URLMeasuresCollectionHandler {

    private static final Logger log = Logger.getLogger(URLMeasuresCollectionHandler.class.getName());

    private Map<URL, MeasuresCollection> collectionCache;
    private Map<String, String> parameters;
    private Holidays holidays;

    public URLMeasuresCollectionHandler() {
        this(new HashMap<String, String>());
    }

    public URLMeasuresCollectionHandler(Map<String, String> parameters) {
        this.collectionCache = new HashMap<URL, MeasuresCollection>();
        this.parameters = parameters;
    }

    public URLMeasuresCollectionHandler(Map<String, String> parameters, Holidays holidays) {
        this.collectionCache = new HashMap<URL, MeasuresCollection>();
        this.parameters = parameters;
        this.holidays = holidays;
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
        URL cleanedURL = cleanURL(url);

        MeasuresCollection measuresCollection = collectionCache.get(cleanedURL);

        if (measuresCollection == null) {
            measuresCollection = downloadCollection(cleanedURL);
            collectionCache.put(cleanedURL, measuresCollection);
        }

        return measuresCollection;
    }

    private URL cleanURL(URL url) {
        try {
            return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile());
        } catch (MalformedURLException e) {
            log.log(Level.INFO, "Could not clean URL: " + url, e);
        }
        return url;
    }

    public MeasuresCollection downloadCollection(URL url) {
        if (url.getProtocol().equals("https")) {
            return downloadCollectionHTTPS(url);
        } else {
            return downloadCollectionHTTP(url);
        }
    }

    private MeasuresCollection downloadCollectionHTTP(URL url) {
        MeasuresCollection measureDefinitions;
        URLConnection urlConn = null;
        try {
            urlConn = url.openConnection();
            measureDefinitions = getMeasuresCollection(urlConn);
        } catch (Exception e) {
            throw new RuntimeException("Exception while calling URL:" + url + " Error: " + e.getMessage());
        }

        return measureDefinitions;
    }

    private MeasuresCollection downloadCollectionHTTPS(URL url) {
        MeasuresCollection measureDefinitions;
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            measureDefinitions = getMeasuresCollection(conn);


        } catch (Exception e) {
            throw new RuntimeException("Exception while calling URL:" + url + " Error: " + e.getMessage());
        }

        return measureDefinitions;
    }

    private MeasuresCollection getMeasuresCollection(URLConnection urlConn) throws IOException {
        MeasuresCollection measureDefinitions = new MeasuresCollection();

        InputStreamReader in;
        if (urlConn != null) {
            urlConn.setReadTimeout(60 * 1000);
        }
        if (urlConn != null && urlConn.getInputStream() != null) {
            in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
            BufferedReader bufferedReader = new BufferedReader(in);

            JSONMeasuresCollectionHandler handler = new JSONMeasuresCollectionHandler(holidays);
            handler.load(bufferedReader, parameters);
            measureDefinitions = handler.getCollection();

            bufferedReader.close();
            in.close();
        }
        return measureDefinitions;
    }

}
