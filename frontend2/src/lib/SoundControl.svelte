<script lang="ts">
    import { page } from "$app/stores";
    import { afterUpdate } from "svelte";
	import { event } from "./GameLog";
    import Fa from 'svelte-fa';
    import { faVolumeHigh, faVolumeXmark } from '@fortawesome/free-solid-svg-icons';
    let playCardAudio: HTMLMediaElement;
    let drawCardAudio: HTMLMediaElement;
    let lastIndex = -1;
    let enabled = true;

    function checkPlaySound() {
        const latestEvent = event($page.data.playerView.log);
        if (latestEvent && latestEvent.index > lastIndex) {
            lastIndex = latestEvent.index;
            if (!enabled) {
                return;
            }
            switch (latestEvent.actionName) {
                case "playCard":
                        playCardAudio.play();
                        break;
                        case "drawCard":
                            drawCardAudio.play();
                            break;
            }
        }
    }

    afterUpdate(checkPlaySound);
</script>

<audio id="playCard" bind:this={playCardAudio} src="/nuo/Card_play_sound_nuo.mp3" preload="auto"/>
<audio id="drawCard" bind:this={drawCardAudio} src="/nuo/Card_draw_sound_nuo.mp3" preload="auto"/>

<label class="label gap-1">
    <Fa icon={enabled ? faVolumeHigh : faVolumeXmark} class="w-6 justify-self-start" />
<input type="checkbox" bind:checked={enabled} class="toggle">
</label>