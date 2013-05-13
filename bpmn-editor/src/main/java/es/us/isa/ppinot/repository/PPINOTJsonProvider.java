package es.us.isa.ppinot.repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.*;
import org.codehaus.jackson.map.deser.BeanDeserializerBuilder;
import org.codehaus.jackson.map.deser.BeanDeserializerFactory;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.ser.BeanPropertyWriter;
import org.codehaus.jackson.map.ser.BeanSerializerFactory;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * User: resinas
 * Date: 09/04/13
 * Time: 10:41
 */
 @Provider
 @Produces(MediaType.APPLICATION_JSON)
 public class PPINOTJsonProvider extends JacksonJsonProvider {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static Log log = LogFactory.getLog(PPINOTJsonProvider.class);
    private static String TYPE = "type";

    @Override
    public void writeTo(Object value, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String,Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        log.info("In custom JSON provider");
        //get the Object Mapper
        ObjectMapper mapper = locateMapper(type, mediaType);
        // Suppress null properties in JSON output
//        mapper.getSerializationConfig().setSerializationInclusion(org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL);
        // Set human readable date format
//        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
//        mapper.getSerializationConfig().setDateFormat(sdf);
//        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS);
//        mapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializerFactory(new CustomBeanSerializerFactory());
        super.writeTo(value, type, genericType, annotations, mediaType, httpHeaders, entityStream);
    }

    private class CustomBeanSerializerFactory extends BeanSerializerFactory {
        @Override
        public List<BeanPropertyWriter> findBeanProperties(SerializationConfig config, BasicBeanDescription beanDescription) throws JsonMappingException {
            List<BeanPropertyWriter> props = super.findBeanProperties(config, beanDescription);
            BeanPropertyWriter bpw = null;
            try {
                Class cc = beanDescription.getType().getRawClass();
                Method m = cc.getMethod("getClass", null);
                bpw = new BeanPropertyWriter(null, null, TYPE, null, null, null, null, m, null,true, null);
            } catch (SecurityException e) {
            } catch (NoSuchMethodException e) {
            }
            props.add(bpw);

            return props;        }
    }

    private class TypeProblemHandler extends DeserializationProblemHandler {
        @Override
        public boolean handleUnknownProperty(DeserializationContext ctxt, JsonDeserializer<?> deserializer, Object beanOrClass, String propertyName) throws IOException, JsonProcessingException {
            if (TYPE.equals(propertyName))
                return true;
            else
            return super.handleUnknownProperty(ctxt, deserializer, beanOrClass, propertyName);
        }
    }

//    private class CustomBeanDeserializerFactory extends BeanDeserializerFactory {
//        @Override
//        protected List<BeanPropertyDefinition> filterBeanProps(DeserializationContext ctxt,
//                                                               BeanDescription beanDesc,
//                                                               BeanDeserializerBuilder builder,
//                                                               List<BeanPropertyDefinition> propDefsIn,
//                                                               Set<String> ignored)
//                throws JsonMappingException   {
//
//        }
//    }
}