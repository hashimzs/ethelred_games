<script lang="ts">
	import { goto } from "$app/navigation";
    import type { PageData } from "./$types";

    export let data: PageData;

    let shortCode : string;

    async function join() {
        const res = await data.fetch(`/api/join/${shortCode}`, {method: 'PUT'});
        const json = await res.json();
        const path = json.path;
        const clientPath = path.split('/').slice(2).join('/');
        console.log("Join: ", clientPath);
        goto(clientPath);
    }

    async function create(gameType : string) {
        const res = await data.fetch(`/api/${gameType}`, {method: 'POST'});
        const json = await res.json();
        const path = json.path;
        const clientPath = path.split('/').slice(2).join('/');
        console.log("Create: ", clientPath);
        goto(clientPath);
    }
</script> 

<form method="POST" on:submit|preventDefault={join}>
    <label for="shortCode">Join a game: enter code</label>
    <input id="shortCode" type="text" name="shortCode" minlength="3" bind:value={shortCode}>
    <input type="submit" value="Join">
</form>
    {#each data.gameTypes as gameType}
        <h2>{gameType}</h2>    
        <form method="POST" on:submit|preventDefault={() => create(gameType)}>
            <input name="gameType" type="hidden" value={gameType}>
            <label for="btn">Create a game</label>
            <input id="btn" type="submit" value="Create">
        </form>
    {/each}