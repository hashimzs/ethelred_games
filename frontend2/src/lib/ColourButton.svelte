<script lang="ts">
    import {createEventDispatcher} from 'svelte';

    const dispatch = createEventDispatcher();

    type Colour = 'r' | 'g' | 'b' | 'y'
    export let colour: Colour

    function getDisplay(c:Colour) {
        switch(c) {
            case 'b' : return 'Blue';
            case 'g' : return 'Green';
            case 'r' : return 'Red';
            case 'y' : return 'Yellow';
        }
    }
    $: display = getDisplay(colour);

    function handle() {
        dispatch('chooseColor', colour);
    }
</script>
<button class={colour} on:click={handle}>{display}</button>

<style>
    button {
        font-size: 1.2rem;
        padding: 0.5rem;
    }
</style>