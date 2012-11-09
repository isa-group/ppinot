package isabpmcenter.ppinot.jsontoxml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.json.JSONObject;
import org.oryxeditor.server.diagram.basic.BasicDiagramBuilder;
import org.oryxeditor.server.diagram.generic.GenericDiagram;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import de.hpi.bpmn2_0.ExportValidationEventCollector;
import de.hpi.bpmn2_0.factory.AbstractBpmnFactory;
import de.hpi.bpmn2_0.model.Definitions;
import de.hpi.bpmn2_0.transformation.BPMNPrefixMapper;
import de.hpi.bpmn2_0.transformation.Diagram2BpmnConverter;

public class BPMNSerializationTest {
	
	public static void prueba () {
		
	}

		//ppinot indica si esta funcion ha sido llamada desde el plugin para ppinot para queries
	//File es el archivo donde quiero que se almace el xml
	public static StringWriter toBpmn2_0(File json, boolean ppinot, File fxml, String schemaFilePath) throws Exception {

System.out.println("----- toBpmn2_0 - 1");
		JSONObject result = new JSONObject();
		StringWriter writer = new StringWriter();

		BufferedReader br = new BufferedReader(new FileReader(json));
		String bpmnJson = "";
		String line;
		while ((line = br.readLine()) != null) {
			bpmnJson += line;
		}
System.out.println("----- toBpmn2_0 - 2");
		
		GenericDiagram diagram = BasicDiagramBuilder.parseJson(bpmnJson);
System.out.println("----- toBpmn2_0 - 3");
		List<Class<? extends AbstractBpmnFactory>> factoryClasses = AbstractBpmnFactory.getFactoryClasses();
System.out.println("----- toBpmn2_0 - 4");
		
		Diagram2BpmnConverter converter = new Diagram2BpmnConverter(diagram, factoryClasses);
System.out.println("----- toBpmn2_0 - 5");
		
			// EDE: A partir del json obtiene objetos (de las clases definidas en factory) con los que se obtiene el xml  
		Definitions def = converter.getDefinitionsFromDiagram();

		JAXBContext context = JAXBContext.newInstance(Definitions.class);
		Marshaller m = context.createMarshaller();
		
		// Schema validation /
		SchemaFactory sf = SchemaFactory
				.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
		
		Schema schema = sf.newSchema(new File(schemaFilePath));
		m.setSchema(schema);
		
		ExportValidationEventCollector vec = new ExportValidationEventCollector();
		m.setEventHandler(vec);
		
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		NamespacePrefixMapper nsp = new BPMNPrefixMapper();
		m.setProperty("com.sun.xml.bind.namespacePrefixMapper", nsp);
System.out.println("----- toBpmn2_0 - 6");
		
		if(ppinot){ 
			m.marshal(def, fxml);
		}else{
			m.marshal(def, writer);
			result.put("xml", writer.toString());
	        // Append XML Schema validation results 
	        if (vec.hasEvents()) {
	            ValidationEvent[] events = vec.getEvents();
	            StringBuilder builder = new StringBuilder();
	            builder.append("Validation Errors: <br /><br />");

	            for (ValidationEvent event : Arrays.asList(events)) {

	                builder.append("Line: ");
	                builder.append(event.getLocator().getLineNumber());
	                builder.append(" Column: ");
	                builder.append(event.getLocator().getColumnNumber());

	                builder.append("<br />Error: ");
	                builder.append(event.getMessage());
	                builder.append("<br /><br />");
	            }
	            result.put("validationEvents", builder.toString());
	        }
		}
System.out.println("----- toBpmn2_0 - 7");

		// Prepare output 
        writer = new StringWriter();
        writer.write(result.toString());
System.out.println("----- toBpmn2_0 - 8");

        return writer;
	}
}