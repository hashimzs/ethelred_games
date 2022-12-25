package org.ethelred.games.server.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.ethelred.games.core.Action;
import org.ethelred.games.core.BooleanAction;
import org.ethelred.games.core.InvalidActionException;
import org.ethelred.games.core.StringAction;
import org.ethelred.games.core.TodoPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public class ActionDeserializer extends StdDeserializer<Action>
{
    public static final JsonPointer[] playerIdPointers = getPointers("/playerId", "/player/id");

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
        this(null);
    }

    @Override
    public Action deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException
    {
        var rootNode = jsonParser.getCodec().readTree(jsonParser);
        var playerIdNode = _first(rootNode, playerIdPointers).orElseThrow(InvalidActionException::new);
        var playerId = ((NumericNode) playerIdNode).longValue();
        var player = new TodoPlayer(playerId);
        var nameNode = _first(rootNode, namePointers).orElseThrow(InvalidActionException::new);
        var name = ((TextNode) nameNode).asText();
        var argumentNode = _first(rootNode, argumentPointers);
        if (argumentNode.isEmpty())
        {
            return new StringAction(player, name, "[NULL]");
        }
        if (argumentNode.get() instanceof BooleanNode)
        {
            return new BooleanAction(player, name, ((BooleanNode) argumentNode.get()).asBoolean());
        }
        if (argumentNode.get() instanceof TextNode)
        {
            return new StringAction(player, name, ((TextNode) argumentNode.get()).asText());
        }

        throw new InvalidActionException();
    }

    private Optional<TreeNode> _first(TreeNode node, JsonPointer[] pointers)
    {
        return Stream.of(pointers)
                .map(node::at)
                .filter(TreeNode::isValueNode)
                .findFirst();
    }
}
