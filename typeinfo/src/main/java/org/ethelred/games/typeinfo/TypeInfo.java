package org.ethelred.games.typeinfo;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.ethelred.games.server.bind.EngineModule;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TypeInfo {
    private static final TypeInfo instance = new TypeInfo();

    private final ObjectMapper objectMapper = EngineModule.objectMapper(); // TODO use injection?
    final SerializerProvider serializerProvider = objectMapper.getSerializerProviderInstance();
    private final Set<MyType> types = new HashSet<>();

    public static void visitClass(Class<?> klass) throws JsonMappingException {
        instance.visitType(klass);
        instance.types.forEach(
                System.out::println
        );
    }

    public void visitType(Class<?> klass) throws JsonMappingException {
        visitType(objectMapper.constructType(klass), null);

        var aliases = types.stream()
                .filter(MyType::isAlias)
                .collect(Collectors.toMap(MyType::getTypeName, MyType::getTypeAlias));
        for (var type: types) {
            if (type.isObject()) {
                type.replaceAliases(aliases);
            }
        }
    }


     MyType visitType(JavaType javaType, BeanProperty beanProperty) throws JsonMappingException {

        var serializer = serializerProvider.findTypedValueSerializer(javaType, false, beanProperty);
        return visitType(javaType, serializer, beanProperty);
    }

     MyType visitType(JavaType javaType, JsonSerializer<?> serializer, BeanProperty prop) throws JsonMappingException {
         var myType = new MyType(javaType);
         types.add(myType);
         serializer.acceptJsonFormatVisitor(new VisitorWrapper(this, myType), javaType);
         return myType;
     }
}
