<script lang="ts">
	import { createEventDispatcher } from "svelte";
    export let highlight:boolean;

    const dispatch = createEventDispatcher();
    function interact() {
        dispatch('drawCard');
    }
</script>
<div class="card card-back" class:highlight on:click={interact} on:keypress={interact}>
    <img src="/nuo/card/back.png" alt="Draw">
</div>

<style>
    img {
        border: thick solid rgba(0, 0, 0, 0);
        border-radius: 1rem;
    }
    .highlight img {
        border: thick dashed black;
        cursor: pointer;
    }
</style>
