package org.ethelred.games.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.ethelred.games.util.Multiset;

import java.io.IOException;

public class MyMultisetSerializer extends StdSerializer<Multiset> {
    private static final long serialVersionUID = 1;
    public MyMultisetSerializer() {
        super(Multiset.class);
    }

    @Override
    public void serialize(Multiset value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartArray();
        for (Object o : value) {
            gen.writeObject(o);
        }
        gen.writeEndArray();
    }

    @Override
    public boolean isEmpty(SerializerProvider provider, Multiset value) {
        return value.isEmpty();
    }
}
