package es.us.isa.ppinot.owl.converter;

import es.us.isa.bpmn.handler.Bpmn20ModelHandler;
import es.us.isa.bpmn.handler.Bpmn20ModelHandlerInterface;
import es.us.isa.bpmn.owl.converter.BPMN2OWLConverter;
import es.us.isa.bpmn.owl.converter.BPMN2OWLConverterInterface;
import es.us.isa.ppinot.handler.PpiNotModelHandler;
import es.us.isa.ppinot.handler.PpiNotModelHandlerInterface;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;

/**
 * Clase con los casos de prueba del proyecto
 * 
 * @author Edelia
 *
 */
public class Ppinot2OwlTest {

	private String bpmnBaseIRI = "http://www.isa.us.es/ontologies/bpmn/";
	private String ppinotBaseIRI = "http://www.isa.us.es/ontologies/ppinot/";
	private String bpmnOntologyURI;

    private OWLOntology ppinotOntology;

    public File targetDir(String dir){
        File target = null;
        URI relPath = null;
        try {
            relPath = getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
            File codeSource = new File(relPath);
            if ( codeSource.exists() ) {
                if ( codeSource.isDirectory() ) {
                    File parentFile = codeSource.getParentFile();
                    target = new File(parentFile, dir);
                    if (! target.exists())
                        target.mkdir();
                }
            }
        } catch (URISyntaxException e) {
        }

        if (target == null) {
            try {
                target = File.createTempFile("test", "target");
            } catch (IOException e) {
                throw new IllegalStateException("Cannot create target directory", e);
            }
        }

        return target;
    }

