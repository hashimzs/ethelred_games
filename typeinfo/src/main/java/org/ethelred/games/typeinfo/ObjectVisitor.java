package org.ethelred.games.typeinfo;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ObjectVisitor extends JsonObjectFormatVisitor.Base {
    private static final Logger LOGGER = LogManager.getLogger();
    private final TypeInfo typeInfo;
    private final MyType myType;

    public ObjectVisitor(TypeInfo typeInfo, MyType myType) {
        super(typeInfo.serializerProvider);
        this.typeInfo = typeInfo;
        this.myType = myType;
    }

    @Override
    public void property(BeanProperty prop) throws JsonMappingException {
        LOGGER.debug("property {}", prop.getName());
        visit(prop, false);
    }

    @Override
    public void optionalProperty(BeanProperty prop) throws JsonMappingException {
        LOGGER.debug("optional property {}", prop.getName());
        visit(prop, true);
    }

    private void visit(BeanProperty prop, boolean optional) {
        try {
            myType.pendingProperty(prop.getName(), optional);
            JsonSerializer<?> serializer = null;
            if (prop instanceof BeanPropertyWriter writer) {
                serializer = writer.getSerializer();
            }
            MyType propertyType;
            if (serializer == null) {
                propertyType = typeInfo.visitType(prop.getType(), prop);
            } else {
                propertyType = typeInfo.visitType(prop.getType(), serializer, prop);
            }
            myType.propertyType(propertyType.typeName);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        }
    }
}
