<script lang="ts">
    type Log = {playerName:string, actionName:string, actionValue:string}
    export let log:Log[] = [];

    $: real = log ? log.reverse().filter(row => row.actionName != 'playDrawn') : [];

    const mapping = {
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
            value: (v) => 'TODO'
        },
        drawCard: {
            text: 'drew a card',
            value: () => ''
        }
    }

    function translateAction(actionName:string) {
        return mapping[actionName]?.text || actionName;
    }

    function translateValue(actionName, actionValue) {
        let m = mapping[actionName];
        if (m) {
            return m.value(actionValue);
        }
        return actionValue;
    }
</script>

<div>
{#each real as row}
    <div>
        {row.playerName} {translateAction(row.actionName)} {@html translateValue(row.actionName, row.actionValue)}
    </div>
{/each}

</div>

<style>
</style>