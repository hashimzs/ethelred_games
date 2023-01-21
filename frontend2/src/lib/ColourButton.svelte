<script lang="ts">
    import {createEventDispatcher} from 'svelte';
	import { getDisplay, type Colour } from './NuoColour';

    const dispatch = createEventDispatcher();

    export let colour: Colour

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