<script lang="ts">
	import { getDisplay, type Colour } from "./NuoColour";


    type Log = {playerName:string, actionName:string, actionValue:string}
    export let log:Log[] = [];

    $: real = log ? log.reverse().filter(row => row.actionName != 'playDrawn') : [];

    interface ActionMapping {
        text: string,
        value: (v:any) => string
    }
    interface LogMapping {
        [index:string]: ActionMapping
    }

    const mapping: LogMapping = {
        playCard: {
            text: 'played card',
            value: (v:string) => `<img src="/nuo/card/${v}.png" height=37>`
        },
        playerReady: {
            text: 'is ready to play',
            value: () => ''
        },
        chooseColor: {
            text: 'chose colour',
            value: (v:Colour) => `<span class="${v}">${getDisplay(v)}</span>`
        },
        drawCard: {
            text: 'drew a card',
            value: () => ''
        }
    }

    function translateAction(actionName:string) {
        return mapping[actionName]?.text || actionName;
    }

    function translateValue(actionName: string, actionValue: string) {
        let m = mapping[actionName];
        if (m) {
            return m.value(actionValue);
        }
        return actionValue;
    }
</script>

<div class="log">
{#each real as row}
    <div>
        {row.playerName} {translateAction(row.actionName)} {@html translateValue(row.actionName, row.actionValue)}
    </div>
{/each}

</div>

<style>
    .log {
        border-top: solid thin black;
        margin-top: 0.5rem;
        padding-top: 0.5rem;
    }
</style>