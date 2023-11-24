package org.ethelred.games.typeinfo;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonAnyFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonBooleanFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonMapFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VisitorWrapper extends JsonFormatVisitorWrapper.Base {
    private static final Logger LOGGER = LogManager.getLogger();
    private final TypeInfo typeInfo;
    private final MyType myType;

    public VisitorWrapper(TypeInfo typeInfo, MyType myType) {
        super(typeInfo.serializerProvider);
        this.typeInfo = typeInfo;
        this.myType = myType;
    }

    @Override
    public JsonAnyFormatVisitor expectAnyFormat(JavaType type) throws JsonMappingException {
        LOGGER.debug("any format {}", type);
        return new AnyFormatVisitor(type);
    }

    @Override
    public JsonObjectFormatVisitor expectObjectFormat(JavaType type) throws JsonMappingException {
        LOGGER.debug("object format {}", type);
        return new ObjectVisitor(typeInfo, myType);
    }

    @Override
    public JsonArrayFormatVisitor expectArrayFormat(JavaType type) throws JsonMappingException {
        LOGGER.debug("array format {}", type);
        return new ArrayVisitor(typeInfo, myType);
    }

    @Override
    public JsonMapFormatVisitor expectMapFormat(JavaType type) throws JsonMappingException {
        LOGGER.debug("map format {}", type);
        return new MapVisitor(typeInfo, myType);
    }

    @Override
    public JsonStringFormatVisitor expectStringFormat(JavaType type) throws JsonMappingException {
        LOGGER.debug("string format {}", type);
        myType.propertyType("string");
        return super.expectStringFormat(type);
    }

    @Override
    public JsonBooleanFormatVisitor expectBooleanFormat(JavaType type) throws JsonMappingException {
        LOGGER.debug("boolean format {}", type);
        myType.propertyType("boolean");
        return super.expectBooleanFormat(type);
    }

    @Override
    public JsonNumberFormatVisitor expectNumberFormat(JavaType type) throws JsonMappingException {
        LOGGER.debug("number format {}", type);
        myType.propertyType("number");
        return super.expectNumberFormat(type);
    }
}
