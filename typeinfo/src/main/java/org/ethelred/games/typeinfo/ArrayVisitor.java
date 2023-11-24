package org.ethelred.games.typeinfo;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArrayVisitor extends JsonArrayFormatVisitor.Base {
    private static final Logger LOGGER = LogManager.getLogger();
    private final TypeInfo typeInfo;
    private final MyType myType;

    public ArrayVisitor(TypeInfo typeInfo, MyType myType) {
        super(typeInfo.serializerProvider);
        this.typeInfo = typeInfo;
        this.myType = myType;
    }

    @Override
    public void itemsFormat(JsonFormatTypes format) throws JsonMappingException {
        myType.propertyType(format.value() + "[]");
    }

    @Override
    public void itemsFormat(JsonFormatVisitable handler, JavaType elementType) throws JsonMappingException {
        LOGGER.debug("itemsFormat({}, {})", handler, elementType);
        myType.propertyType(elementType.toCanonical() + "[]");
        typeInfo.visitType(elementType, null);
    }
}
