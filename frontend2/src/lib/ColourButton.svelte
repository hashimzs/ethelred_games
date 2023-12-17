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
<button class={`${colour} btn text-lg p-2`} on:click={handle}>{display}</button>
