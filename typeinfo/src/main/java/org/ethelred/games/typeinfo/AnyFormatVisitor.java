package org.ethelred.games.typeinfo;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonAnyFormatVisitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnyFormatVisitor extends JsonAnyFormatVisitor.Base {
    private static final Logger LOGGER = LogManager.getLogger();
    public AnyFormatVisitor(JavaType type) {
    }
}