	/**
	 * Genera la ontologia OWL con la URI especificada a partir de un xml
	 * 
	 * @param sourceFile XML a partir del cual se crea la ontologia
	 * @return
	 */
	private Boolean ppinot2Owl(String sourceFile) {

//		String caminoOrigen = "D:/eclipse-appweb-indigo/ppinot-repository/bpmn-xml-owl/target/test-classes/xml/";
//		String targetPath = "D:/eclipse-appweb-indigo/ppinot-repository/ppinot-xml-owl/target/test-classes/owl/";
//        String targetPath = "./";
        String targetPath = targetDir("test-ontologies").getAbsolutePath()+File.separator;

        try {
            OWLOntologyManager owlManager = OWLManager.createOWLOntologyManager();

            InputStream sourceStream = getClass().getResourceAsStream(sourceFile);
            Bpmn20ModelHandlerInterface bpmnModelHandler = new Bpmn20ModelHandler();
//            bpmnModelHandler.load(caminoOrigen, sourceFile);
            bpmnModelHandler.load(sourceStream);

            BPMN2OWLConverterInterface bpmnConverter = new BPMN2OWLConverter(bpmnBaseIRI, owlManager);
            bpmnConverter.convertToOwlOntology(bpmnModelHandler);
            bpmnConverter.saveOntology(targetPath, "ExpressionsOWLBpmn-" + sourceFile.substring(0, sourceFile.indexOf(".")) + ".owl");
            bpmnOntologyURI = bpmnConverter.getOntologyURI();

            sourceStream = getClass().getResourceAsStream(sourceFile);
            PpiNotModelHandlerInterface ppinotModelHandler = new PpiNotModelHandler();
//            ppinotModelHandler.load(caminoOrigen, sourceFile);
            ppinotModelHandler.load(sourceStream);

            PPINOT2OWLConverterInterface ppinotConverter = new PPINOT2OWLConverter(ppinotBaseIRI, owlManager);
            ppinotConverter.setBpmnData(bpmnOntologyURI, bpmnModelHandler);
            ppinotOntology = ppinotConverter.convertToOwlOntology(ppinotModelHandler);

            // guarda la ontologia generada
            ppinotConverter.saveOntology(targetPath, "ExpressionsOWLPpinot-" + sourceFile.substring(0, sourceFile.indexOf(".")) + ".owl");

            return true;
        } catch (JAXBException e) {

            e.printStackTrace();
        } catch (OWLOntologyCreationException e) {

            e.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

	@Test
	public void testBase() {
		String testFile = "base.bpmn20.xml";

        assertTrue(ppinot2Owl(testFile));

        PpiNotTestAnalyser analyser = new PpiNotTestAnalyser(ppinotOntology, bpmnOntologyURI);
        
        // time instance measure
        assertTrue(analyser.isLinearTimeIntanceMeasure("sid-3B70F4A1-8F9F-4909-A3AC-8F06FBDC6C97"));
        assertTrue(analyser.isFrom("sid-3B70F4A1-8F9F-4909-A3AC-8F06FBDC6C97", "startsid-364A55D5-916D-4669-B4A5-29E82CACEB8C"));
        assertTrue(analyser.isTo("sid-3B70F4A1-8F9F-4909-A3AC-8F06FBDC6C97", "endsid-0B0B61E3-89F3-4028-9746-D122688C2E93"));
        assertTrue(analyser.isActivityStart("startsid-364A55D5-916D-4669-B4A5-29E82CACEB8C"));
        assertTrue(analyser.isActivityEnd("endsid-0B0B61E3-89F3-4028-9746-D122688C2E93"));
        assertTrue(analyser.isAppliedTo("startsid-364A55D5-916D-4669-B4A5-29E82CACEB8C", "sid-364A55D5-916D-4669-B4A5-29E82CACEB8C"));
        assertTrue(analyser.isAppliedTo("endsid-0B0B61E3-89F3-4028-9746-D122688C2E93", "sid-0B0B61E3-89F3-4028-9746-D122688C2E93"));

        // count instance measure
        assertTrue(analyser.isCountInstanceMeasure("sid-F23CA3E1-9D3C-46B5-BB7F-E37E27DB729E"));
        assertTrue(analyser.isWhen("sid-F23CA3E1-9D3C-46B5-BB7F-E37E27DB729E", "TimeInstantsid-F23CA3E1-9D3C-46B5-BB7F-E37E27DB729E"));
        assertTrue(analyser.isActivityStart("TimeInstantsid-F23CA3E1-9D3C-46B5-BB7F-E37E27DB729E"));
        assertTrue(analyser.isAppliedTo("TimeInstantsid-F23CA3E1-9D3C-46B5-BB7F-E37E27DB729E", "sid-364A55D5-916D-4669-B4A5-29E82CACEB8C"));
        
        // state condition instance measure
        assertTrue(analyser.isStateConditionInstanceMeasure("sid-70636F2E-63D1-47D3-A17C-8832E2F4890F"));
        assertTrue(analyser.isMeets("sid-70636F2E-63D1-47D3-A17C-8832E2F4890F", "sid-70636F2E-63D1-47D3-A17C-8832E2F4890FRunning"));
        assertTrue(analyser.isStateCondition("sid-70636F2E-63D1-47D3-A17C-8832E2F4890FRunning"));
        assertTrue(analyser.isAppliedTo("sid-70636F2E-63D1-47D3-A17C-8832E2F4890FRunning", "sid-0B0B61E3-89F3-4028-9746-D122688C2E93"));

        // data instance measure
        assertTrue(analyser.isDataInstanceMeasure("sid-98764615-A771-4C7D-8619-16008DAE4679"));
        assertTrue(analyser.isMeasuresData("sid-98764615-A771-4C7D-8619-16008DAE4679", "DCSelectionsid-98764615-A771-4C7D-8619-16008DAE4679"));
        assertTrue(analyser.isDataContentSelection("DCSelectionsid-98764615-A771-4C7D-8619-16008DAE4679"));
        assertTrue(analyser.isData("DCSelectionsid-98764615-A771-4C7D-8619-16008DAE4679", "sid-9832DD51-05CF-4B45-BB61-C46740621C4F"));
        
        // data condition instance measure
        assertTrue(analyser.isDataPropertyConditionInstanceMeasure("sid-7B5191F7-B178-47CB-AC6E-0A99C1577DC3"));
        assertTrue(analyser.isMeets("sid-7B5191F7-B178-47CB-AC6E-0A99C1577DC3", "sid-7B5191F7-B178-47CB-AC6E-0A99C1577DC3Restriction"));
        assertTrue(analyser.isDataPropertyCondition("sid-7B5191F7-B178-47CB-AC6E-0A99C1577DC3Restriction"));
        assertTrue(analyser.isAppliedTo("sid-7B5191F7-B178-47CB-AC6E-0A99C1577DC3Restriction", "sid-9832DD51-05CF-4B45-BB61-C46740621C4F"));
	}

	@Test
	public void testAggregated() {
		String nomFichOrigen = "aggregated.bpmn20.xml";

		assertTrue(ppinot2Owl(nomFichOrigen));

        PpiNotTestAnalyser analyser = new PpiNotTestAnalyser(ppinotOntology, bpmnOntologyURI);
        
        // time aggregated measure
        assertTrue(analyser.isAggregatedMeasure("sid-C11F5344-C7B6-43EF-B6CD-4064C1AE8BA9"));
        assertTrue(analyser.isAggregates("sid-C11F5344-C7B6-43EF-B6CD-4064C1AE8BA9", "sid-0c306446-8c69-449a-8da4-d2780209b03b"));
        assertTrue(analyser.isCyclicTimeIntanceMeasure("sid-0c306446-8c69-449a-8da4-d2780209b03b"));
        assertTrue(analyser.isFrom("sid-0c306446-8c69-449a-8da4-d2780209b03b", "startsid-D5274D42-55B0-46A0-8EB6-B99B186D3873"));
        assertTrue(analyser.isTo("sid-0c306446-8c69-449a-8da4-d2780209b03b", "endsid-2171B3CC-36ED-43B6-B6B8-339732CCB2BD"));
        assertTrue(analyser.isActivityStart("startsid-D5274D42-55B0-46A0-8EB6-B99B186D3873"));
        assertTrue(analyser.isActivityEnd("endsid-2171B3CC-36ED-43B6-B6B8-339732CCB2BD"));
        assertTrue(analyser.isAppliedTo("startsid-D5274D42-55B0-46A0-8EB6-B99B186D3873", "sid-D5274D42-55B0-46A0-8EB6-B99B186D3873"));
        assertTrue(analyser.isAppliedTo("endsid-2171B3CC-36ED-43B6-B6B8-339732CCB2BD", "sid-2171B3CC-36ED-43B6-B6B8-339732CCB2BD"));
        
        // count aggregated measure
        assertTrue(analyser.isAggregatedMeasure("sid-85ADE8CD-C32A-4F59-B519-F9A59D9FF38A"));
        assertTrue(analyser.isAggregates("sid-85ADE8CD-C32A-4F59-B519-F9A59D9FF38A", "sid-4ca76a0a-5e7a-4ad8-a35c-23319b904340"));
        assertTrue(analyser.isCountInstanceMeasure("sid-4ca76a0a-5e7a-4ad8-a35c-23319b904340"));
        assertTrue(analyser.isWhen("sid-4ca76a0a-5e7a-4ad8-a35c-23319b904340", "TimeInstantsid-4ca76a0a-5e7a-4ad8-a35c-23319b904340"));
        assertTrue(analyser.isActivityStart("TimeInstantsid-4ca76a0a-5e7a-4ad8-a35c-23319b904340"));
        assertTrue(analyser.isAppliedTo("TimeInstantsid-4ca76a0a-5e7a-4ad8-a35c-23319b904340", "sid-D5274D42-55B0-46A0-8EB6-B99B186D3873"));
        
        // state condition aggregated measure
        assertTrue(analyser.isAggregatedMeasure("sid-8A0FCBF6-2486-4855-9D33-1921E646BB6C"));
        assertTrue(analyser.isAggregates("sid-8A0FCBF6-2486-4855-9D33-1921E646BB6C", "sid-9cee8a2a-e4a6-4211-a9d8-5f4948cd63e7"));
       	assertTrue(analyser.isStateConditionInstanceMeasure("sid-9cee8a2a-e4a6-4211-a9d8-5f4948cd63e7"));
        assertTrue(analyser.isMeets("sid-9cee8a2a-e4a6-4211-a9d8-5f4948cd63e7", "sid-9cee8a2a-e4a6-4211-a9d8-5f4948cd63e7Running"));
        assertTrue(analyser.isStateCondition("sid-9cee8a2a-e4a6-4211-a9d8-5f4948cd63e7Running"));
        assertTrue(analyser.isAppliedTo("sid-9cee8a2a-e4a6-4211-a9d8-5f4948cd63e7Running", "sid-2171B3CC-36ED-43B6-B6B8-339732CCB2BD"));

        // data aggregated measure
        assertTrue(analyser.isAggregatedMeasure("sid-58F97E64-1E94-4F39-A597-5854A11428B5"));
        assertTrue(analyser.isAggregates("sid-58F97E64-1E94-4F39-A597-5854A11428B5", "sid-fb7bdcc9-d180-46ea-85a3-1072e21307fd"));
        assertTrue(analyser.isDataInstanceMeasure("sid-fb7bdcc9-d180-46ea-85a3-1072e21307fd"));
        assertTrue(analyser.isMeasuresData("sid-fb7bdcc9-d180-46ea-85a3-1072e21307fd", "DCSelectionsid-fb7bdcc9-d180-46ea-85a3-1072e21307fd"));
        assertTrue(analyser.isDataContentSelection("DCSelectionsid-fb7bdcc9-d180-46ea-85a3-1072e21307fd"));
        assertTrue(analyser.isData("DCSelectionsid-fb7bdcc9-d180-46ea-85a3-1072e21307fd", "sid-E55E3010-5D7F-4D21-B968-D4D109D625CA"));
        
        // data condition aggregated measure
        assertTrue(analyser.isAggregatedMeasure("sid-06FF961A-1AF5-4EAB-A72F-E5B5B6356629"));
        assertTrue(analyser.isAggregates("sid-06FF961A-1AF5-4EAB-A72F-E5B5B6356629", "sid-7cbea7e7-436d-417c-ac30-fd56113f6b92"));
        assertTrue(analyser.isDataPropertyConditionInstanceMeasure("sid-7cbea7e7-436d-417c-ac30-fd56113f6b92"));
        assertTrue(analyser.isMeets("sid-7cbea7e7-436d-417c-ac30-fd56113f6b92", "sid-7cbea7e7-436d-417c-ac30-fd56113f6b92Restriction"));
        assertTrue(analyser.isDataPropertyCondition("sid-7cbea7e7-436d-417c-ac30-fd56113f6b92Restriction"));
        assertTrue(analyser.isAppliedTo("sid-7cbea7e7-436d-417c-ac30-fd56113f6b92Restriction", "sid-E55E3010-5D7F-4D21-B968-D4D109D625CA"));

	}

	@Test
	public void testDerived() {
		String nomFichOrigen = "derived.bpmn20.xml";
        
		assertTrue(ppinot2Owl(nomFichOrigen));

        PpiNotTestAnalyser analyser = new PpiNotTestAnalyser(ppinotOntology, bpmnOntologyURI);
        
        // derived single instance measure
        assertTrue(analyser.isDerivedSingleInstanceMeasure("sid-B07ED6A7-C614-4B29-BCE4-43E3AD2DE62B"));
        assertTrue(analyser.isCalculated("sid-B07ED6A7-C614-4B29-BCE4-43E3AD2DE62B", "sid-23A454B9-10BF-4B1A-B8EA-513192F6E188"));
        assertTrue(analyser.isCalculated("sid-B07ED6A7-C614-4B29-BCE4-43E3AD2DE62B", "sid-8C5D7567-02A9-4447-9F8C-B53EFE9805CC"));
        	// count measure sid-23A454B9-10BF-4B1A-B8EA-513192F6E188
        assertTrue(analyser.isCountInstanceMeasure("sid-23A454B9-10BF-4B1A-B8EA-513192F6E188"));
        assertTrue(analyser.isWhen("sid-23A454B9-10BF-4B1A-B8EA-513192F6E188", "TimeInstantsid-23A454B9-10BF-4B1A-B8EA-513192F6E188"));
        assertTrue(analyser.isActivityEnd("TimeInstantsid-23A454B9-10BF-4B1A-B8EA-513192F6E188"));
        assertTrue(analyser.isAppliedTo("TimeInstantsid-23A454B9-10BF-4B1A-B8EA-513192F6E188", "sid-CC51D716-391D-4FC4-8196-3A0078B8344A"));
	    	// count measure sid-8C5D7567-02A9-4447-9F8C-B53EFE9805CC
	    assertTrue(analyser.isCountInstanceMeasure("sid-8C5D7567-02A9-4447-9F8C-B53EFE9805CC"));
	    assertTrue(analyser.isWhen("sid-8C5D7567-02A9-4447-9F8C-B53EFE9805CC", "TimeInstantsid-8C5D7567-02A9-4447-9F8C-B53EFE9805CC"));
	    assertTrue(analyser.isActivityStart("TimeInstantsid-8C5D7567-02A9-4447-9F8C-B53EFE9805CC"));
	    assertTrue(analyser.isAppliedTo("TimeInstantsid-8C5D7567-02A9-4447-9F8C-B53EFE9805CC", "sid-68C5EF66-23E7-4E35-9F38-9DD42B4CFFB9"));

        // derived multinstance measure
        assertTrue(analyser.isDerivedMultiInstanceMeasure("sid-3FA7D0BE-4102-432E-8EE9-2C9A7F582B1C"));
        assertTrue(analyser.isCalculated("sid-3FA7D0BE-4102-432E-8EE9-2C9A7F582B1C", "sid-0807D633-3DF8-4BD0-BE6D-848BE7D322FE"));
        assertTrue(analyser.isCalculated("sid-3FA7D0BE-4102-432E-8EE9-2C9A7F582B1C", "sid-B6C62384-BC0E-4132-B7E6-CF64A4A5E97B"));
        	// data property condition aggregated measure sid-0807D633-3DF8-4BD0-BE6D-848BE7D322FE
        assertTrue(analyser.isAggregatedMeasure("sid-0807D633-3DF8-4BD0-BE6D-848BE7D322FE"));
        assertTrue(analyser.isAggregates("sid-0807D633-3DF8-4BD0-BE6D-848BE7D322FE", "sid-9e4da4af-5526-4feb-9932-2b063feee420"));
        assertTrue(analyser.isDataPropertyConditionInstanceMeasure("sid-9e4da4af-5526-4feb-9932-2b063feee420"));
        assertTrue(analyser.isMeets("sid-9e4da4af-5526-4feb-9932-2b063feee420", "sid-9e4da4af-5526-4feb-9932-2b063feee420Restriction"));
        assertTrue(analyser.isDataPropertyCondition("sid-9e4da4af-5526-4feb-9932-2b063feee420Restriction"));
        assertTrue(analyser.isAppliedTo("sid-9e4da4af-5526-4feb-9932-2b063feee420Restriction", "sid-8B684F86-01DC-4296-AC51-86D38637873C"));
        	// data aggregated measure sid-B6C62384-BC0E-4132-B7E6-CF64A4A5E97B
        assertTrue(analyser.isAggregatedMeasure("sid-B6C62384-BC0E-4132-B7E6-CF64A4A5E97B"));
        assertTrue(analyser.isAggregates("sid-B6C62384-BC0E-4132-B7E6-CF64A4A5E97B", "sid-46ee571c-7158-4cff-ad97-07e96fe59627"));
        assertTrue(analyser.isDataInstanceMeasure("sid-46ee571c-7158-4cff-ad97-07e96fe59627"));
        assertTrue(analyser.isMeasuresData("sid-46ee571c-7158-4cff-ad97-07e96fe59627", "DCSelectionsid-46ee571c-7158-4cff-ad97-07e96fe59627"));
        assertTrue(analyser.isDataContentSelection("DCSelectionsid-46ee571c-7158-4cff-ad97-07e96fe59627"));
        assertTrue(analyser.isData("DCSelectionsid-46ee571c-7158-4cff-ad97-07e96fe59627", "sid-8B684F86-01DC-4296-AC51-86D38637873C"));
        
	}

	@Test
	public void testIsgroupedbyConnector() {
		String nomFichOrigen = "isgroupedby-connector.bpmn20.xml";

		assertTrue(ppinot2Owl(nomFichOrigen));

        PpiNotTestAnalyser analyser = new PpiNotTestAnalyser(ppinotOntology, bpmnOntologyURI);

        // count aggregated measure sid-276FC2FA-BA15-46A7-8203-ADD1F2C834EC
        assertTrue(analyser.isAggregatedMeasure("sid-276FC2FA-BA15-46A7-8203-ADD1F2C834EC"));
        assertTrue(analyser.isAggregates("sid-276FC2FA-BA15-46A7-8203-ADD1F2C834EC", "sid-13863188-2979-4fcd-90bf-7af4054ccc5b"));
        assertTrue(analyser.isCountInstanceMeasure("sid-13863188-2979-4fcd-90bf-7af4054ccc5b"));
        assertTrue(analyser.isWhen("sid-13863188-2979-4fcd-90bf-7af4054ccc5b", "TimeInstantsid-13863188-2979-4fcd-90bf-7af4054ccc5b"));
        assertTrue(analyser.isActivityStart("TimeInstantsid-13863188-2979-4fcd-90bf-7af4054ccc5b"));
        assertTrue(analyser.isAppliedTo("TimeInstantsid-13863188-2979-4fcd-90bf-7af4054ccc5b", "sid-B001BA42-6F8F-4EC0-BF6D-AB46F4F5EF8E"));
        assertTrue(analyser.isGroupedBy("sid-276FC2FA-BA15-46A7-8203-ADD1F2C834EC", "DCSelectionsid-276FC2FA-BA15-46A7-8203-ADD1F2C834EC"));
        assertTrue(analyser.isDataContentSelection("DCSelectionsid-276FC2FA-BA15-46A7-8203-ADD1F2C834EC"));
        assertTrue(analyser.isData("DCSelectionsid-276FC2FA-BA15-46A7-8203-ADD1F2C834EC", "sid-58AEF45E-1C11-4923-9071-290DB1DD0F0D"));
        
        // count aggregated measure sid-905C337C-96E4-4D41-8465-62DD8A36221C
        assertTrue(analyser.isAggregatedMeasure("sid-905C337C-96E4-4D41-8465-62DD8A36221C"));
        assertTrue(analyser.isAggregates("sid-905C337C-96E4-4D41-8465-62DD8A36221C", "sid-F2220D80-9C7F-419A-88D6-CE6A11ED5339"));
        assertTrue(analyser.isCountInstanceMeasure("sid-F2220D80-9C7F-419A-88D6-CE6A11ED5339"));
        assertTrue(analyser.isWhen("sid-F2220D80-9C7F-419A-88D6-CE6A11ED5339", "TimeInstantsid-F2220D80-9C7F-419A-88D6-CE6A11ED5339"));
        assertTrue(analyser.isActivityStart("TimeInstantsid-F2220D80-9C7F-419A-88D6-CE6A11ED5339"));
        assertTrue(analyser.isAppliedTo("TimeInstantsid-F2220D80-9C7F-419A-88D6-CE6A11ED5339", "sid-DB049C83-0C6F-4643-993D-9087B1C0D0CB"));
        assertTrue(analyser.isGroupedBy("sid-905C337C-96E4-4D41-8465-62DD8A36221C", "DCSelectionsid-905C337C-96E4-4D41-8465-62DD8A36221C"));
        assertTrue(analyser.isDataContentSelection("DCSelectionsid-905C337C-96E4-4D41-8465-62DD8A36221C"));
        assertTrue(analyser.isData("DCSelectionsid-905C337C-96E4-4D41-8465-62DD8A36221C", "sid-58AEF45E-1C11-4923-9071-290DB1DD0F0D"));
	}

	@Test
	public void testAggregatedConnector() {
		String nomFichOrigen = "aggregated-connector.bpmn20.xml";

		assertTrue(ppinot2Owl(nomFichOrigen));

        PpiNotTestAnalyser analyser = new PpiNotTestAnalyser(ppinotOntology, bpmnOntologyURI);

        // count measure
        assertTrue(analyser.isAggregatedMeasure("sid-C87139EB-DB41-4515-96F7-12200A024FE4"));
        assertTrue(analyser.isAggregates("sid-C87139EB-DB41-4515-96F7-12200A024FE4", "sid-31EDBFC8-132E-42FD-94EB-330187F56AE4"));
        assertTrue(analyser.isCountInstanceMeasure("sid-31EDBFC8-132E-42FD-94EB-330187F56AE4"));
        assertTrue(analyser.isWhen("sid-31EDBFC8-132E-42FD-94EB-330187F56AE4", "TimeInstantsid-31EDBFC8-132E-42FD-94EB-330187F56AE4"));
        assertTrue(analyser.isActivityStart("TimeInstantsid-31EDBFC8-132E-42FD-94EB-330187F56AE4"));
        assertTrue(analyser.isAppliedTo("TimeInstantsid-31EDBFC8-132E-42FD-94EB-330187F56AE4", "sid-1BC2E6C5-FC04-42C8-8D3D-27F7A20485D6"));

        assertTrue(analyser.isDerivedSingleInstanceMeasure("sid-951D10E8-024A-48A6-A103-BC2EBC7BEFDC"));
        assertTrue(analyser.isCalculated("sid-951D10E8-024A-48A6-A103-BC2EBC7BEFDC", "sid-31EDBFC8-132E-42FD-94EB-330187F56AE4"));
	}
	
	@Test
	public void testLanes() {
		String nomFichOrigen = "lanes.bpmn20.xml";

		assertTrue(ppinot2Owl(nomFichOrigen));

        PpiNotTestAnalyser analyser = new PpiNotTestAnalyser(ppinotOntology, bpmnOntologyURI);
        
        // time measure
        assertTrue(analyser.isLinearTimeIntanceMeasure("sid-D8EE818D-0470-49E9-A6F9-194D76966C7F"));
        assertTrue(analyser.isFrom("sid-D8EE818D-0470-49E9-A6F9-194D76966C7F", "startsid-AE412B24-8E94-4C0C-A83E-40A3A2CFFFDC"));
        assertTrue(analyser.isTo("sid-D8EE818D-0470-49E9-A6F9-194D76966C7F", "endsid-E7776982-9DCD-48D1-BF8E-BEC23728B124"));
        assertTrue(analyser.isActivityStart("startsid-AE412B24-8E94-4C0C-A83E-40A3A2CFFFDC"));
        assertTrue(analyser.isActivityEnd("endsid-E7776982-9DCD-48D1-BF8E-BEC23728B124"));
        assertTrue(analyser.isAppliedTo("startsid-AE412B24-8E94-4C0C-A83E-40A3A2CFFFDC", "sid-AE412B24-8E94-4C0C-A83E-40A3A2CFFFDC"));
        assertTrue(analyser.isAppliedTo("endsid-E7776982-9DCD-48D1-BF8E-BEC23728B124", "sid-E7776982-9DCD-48D1-BF8E-BEC23728B124"));

        // count measure
        assertTrue(analyser.isCountInstanceMeasure("sid-E6EBD957-60EE-4AEF-BDB5-D066FC07DC44"));
        assertTrue(analyser.isWhen("sid-E6EBD957-60EE-4AEF-BDB5-D066FC07DC44", "TimeInstantsid-E6EBD957-60EE-4AEF-BDB5-D066FC07DC44"));
        assertTrue(analyser.isActivityStart("TimeInstantsid-E6EBD957-60EE-4AEF-BDB5-D066FC07DC44"));
        assertTrue(analyser.isAppliedTo("TimeInstantsid-E6EBD957-60EE-4AEF-BDB5-D066FC07DC44", "sid-AE412B24-8E94-4C0C-A83E-40A3A2CFFFDC"));
	}

	@Test
	public void testPpi() {
		String nomFichOrigen = "ppi.bpmn20.xml";

		assertTrue(ppinot2Owl(nomFichOrigen));

        PpiNotTestAnalyser analyser = new PpiNotTestAnalyser(ppinotOntology, bpmnOntologyURI);
        
        // PPI de count measure
        assertTrue(analyser.isPpi("sid-eaf8645a-d548-4e9f-8a39-bf3aa78504fd"));
        assertTrue(analyser.isDefinition("sid-eaf8645a-d548-4e9f-8a39-bf3aa78504fd", "sid-8F97D7AD-B211-429E-911B-F278B5D12D98"));
        assertTrue(analyser.isCountInstanceMeasure("sid-8F97D7AD-B211-429E-911B-F278B5D12D98"));
        assertTrue(analyser.isWhen("sid-8F97D7AD-B211-429E-911B-F278B5D12D98", "TimeInstantsid-8F97D7AD-B211-429E-911B-F278B5D12D98"));
        assertTrue(analyser.isActivityStart("TimeInstantsid-8F97D7AD-B211-429E-911B-F278B5D12D98"));
        assertTrue(analyser.isAppliedTo("TimeInstantsid-8F97D7AD-B211-429E-911B-F278B5D12D98", "sid-680678C2-CDEE-4852-85E7-CAA68E08DF78"));
        
        // PPI de time aggregated measure
        assertTrue(analyser.isPpi("sid-ef26d078-4a10-41d5-98f4-7dda5539df30"));
        assertTrue(analyser.isDefinition("sid-ef26d078-4a10-41d5-98f4-7dda5539df30", "sid-CC7889D8-3920-4A37-B90B-6BE0A8923F08"));

        assertTrue(analyser.isAggregatedMeasure("sid-CC7889D8-3920-4A37-B90B-6BE0A8923F08"));
        assertTrue(analyser.isAggregates("sid-CC7889D8-3920-4A37-B90B-6BE0A8923F08", "sid-9a4caf47-3141-4370-9cfc-4902aabf53f3"));
        assertTrue(analyser.isLinearTimeIntanceMeasure("sid-9a4caf47-3141-4370-9cfc-4902aabf53f3"));
        assertTrue(analyser.isFrom("sid-9a4caf47-3141-4370-9cfc-4902aabf53f3", "startsid-680678C2-CDEE-4852-85E7-CAA68E08DF78"));
        assertTrue(analyser.isTo("sid-9a4caf47-3141-4370-9cfc-4902aabf53f3", "endsid-A96CA54F-FEAC-4E71-AE44-BDE3C98FB11B"));
        assertTrue(analyser.isActivityStart("startsid-680678C2-CDEE-4852-85E7-CAA68E08DF78"));
        assertTrue(analyser.isActivityEnd("endsid-A96CA54F-FEAC-4E71-AE44-BDE3C98FB11B"));
        assertTrue(analyser.isAppliedTo("startsid-680678C2-CDEE-4852-85E7-CAA68E08DF78", "sid-680678C2-CDEE-4852-85E7-CAA68E08DF78"));
        assertTrue(analyser.isAppliedTo("endsid-A96CA54F-FEAC-4E71-AE44-BDE3C98FB11B", "sid-A96CA54F-FEAC-4E71-AE44-BDE3C98FB11B"));
	}

	@Test
	public void testSingleInstanceDerivedAggregated() {
		String nomFichOrigen = "single-instance-derived-aggregated.bpmn20.xml";

		assertTrue(ppinot2Owl(nomFichOrigen));
        PpiNotTestAnalyser analyser = new PpiNotTestAnalyser(ppinotOntology, bpmnOntologyURI);

        // count measure
        assertTrue(analyser.isAggregatedMeasure("sid-C87139EB-DB41-4515-96F7-12200A024FE4"));
        assertTrue(analyser.isAggregates("sid-C87139EB-DB41-4515-96F7-12200A024FE4", "sid-951D10E8-024A-48A6-A103-BC2EBC7BEFDC"));
        
        assertTrue(analyser.isDerivedSingleInstanceMeasure("sid-951D10E8-024A-48A6-A103-BC2EBC7BEFDC"));
        assertTrue(analyser.isCalculated("sid-951D10E8-024A-48A6-A103-BC2EBC7BEFDC", "sid-31EDBFC8-132E-42FD-94EB-330187F56AE4"));
        
        assertTrue(analyser.isCountInstanceMeasure("sid-31EDBFC8-132E-42FD-94EB-330187F56AE4"));
        assertTrue(analyser.isWhen("sid-31EDBFC8-132E-42FD-94EB-330187F56AE4", "TimeInstantsid-31EDBFC8-132E-42FD-94EB-330187F56AE4"));
        assertTrue(analyser.isActivityStart("TimeInstantsid-31EDBFC8-132E-42FD-94EB-330187F56AE4"));
        assertTrue(analyser.isAppliedTo("TimeInstantsid-31EDBFC8-132E-42FD-94EB-330187F56AE4", "sid-1BC2E6C5-FC04-42C8-8D3D-27F7A20485D6"));

	}	

	//@Test
	public void testMany() {
		String nomFichOrigen = "RFC-simplified+PPIs.bpmn20.xml";

		assertTrue(ppinot2Owl(nomFichOrigen));

	    PpiNotTestAnalyser analyser = new PpiNotTestAnalyser(ppinotOntology, bpmnOntologyURI);
		
        //PPI2-TimeAggregatedMeasure (LinearTimeMeasure)
        assertTrue("PPI2-isPpi", analyser.isPpi("sid-b1b40ce5-fc62-4035-ac68-1cfb012ebf01"));
        assertTrue("PPI2-isDefinition", analyser.isDefinition("sid-b1b40ce5-fc62-4035-ac68-1cfb012ebf01", "sid-5B1C361D-246B-4A1C-A9FD-B045B05F4A4B"));
        assertTrue("PPI2-isAggregatedMeasure", analyser.isAggregatedMeasure("sid-5B1C361D-246B-4A1C-A9FD-B045B05F4A4B"));
        assertTrue("PPI2-isAggregates", analyser.isAggregates("sid-5B1C361D-246B-4A1C-A9FD-B045B05F4A4B", "sid-ffbd4fe1-31d2-4d4d-86f7-f78e6bc64ca8"));
        assertTrue("PPI2-isLinearTimeIntanceMeasure", analyser.isLinearTimeIntanceMeasure("sid-ffbd4fe1-31d2-4d4d-86f7-f78e6bc64ca8"));
        assertTrue("PPI2-isFrom", analyser.isFrom("sid-ffbd4fe1-31d2-4d4d-86f7-f78e6bc64ca8", "startsid-DDF4FE82-F265-4834-8F12-B49B59BECEF8"));
        assertTrue("PPI2-isActivityStart", analyser.isActivityStart("startsid-DDF4FE82-F265-4834-8F12-B49B59BECEF8"));
        assertTrue("PPI2-isAppliedTo", analyser.isAppliedTo("startsid-DDF4FE82-F265-4834-8F12-B49B59BECEF8", "sid-DDF4FE82-F265-4834-8F12-B49B59BECEF8"));
        assertTrue("PPI2-isTo", analyser.isTo("sid-ffbd4fe1-31d2-4d4d-86f7-f78e6bc64ca8", "endsid-DDF4FE82-F265-4834-8F12-B49B59BECEF8"));
        assertTrue("PPI2-isActivityEnd", analyser.isActivityEnd("endsid-DDF4FE82-F265-4834-8F12-B49B59BECEF8"));
        assertTrue("PPI2-isAppliedTo", analyser.isAppliedTo("endsid-DDF4FE82-F265-4834-8F12-B49B59BECEF8", "sid-DDF4FE82-F265-4834-8F12-B49B59BECEF8"));

		//PPI3-DerivedMulti-InstanceMeasure (CountAggregatedMeasure y DataPropertyConditionAggregatedMeasure)
        assertTrue("PPI3-isPpi1",analyser.isPpi("sid-f12eab59-5437-49be-afed-3ea39bf0af0e"));
        assertTrue("PPI3-isPpi2",analyser.isPpi("sid-50ec1572-8ca9-4a8d-8621-34b7146dbbde"));
        assertTrue("PPI3-isPpi3",analyser.isPpi("sid-e61b5838-2913-42c5-be44-0fbd0160185f"));
        assertTrue("PPI3-isDefinition1",analyser.isDefinition("sid-f12eab59-5437-49be-afed-3ea39bf0af0e", "sid-3DCD4482-896F-4B7D-9B24-872EB9ADFF65"));
        assertTrue("PPI3-isDefinition2",analyser.isDefinition("sid-50ec1572-8ca9-4a8d-8621-34b7146dbbde", "sid-5AE72772-03B8-482C-8798-09513C07A216"));
        assertTrue("PPI3-isDefinition3",analyser.isDefinition("sid-e61b5838-2913-42c5-be44-0fbd0160185f", "sid-8C0CBFEB-669B-44C6-AD1A-210A733FA166"));
        assertTrue("PPI3-isDerivedMultiInstanceMeasure",analyser.isDerivedMultiInstanceMeasure("sid-3DCD4482-896F-4B7D-9B24-872EB9ADFF65"));
        assertTrue("PPI3-isCalculated1",analyser.isCalculated("sid-3DCD4482-896F-4B7D-9B24-872EB9ADFF65", "sid-5AE72772-03B8-482C-8798-09513C07A216"));
        assertTrue("PPI3-isAggregatedMeasure1",analyser.isAggregatedMeasure("sid-5AE72772-03B8-482C-8798-09513C07A216"));
        assertTrue("PPI3-isAggregates1",analyser.isAggregates("sid-5AE72772-03B8-482C-8798-09513C07A216", "sid-1158b4d6-f854-430d-a52e-ad66cac513e8"));
        assertTrue("PPI3-isDataPropertyConditionInstanceMeasure",analyser.isDataPropertyConditionInstanceMeasure("sid-1158b4d6-f854-430d-a52e-ad66cac513e8"));
        assertTrue("PPI3-isMeets", analyser.isMeets("sid-1158b4d6-f854-430d-a52e-ad66cac513e8", "sid-1158b4d6-f854-430d-a52e-ad66cac513e8CorrectiveChange"));
        assertTrue("PPI3-isDataPropertyCondition", analyser.isDataPropertyCondition("sid-1158b4d6-f854-430d-a52e-ad66cac513e8CorrectiveChange"));
        assertTrue("PPI3-isAppliedTo1", analyser.isAppliedTo("sid-1158b4d6-f854-430d-a52e-ad66cac513e8CorrectiveChange", "sid-BF1BF36F-651C-4F36-806B-2A763E8B51A7"));
        assertTrue("PPI3-isCalculated2",analyser.isCalculated("sid-3DCD4482-896F-4B7D-9B24-872EB9ADFF65", "sid-8C0CBFEB-669B-44C6-AD1A-210A733FA166"));
        assertTrue("PPI3-isAggregatedMeasure2",analyser.isAggregatedMeasure("sid-8C0CBFEB-669B-44C6-AD1A-210A733FA166"));
        assertTrue("PPI3-isAggregates2",analyser.isAggregates("sid-8C0CBFEB-669B-44C6-AD1A-210A733FA166", "sid-93bb2a0c-7d2f-48d4-ac60-45e88986f8ef"));
        assertTrue("PPI3-isCountInstanceMeasure", analyser.isCountInstanceMeasure("sid-93bb2a0c-7d2f-48d4-ac60-45e88986f8ef"));
        assertTrue("PPI3-isWhen", analyser.isWhen("sid-93bb2a0c-7d2f-48d4-ac60-45e88986f8ef", "TimeInstantsid-93bb2a0c-7d2f-48d4-ac60-45e88986f8ef"));
        assertTrue("PPI3-isEventTrigger", analyser.isEventTrigger("TimeInstantsid-93bb2a0c-7d2f-48d4-ac60-45e88986f8ef"));
        assertTrue("PPI3-isAppliedTo2", analyser.isAppliedTo("TimeInstantsid-93bb2a0c-7d2f-48d4-ac60-45e88986f8ef", "sid-FCD39DEF-F581-4553-AD79-770A075A2992"));

        //PPI6- StateConditionAggregatedMeasure
        assertTrue("PPI6-isPpi1",analyser.isPpi("sid-722d6130-f69e-42a7-bf35-7375aa4eea5a"));
        assertTrue("PPI6-isPpi2",analyser.isPpi("sid-53ffa021-4895-4036-8472-b275cceb717e"));
        assertTrue("PPI6-isDefinition",analyser.isDefinition("sid-722d6130-f69e-42a7-bf35-7375aa4eea5a", "sid-786C6028-1978-433E-9C6D-C23DC19F0D2E"));
        assertTrue("PPI6-isDefinition",analyser.isDefinition("sid-53ffa021-4895-4036-8472-b275cceb717e", "sid-B67BB75F-848D-4D70-89C1-CCF3B328BA00"));
        assertTrue("PPI6-isAggregatedMeasure",analyser.isAggregatedMeasure("sid-B67BB75F-848D-4D70-89C1-CCF3B328BA00"));
		assertTrue("PPI6-isAggregates",analyser.isAggregates("sid-B67BB75F-848D-4D70-89C1-CCF3B328BA00", "sid-786C6028-1978-433E-9C6D-C23DC19F0D2E"));
		assertTrue("PPI6-isStateConditionInstanceMeasure",analyser.isStateConditionInstanceMeasure("sid-786C6028-1978-433E-9C6D-C23DC19F0D2E"));
		assertTrue("PPI6-isMeets",analyser.isMeets("sid-786C6028-1978-433E-9C6D-C23DC19F0D2E", "sid-786C6028-1978-433E-9C6D-C23DC19F0D2Eactive"));
        assertTrue("PPI6-isStateCondition",analyser.isStateCondition("sid-786C6028-1978-433E-9C6D-C23DC19F0D2Eactive"));
        assertTrue("PPI6-isAppliedTo",analyser.isAppliedTo("sid-786C6028-1978-433E-9C6D-C23DC19F0D2Eactive", "sid-202DF64D-1397-4599-9D60-3B42B7224F48"));
        
        //PPI8- CountAggregatedMeasure con isGroupedBy
        assertTrue("PPI8-isPpi",analyser.isPpi("sid-ad3f2a09-31cb-485f-a7c1-eee2b868d4f0"));
        assertTrue("PPI8-isDefinition",analyser.isDefinition("sid-ad3f2a09-31cb-485f-a7c1-eee2b868d4f0", "sid-35F4C304-E98F-4C04-B999-89B7B77A1E17"));
        assertTrue("PPI8-isAggregatedMeasure",analyser.isAggregatedMeasure("sid-35F4C304-E98F-4C04-B999-89B7B77A1E17"));
        assertTrue("PPI8-isAggregates",analyser.isAggregates("sid-35F4C304-E98F-4C04-B999-89B7B77A1E17", "sid-457ec96d-410d-48aa-b388-dc9b4d203f87"));
		assertTrue("PPI8-isCountInstanceMeasure",analyser.isCountInstanceMeasure("sid-457ec96d-410d-48aa-b388-dc9b4d203f87"));
		assertTrue("PPI8-isWhen",analyser.isWhen("sid-457ec96d-410d-48aa-b388-dc9b4d203f87", "TimeInstantsid-457ec96d-410d-48aa-b388-dc9b4d203f87"));
        assertTrue("PPI8-isEventTrigger",analyser.isEventTrigger("TimeInstantsid-457ec96d-410d-48aa-b388-dc9b4d203f87"));
        assertTrue("PPI8-isAppliedTo",analyser.isAppliedTo("TimeInstantsid-457ec96d-410d-48aa-b388-dc9b4d203f87", "sid-2DB95E04-AB48-43FE-A67B-B0169885AB4F"));
        assertTrue("PPI8-isGroupedBy",analyser.isGroupedBy("sid-35F4C304-E98F-4C04-B999-89B7B77A1E17", "DCSelectionsid-35F4C304-E98F-4C04-B999-89B7B77A1E17"));
        assertTrue("PPI8-isDataContentSelection",analyser.isDataContentSelection("DCSelectionsid-35F4C304-E98F-4C04-B999-89B7B77A1E17"));
        assertTrue("PPI8-isData",analyser.isData("DCSelectionsid-35F4C304-E98F-4C04-B999-89B7B77A1E17", "sid-C59544E2-20D8-42E5-AB67-A1471BAC4D24"));
        
        //PPI9-TimeAggregatedMeasure (LinearTimeMeasure)- proceso completo
        assertTrue("PPI9-isPpi",analyser.isPpi("sid-50135ec0-20a5-4bc5-ab2c-4fb912cc7783"));
        assertTrue("PPI9-isDefinition",analyser.isDefinition("sid-50135ec0-20a5-4bc5-ab2c-4fb912cc7783", "sid-2C3E6BF6-BAF0-4B32-B31B-34BDD7F2375E"));
        assertTrue("PPI9-isAggregatedMeasure",analyser.isAggregatedMeasure("sid-2C3E6BF6-BAF0-4B32-B31B-34BDD7F2375E"));
        assertTrue("PPI9-isAggregates",analyser.isAggregates("sid-2C3E6BF6-BAF0-4B32-B31B-34BDD7F2375E", "sid-440b80c1-71ef-406d-9417-5acdb244cd1b"));
        assertTrue("PPI9-isLinearTimeIntanceMeasure",analyser.isLinearTimeIntanceMeasure("sid-440b80c1-71ef-406d-9417-5acdb244cd1b"));
        assertTrue("PPI9-isFrom",analyser.isFrom("sid-440b80c1-71ef-406d-9417-5acdb244cd1b", "startsid-2DB95E04-AB48-43FE-A67B-B0169885AB4F"));
        assertTrue("PPI9-isEventTrigger",analyser.isEventTrigger("startsid-2DB95E04-AB48-43FE-A67B-B0169885AB4F"));
        assertTrue("PPI9-isAppliedTo",analyser.isAppliedTo("startsid-2DB95E04-AB48-43FE-A67B-B0169885AB4F", "sid-2DB95E04-AB48-43FE-A67B-B0169885AB4F"));
        assertTrue("PPI9-isTo",analyser.isTo("sid-440b80c1-71ef-406d-9417-5acdb244cd1b", "endsid-FCD39DEF-F581-4553-AD79-770A075A2992"));
        assertTrue("PPI9-isEventTrigger",analyser.isEventTrigger("endsid-FCD39DEF-F581-4553-AD79-770A075A2992"));
        assertTrue("PPI9-isAppliedTo",analyser.isAppliedTo("endsid-FCD39DEF-F581-4553-AD79-770A075A2992", "sid-FCD39DEF-F581-4553-AD79-770A075A2992"));

        //PPIX- Aggregated de DerivedSingle-InstanceMeasure usando DataMeasures
        assertTrue("PPIX-isPpi1",analyser.isPpi("sid-43a1e2d2-e033-4dc8-b933-a938b013153d"));
        assertTrue("PPIX-isPpi2",analyser.isPpi("sid-4014623c-a95d-438d-bdbd-0efc76c842ef"));
        assertTrue("PPIX-isPpi3",analyser.isPpi("sid-8442b02a-3f4d-4643-b529-266780c9af33"));
        assertTrue("PPIX-isPpi4",analyser.isPpi("sid-7f1b5250-203b-464f-b68e-9e5105b97c8a"));
        assertTrue("PPIX-isDefinition1",analyser.isDefinition("sid-43a1e2d2-e033-4dc8-b933-a938b013153d", "sid-B7C7F70E-4CDA-410B-B6D6-0DA5EDE18341"));
        assertTrue("PPIX-isDefinition2",analyser.isDefinition("sid-4014623c-a95d-438d-bdbd-0efc76c842ef", "sid-328C78CF-7AB8-4046-8AA0-220E90281FDB"));
        assertTrue("PPIX-isDefinition3",analyser.isDefinition("sid-8442b02a-3f4d-4643-b529-266780c9af33", "sid-25409B7D-BE71-4EF1-9473-BE9C30984AD4"));
        assertTrue("PPIX-isDefinition4",analyser.isDefinition("sid-7f1b5250-203b-464f-b68e-9e5105b97c8a", "sid-95F1F455-43F2-47B6-9FCA-46467D229493"));
        assertTrue("PPIX-isDerivedSingleInstanceMeasure",analyser.isDerivedSingleInstanceMeasure("sid-B7C7F70E-4CDA-410B-B6D6-0DA5EDE18341"));
        assertTrue("PPIX-isAggregatedMeasure",analyser.isAggregatedMeasure("sid-95F1F455-43F2-47B6-9FCA-46467D229493"));
        assertTrue("PPIX-isAggregates",analyser.isAggregates("sid-95F1F455-43F2-47B6-9FCA-46467D229493", "sid-B7C7F70E-4CDA-410B-B6D6-0DA5EDE18341"));
        assertTrue("PPIX-isCalculated1",analyser.isCalculated("sid-B7C7F70E-4CDA-410B-B6D6-0DA5EDE18341", "sid-328C78CF-7AB8-4046-8AA0-220E90281FDB"));
        assertTrue("PPIX-isDataInstanceMeasure1",analyser.isDataInstanceMeasure("sid-328C78CF-7AB8-4046-8AA0-220E90281FDB"));
        assertTrue("PPIX-isMeasuresData1",analyser.isMeasuresData("sid-328C78CF-7AB8-4046-8AA0-220E90281FDB", "DCSelectionsid-328C78CF-7AB8-4046-8AA0-220E90281FDB"));
        assertTrue("PPIX-isDataContentSelection1",analyser.isDataContentSelection("DCSelectionsid-328C78CF-7AB8-4046-8AA0-220E90281FDB"));
        assertTrue("PPIX-",analyser.isData("DCSelectionsid-328C78CF-7AB8-4046-8AA0-220E90281FDB", "sid-C59544E2-20D8-42E5-AB67-A1471BAC4D24"));
        assertTrue("PPIX-isCalculated2",analyser.isCalculated("sid-B7C7F70E-4CDA-410B-B6D6-0DA5EDE18341", "sid-25409B7D-BE71-4EF1-9473-BE9C30984AD4"));
        assertTrue("PPIX-isDataInstanceMeasure2",analyser.isDataInstanceMeasure("sid-25409B7D-BE71-4EF1-9473-BE9C30984AD4"));
        assertTrue("PPIX-isMeasuresData2",analyser.isMeasuresData("sid-25409B7D-BE71-4EF1-9473-BE9C30984AD4", "DCSelectionsid-25409B7D-BE71-4EF1-9473-BE9C30984AD4"));
        assertTrue("PPIX-isDataContentSelection2",analyser.isDataContentSelection("DCSelectionsid-25409B7D-BE71-4EF1-9473-BE9C30984AD4"));
        assertTrue("PPIX-",analyser.isData("DCSelectionsid-25409B7D-BE71-4EF1-9473-BE9C30984AD4", "sid-BF1BF36F-651C-4F36-806B-2A763E8B51A7"));

        //PPIY- CyclicTimeMeasure
        assertTrue("PPIY-",analyser.isPpi("sid-dd680ca1-0bcf-4f10-bf66-a3eff0462912"));
        assertTrue("PPIY-",analyser.isDefinition("sid-dd680ca1-0bcf-4f10-bf66-a3eff0462912", "sid-C88ED64E-29BF-47BE-8123-EC218303EA85"));
        assertTrue("PPIY-",analyser.isCyclicTimeIntanceMeasure("sid-C88ED64E-29BF-47BE-8123-EC218303EA85"));
        assertTrue("PPIY-",analyser.isFrom("sid-C88ED64E-29BF-47BE-8123-EC218303EA85", "startsid-DDF4FE82-F265-4834-8F12-B49B59BECEF8"));
        assertTrue("PPIY-",analyser.isActivityStart("startsid-DDF4FE82-F265-4834-8F12-B49B59BECEF8"));
        assertTrue("PPIY-",analyser.isAppliedTo("startsid-DDF4FE82-F265-4834-8F12-B49B59BECEF8", "sid-DDF4FE82-F265-4834-8F12-B49B59BECEF8"));
        assertTrue("PPIY-",analyser.isTo("sid-C88ED64E-29BF-47BE-8123-EC218303EA85", "endsid-DDF4FE82-F265-4834-8F12-B49B59BECEF8"));
        assertTrue("PPIY-",analyser.isActivityEnd("endsid-DDF4FE82-F265-4834-8F12-B49B59BECEF8"));
        assertTrue("PPIY-",analyser.isAppliedTo("endsid-DDF4FE82-F265-4834-8F12-B49B59BECEF8", "sid-DDF4FE82-F265-4834-8F12-B49B59BECEF8"));
	}

}
