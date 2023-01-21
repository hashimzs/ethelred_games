package org.ethelred.games.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.ethelred.games.core.Action;
import org.ethelred.games.core.BooleanAction;
import org.ethelred.games.core.InvalidActionException;
import org.ethelred.games.core.StringAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public class ActionDeserializer extends StdDeserializer<Action>
{
    private static final long serialVersionUID = 1;
    public static final JsonPointer[] namePointers = getPointers("/name", "/action");

    @NotNull
    private static JsonPointer[] getPointers(String... paths)
    {
        return Stream.of(paths).map(JsonPointer::compile).toArray(JsonPointer[]::new);
    }

    public static final JsonPointer[] argumentPointers = getPointers("/argument", "/value");

    public ActionDeserializer(@Nullable Class<?> vc)
    {
        super(vc);
    }

    public ActionDeserializer()
    {
        this(Action.class);
    }

    @Override
    public Action deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        var rootNode = jsonParser.getCodec().readTree(jsonParser);
        var nameNode = first(rootNode, namePointers).orElseThrow(() -> new InvalidActionException("Missing name node"));
        var name = ((TextNode) nameNode).asText();
        var argumentNode = first(rootNode, argumentPointers);
        if (argumentNode.isEmpty() || argumentNode.get() instanceof NullNode)
        {
            return new StringAction(name, "[NULL]");
        }
        if (argumentNode.get() instanceof BooleanNode)
        {
            return new BooleanAction(name, ((BooleanNode) argumentNode.get()).asBoolean());
        }
        if (argumentNode.get() instanceof TextNode)
        {
            return new StringAction(name, ((TextNode) argumentNode.get()).asText());
        }

        throw new InvalidActionException("Deserialization failed");
    }

    private Optional<TreeNode> first(TreeNode node, JsonPointer[] pointers)
    {
        return Stream.of(pointers)
                .map(node::at)
                .filter(TreeNode::isValueNode)
                .findFirst();
    }
}
