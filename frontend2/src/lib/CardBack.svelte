<script lang="ts">
	import { createEventDispatcher } from "svelte";
    export let highlight:boolean;

    const dispatch = createEventDispatcher();
    function interact() {
        dispatch('drawCard');
    }
</script>
<div class="nuocard card-back" class:highlight on:click={interact} on:keypress={interact}>
    <img src="/nuo/card/back.png" alt="Draw">
</div>
