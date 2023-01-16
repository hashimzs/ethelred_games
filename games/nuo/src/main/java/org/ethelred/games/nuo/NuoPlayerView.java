package org.ethelred.games.nuo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ethelred.games.core.BasePlayerView;
import org.ethelred.games.core.Game;
import org.ethelred.games.core.Player;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NuoPlayerView extends BasePlayerView<NuoPlayer> {
    @JsonProperty
    boolean reversedDirection;
    @JsonProperty
    Card current;

    @JsonProperty
    Color wildColor;

    @JsonProperty
    Card drewCard;

    public NuoPlayerView(Player self, NuoGame game) {
        super(self, game);
        if (game.status() == Game.Status.PRESTART) {
            return;
        }
        reversedDirection = game.reversedDirection;
        current = game.current();
        wildColor = game.wildColor();
        if (game.playState == NuoGame.PlayState.PLAY_DRAWN) {
            drewCard = game.gamePlayer(self).drewCard;
        }
    }
}
