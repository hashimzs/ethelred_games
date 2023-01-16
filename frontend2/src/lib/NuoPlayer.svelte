<script lang="ts">
    import Fa from 'svelte-fa';
    import { faArrowRight, faStar, faTrophy } from '@fortawesome/free-solid-svg-icons';
    export let player:{name:string, self:boolean, cardCount:number, turn:boolean, winner:boolean};
    $: mini = {length:player?.cardCount || 0};
</script>

    <div class="icon">
    {#if player.turn}
    <Fa icon={faArrowRight} />
    {/if}
    {#if player.winner}
    <Fa icon={faTrophy} />
    {/if}
</div>
<div class="icon">
    {#if player.self}
    <Fa icon={faStar} />
    {/if}
</div>
<div>
    {player.name}
</div>
<div class="mini fl">
    {#each mini as minicard}
        <img src="/nuo/card/mini.png" alt="card">
    {/each}
</div>

<style>
    .mini {
        justify-content: flex-start;
        min-height: 37px;
    }
</style>