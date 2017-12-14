package es.us.isa.ppinot.handler;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import es.us.isa.ppinot.model.MeasuresCollection;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

/**
 * URLMeasuresCollectionHandlerTest
 * Copyright (C) 2017 Universidad de Sevilla
 *
 * @author resinas
 */
public class URLMeasuresCollectionHandlerTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(7777);

    @Rule
    public WireMockRule wireMockRuleHttps = new WireMockRule(wireMockConfig().httpsPort(8443));


    @Test
    public void test() throws MalformedURLException {
        stubFor(get(urlEqualTo("/measures.json"))
                .willReturn(okJson(timeWithTemplate())));

        Map<String, String> parameters = new HashMap();
        parameters.put("priority", "P4");
        parameters.put("deadline", "< 2");
        parameters.put("schedules", ""
                + "["
                + "{\"from\":\"1/1\",\"to\":\"6/14\",\"schedule\":\"L-VT09:00-18:00\"},"
                + "{\"from\":\"6/15\",\"to\":\"9/15\",\"schedule\":\"L-VT08:30-15:00\"},"
                + "{\"from\":\"9/16\",\"to\":\"12/23\",\"schedule\":\"L-VT09:00-18:00\"},"
                + "{\"from\":\"12/24\",\"to\":\"12/24\",\"schedule\":\"L-VT09:00-13:00\"},"
                + "{\"from\":\"12/25\",\"to\":\"12/30\",\"schedule\":\"L-VT09:00-18:00\"},"
                + "{\"from\":\"12/31\",\"to\":\"12/31\",\"schedule\":\"L-VT09:00-13:00\"}"
                + "]");

        URL measuresURL = new URL("http://localhost:7777/measures.json");

        URLMeasuresCollectionHandler handler = null;
        handler = new URLMeasuresCollectionHandler(parameters);

        MeasuresCollection measuresCollection = handler.loadCollection(measuresURL);

        Assert.assertTrue(measuresCollection.getDefinitions().size() > 0);

    }




    private String timeWithTemplate() {
        return "{\n" +
                "    \"name\": \"Test\",\n" +
                "    \"description\": \"Measures for test SLA\",\n" +
                "    \"definitions\": [{\n" +
                "        \"kind\": \"TimeMeasure\",\n" +
                "        \"id\": \"issue_trlp_duration\",\n" +
                "        \"unitOfMeasure\": \"hours\",\n" +
                "        \"from\": {\n" +
                "            \"kind\": \"TimeInstantCondition\",\n" +
                "            \"appliesTo\": \"Data\",\n" +
                "            \"changesToState\": {\n" +
                "                \"dataObjectState\": {\n" +
                "                    \"name\": \"ACTION == 'SDK_HIST_ASSIGNED'\"\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        \"to\": {\n" +
                "            \"kind\": \"TimeInstantCondition\",\n" +
                "            \"appliesTo\": \"Data\",\n" +
                "            \"changesToState\": {\n" +
                "                \"dataObjectState\": {\n" +
                "                    \"name\": \"ACTION == 'SDK_HIST_TRANS_PHASE' && VALUE == 'Cierre temporal'\"\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        \"timeMeasureType\": \"LINEAR\",\n" +
                "        \"considerOnly\": \"${schedule}\",\n" +
                "        \"computeUnfinished\": false,\n" +
                "        \"firstTo\": false\n" +
                "    }]}";
    }
}