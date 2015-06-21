package au.gov.ga.geodesy.interfaces.rest;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import au.gov.ga.geodesy.domain.model.Equipment;
import au.gov.ga.geodesy.domain.model.EquipmentConfiguration;
import au.gov.ga.geodesy.domain.model.EquipmentRepository;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.impl.BeanAsArraySerializer;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;

@Configuration
public class RestConfig extends RepositoryRestMvcConfiguration {

   @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(IgsSiteLog.class);
    }

    @SuppressWarnings("serial")
    @Override
    protected void configureJacksonObjectMapper(ObjectMapper mapper) {
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd")); // ISO 8601
 
        mapper.registerModule(new SimpleModule() {
            public void setupModule(SetupContext context) {
                super.setupModule(context);
                context.addBeanSerializerModifier(new SerializerModifier());
            }
        });
    }
 
    public static class SerializerModifier extends BeanSerializerModifier {
        public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc,
                JsonSerializer<?> ser) {
 
            if (EquipmentConfiguration.class.isAssignableFrom(beanDesc.getBeanClass())) {
                return new CustomSerializer((BeanSerializerBase) ser);
            } else {
                return ser;
            }
        }
    }

    @SuppressWarnings("serial")
    @Configurable
    public static class CustomSerializer extends BeanSerializerBase {

        @Autowired
        private EquipmentRepository equipment;
 
        public CustomSerializer(BeanSerializerBase source) {
            super(source);
        }
 
        public CustomSerializer(BeanSerializerBase source, Class<?> clazz) {
            super(source);
        }

        public CustomSerializer(CustomSerializer source, ObjectIdWriter objectIdWriter) {
            super(source, objectIdWriter);
        }
 
        public CustomSerializer(CustomSerializer source, ObjectIdWriter objectIdWriter, Object filterId) {
            super(source, objectIdWriter, filterId);
        }
 
        public CustomSerializer(CustomSerializer source, String[] toIgnore) {
            super(source, toIgnore);
        }
 
        public BeanSerializerBase withObjectIdWriter(ObjectIdWriter objectIdWriter) {
            return new CustomSerializer(this, objectIdWriter);
        }
 
        public BeanSerializerBase withIgnorals(String[] toIgnore) {
            return new CustomSerializer(this, toIgnore);
        }
 
        public BeanSerializerBase withFilterId(Object id) {
            return new CustomSerializer(this, _objectIdWriter, id);
        }
 
        public BeanSerializerBase asArraySerializer() {
            if ((_objectIdWriter == null)
                    && (_anyGetterWriter == null)
                    && (_propertyFilterId == null)) {
                return new BeanAsArraySerializer(this);
            }
            return this;
        }
 
        public void serialize(Object bean, JsonGenerator jgen, SerializerProvider provider) throws IOException,
                JsonGenerationException {
            jgen.writeStartObject();

            if (bean instanceof EquipmentConfiguration) {
                Integer equipmentId = ((EquipmentConfiguration) bean).getEquipmentId();
                Equipment e = equipment.findOne(equipmentId);
            jgen.writeObjectField("equipment", e);
        }
            serializeFields(bean, jgen, provider);
            jgen.writeEndObject();
        }
    }
}
