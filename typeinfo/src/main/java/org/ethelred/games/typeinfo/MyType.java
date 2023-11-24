package org.ethelred.games.typeinfo;

import com.fasterxml.jackson.databind.JavaType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MyType {
    public void replaceAliases(Map<String, String> aliases) {
        for (var entry :
                properties.entrySet()) {
            if (aliases.containsKey(entry.getValue())) {
                entry.setValue(aliases.get(entry.getValue()));
            }
        }
    }

    record PropertyEntry(String name, boolean optional){}

    public String getTypeName() {
        return typeName;
    }

    public String getTypeAlias() {
        return typeAlias;
    }

    final String typeName;
    private final Map<PropertyEntry, String> properties = new HashMap<>();

    private PropertyEntry pending;
    private String typeAlias;

    public MyType(JavaType javaType) {
        this.typeName = javaType.toCanonical();
    }

    boolean isObject() {
        return !properties.isEmpty();
    }

    boolean isAlias() {
        return typeAlias != null;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyType myType = (MyType) o;
        return typeName.equals(myType.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName);
    }

    @Override
    public String toString() {
        var builder = new StringBuilder(typeName);
        if (!properties.isEmpty()) {
            builder.append('{');
            properties.forEach((k, v) -> {
                builder.append(k.name);
                if (k.optional) {
                    builder.append('?');
                }
                builder.append(": ");
                builder.append(v);
                builder.append("; ");
            });
            builder.append('}');
        }
        if (typeAlias != null) {
            builder.append(" = ").append(typeAlias);
        }
        return builder.toString();
    }

    public void pendingProperty(String name, boolean optional) {
        if (pending == null) {
            pending = new PropertyEntry(name, optional);
        } else {
            throw new IllegalStateException("Already has pending property");
        }
    }

    public void propertyType(String typeName) {
        if (pending == null) {
            typeAlias = typeName;
        } else {
            properties.put(pending, typeName);
            pending = null;
        }
    }
}
