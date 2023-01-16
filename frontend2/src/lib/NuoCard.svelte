<script lang="ts">
	import { createEventDispatcher } from "svelte";

    export let card:string;
    export let highlight:boolean;
    export let wildColor:string | undefined = undefined;

    const dispatch = createEventDispatcher();
    function interact() {
        dispatch('playCard', card);
    }
    function getDisplay(c:string) {
        switch(c) {
            case 'b' : return 'Blue';
            case 'g' : return 'Green';
            case 'r' : return 'Red';
            case 'y' : return 'Yellow';
        }
    }
</script>
<div class="card" class:highlight on:click={interact} on:keypress={interact}>
    <img src={`/nuo/card/${card}.png`} alt={card}>
    {#if wildColor}
    <div class={`wildColor ${wildColor}`}>{getDisplay(wildColor)}</div>
    {/if}
</div>

<style>
    .card {
        position: relative;
    }
    img {
        border: thick solid rgba(0, 0, 0, 0);
        border-radius: 1rem;
    }
    .highlight img {
        border: thick dashed black;
        cursor: pointer;
    }
    .wildColor {
        text-align: center;
        padding: 0.5em;
        font-weight: bold;
        font-size: 1.2rem;
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        top: 80%;
    }
</style>