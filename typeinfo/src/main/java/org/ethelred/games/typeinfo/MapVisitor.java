package org.ethelred.games.typeinfo;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonMapFormatVisitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MapVisitor extends JsonMapFormatVisitor.Base {
    private static final Logger LOGGER = LogManager.getLogger();
    private final TypeInfo typeInfo;
    private final MyType myType;

    public MapVisitor(TypeInfo typeInfo, MyType myType) {
        super(typeInfo.serializerProvider);
        this.typeInfo = typeInfo;
        this.myType = myType;
    }

    @Override
    public void keyFormat(JsonFormatVisitable handler, JavaType keyType) throws JsonMappingException {
        LOGGER.debug("keyFormat({}, {})", handler, keyType);
        super.keyFormat(handler, keyType);
    }

    @Override
    public void valueFormat(JsonFormatVisitable handler, JavaType valueType) throws JsonMappingException {
        LOGGER.debug("valueFormat({}, {})", handler, valueType);
        super.valueFormat(handler, valueType);
    }
}
