<script lang="ts">
    import Fa from 'svelte-fa';
    import { faCheck } from '@fortawesome/free-solid-svg-icons';
    export let player:{name:string, ready:boolean};
    export let showButton:boolean;
</script>
    <div class="name">{player.name}</div>
    <div class="ready">{#if showButton}
    <button class="btn" on:click>Ready</button>
    {:else if player.ready}
    <Fa icon={faCheck} />
    {/if}
</div>

<style>
    .name {
        grid-column: 1/4;
    }
</style>