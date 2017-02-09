package es.us.isa.ppinot.evaluation.logs;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * JSONLogProviderTest
 * Copyright (C) 2016 Universidad de Sevilla
 *
 * @author resinas
 */
public class JSONLogProviderTest {

    private String testJson = "[\n" +
            "  {\n" +
            "    \"AUTOR\": \"PSJC\",\n" +
            "    \"FECHA\": \"2016-01-15T07:41:06+0100\",\n" +
            "    \"PADRE\": 1,\n" +
            "    \"RECLAMACION\": 0,\n" +
            "    \"CANCELADA\": 0,\n" +
            "    \"PRIORIDAD\": \"P3\",\n" +
            "    \"NODO\": \"CORDOBA\",\n" +
            "    \"RESOLUTOR\": \"CGS\",\n" +
            "    \"DESCORG\": \"reassign responsibility\",\n" +
            "    \"SOPORTE\": 0,\n" +
            "    \"FECHAMOD\": \"2016-01-16T01:16:36+0100\",\n" +
            "    \"SISTEMA\": \"CA-SDM\",\n" +
            "    \"GRUPOAUTOR\": \"ERROR\",\n" +
            "    \"TIPOLOGIA\": \"Peticion\",\n" +
            "    \"ASUNTO\": \"Dar de alta el recurso 239751J en DRI.  \",\n" +
            "    \"ESTADO\": \"Abierta\",\n" +
            "    \"ASIGNATARIO\": \"CAU, SEGUIMIENTO DE INCIDENCIAS\",\n" +
            "    \"CENTRO\": \"ESARS\",\n" +
            "    \"ORIGEN\": \"WEBSVC\",\n" +
            "    \"ID\": \"3412452\",\n" +
            "    \"REAPERTURAINC\": 0,\n" +
            "    \"TYPEORG\": \"TR\",\n" +
            "    \"RECURSO\": \"0000893719\",\n" +
            "    \"MOTIVOCIERRE\": \" \"\n" +
            "  },\n" +
            "  {\n" +
            "    \"AUTOR\": \"PSJC\",\n" +
            "    \"FECHA\": \"2016-01-15T07:43:46+0100\",\n" +
            "    \"PADRE\": 1,\n" +
            "    \"RECLAMACION\": 0,\n" +
            "    \"CANCELADA\": 0,\n" +
            "    \"PRIORIDAD\": \"P3\",\n" +
            "    \"NODO\": \"CORDOBA\",\n" +
            "    \"RESOLUTOR\": \"CGS\",\n" +
            "    \"DESCORG\": \"reassign responsibility\",\n" +
            "    \"SOPORTE\": 0,\n" +
            "    \"FECHAMOD\": \"2016-01-16T01:08:30+0100\",\n" +
            "    \"SISTEMA\": \"CA-SDM\",\n" +
            "    \"GRUPOAUTOR\": \"ERROR\",\n" +
            "    \"TIPOLOGIA\": \"Peticion\",\n" +
            "    \"ASUNTO\": \"Dar de alta el recurso 239760U en DRI.  \",\n" +
            "    \"ESTADO\": \"Abierta\",\n" +
            "    \"ASIGNATARIO\": \"CAU, SEGUIMIENTO DE INCIDENCIAS\",\n" +
            "    \"CENTRO\": \"ESARS\",\n" +
            "    \"ORIGEN\": \"WEBSVC\",\n" +
            "    \"ID\": \"3412453\",\n" +
            "    \"REAPERTURAINC\": 0,\n" +
            "    \"TYPEORG\": \"TR\",\n" +
            "    \"RECURSO\": \"FEBEBHA089201\",\n" +
            "    \"MOTIVOCIERRE\": \" \"\n" +
            "  },\n" +
            "  {\n" +
            "    \"AUTOR\": \"BAE\",\n" +
            "    \"FECHA\": \"2016-01-15T07:46:55+0100\",\n" +
            "    \"PADRE\": 1,\n" +
            "    \"RECLAMACION\": 0,\n" +
            "    \"CANCELADA\": 0,\n" +
            "    \"PRIORIDAD\": \"P3\",\n" +
            "    \"NODO\": \"CORDOBA\",\n" +
            "    \"RESOLUTOR\": \"SSA2\",\n" +
            "    \"DESCORG\": \"reassign responsibility\",\n" +
            "    \"SOPORTE\": 1,\n" +
            "    \"FECHAMOD\": \"2016-01-16T01:00:50+0100\",\n" +
            "    \"SISTEMA\": \"CA-SDM\",\n" +
            "    \"GRUPOAUTOR\": \"SSA2\",\n" +
            "    \"TIPOLOGIA\": \"Tarea.Operacion\",\n" +
            "    \"ASUNTO\": \"Comprobar que se han realizado correctamente las copias de s...\",\n" +
            "    \"ESTADO\": \"Abierta\",\n" +
            "    \"ASIGNATARIO\": \"BAE\",\n" +
            "    \"CENTRO\": \"HVP\",\n" +
            "    \"ORIGEN\": \"WEBSVC\",\n" +
            "    \"ID\": \"3412454\",\n" +
            "    \"REAPERTURAINC\": 0,\n" +
            "    \"TYPEORG\": \"TR\",\n" +
            "    \"RECURSO\": \"SRVFICH32011\",\n" +
            "    \"MOTIVOCIERRE\": \" \"\n" +
            "  }]";

    @Test
    public void testProcessLog() throws Exception {
        JSONLogEntryLoader loader = new JSONLogEntryLoader("ID", "ACCION", "FECHA");

        JSONLogProvider provider = new JSONLogProvider(new StringReader(testJson), loader);

        StatsLogListener stats = new StatsLogListener();

        provider.registerListener(stats);

        provider.processLog();

        Assert.assertNotNull(stats.getCounter().get("AUTOR"));
        Assert.assertNotNull(stats.getCounter().get("AUTOR").get("BAE"));
        Assert.assertNotNull(stats.getCounter().get("AUTOR").get("PSJC"));
        Assert.assertEquals(1, (int) stats.getCounter().get("AUTOR").get("BAE"));
        Assert.assertEquals(2, (int) stats.getCounter().get("AUTOR").get("PSJC"));

    }



}