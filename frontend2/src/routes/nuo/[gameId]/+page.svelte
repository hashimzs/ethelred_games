<script lang="ts">
	import { goto, invalidateAll } from '$app/navigation';
	import PlayerName from '$lib/PlayerName.svelte';
	import NuoCard from '$lib/NuoCard.svelte';
	import { afterUpdate } from 'svelte';
	import type { PageData } from './$types';
	import CardBack from '$lib/CardBack.svelte';
	import PregamePlayer from '$lib/PregamePlayer.svelte';
	import NuoPlayer from '$lib/NuoPlayer.svelte';
	import { faCopy, faArrowUp, faArrowDown } from '@fortawesome/free-solid-svg-icons';
	import Fa from 'svelte-fa'
	import ColourButton from '$lib/ColourButton.svelte';
	import GameLog from '$lib/GameLog.svelte';

	export let data: PageData;

	let copied = '';

	let updateTimeout: string | number | NodeJS.Timeout | undefined | null

	const controller = new AbortController();
	const signal = controller.signal;

	function scheduleUpdate() {
		cancelUpdate();
		updateTimeout = setTimeout(() => {
			invalidateAll();
		}, 1000);
	}

	function cancelUpdate() {
		if (updateTimeout) {
			clearTimeout(updateTimeout);
			updateTimeout = null;
		}
		// controller.abort();
	}

	afterUpdate(scheduleUpdate);

	$: pregame = data.playerView.status === "PRESTART";
	$: self = data.playerView.self;
	$: actions = data.playerView.availableActions;
	$: showChooseColor = actions && actions.some(a => a.name == 'chooseColor');
	$: showPlayDrawn = actions && actions.some(a => a.name == 'playDrawn');
	$: showPlayAgain = actions && actions.some(a => a.name == 'playAgain');
	$: showAddBot = actions && actions.some(a => a.name == 'addBot');
	$: winner = data.playerView.players.find(p => p.winner)?.name;

	$: hasAction = function(name: string, value?: string | boolean) {
		return actions && actions.some((a: { name: string; possibleArguments: (string | boolean)[]; }) => a.name == name && (!value || a.possibleArguments.includes(value)))
	}

	async function action(name: string, value?: string | boolean) {
		cancelUpdate();
		const action = { name, value };
		const res = await data.fetch(data.path, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(action),
			signal
		});
		const json = await res.json();
		json.fetch = data.fetch;
		data = json;
		if (name == 'playAgain' && !value) {
			goto('/')
		}
	}

	function handleAction(event: CustomEvent) {
		console.log("handleAction(%s, %s)", event.type, event.detail);
		if (hasAction(event.type, event.detail)) {
			action(event.type, event.detail);
		}
	}

	function copyCode() {
		navigator.clipboard.writeText(data.playerView.shortCode)
		.then(() => copied = 'Copied to clipboard!')
		.catch((err) => alert(`Nope. ${err}`));
	}
</script>
<!-- <tt>{JSON.stringify(data)}</tt> -->
{#if pregame}
<PlayerName name={self.name} on:open={cancelUpdate} on:close={scheduleUpdate}/>
	<p>Invite players to join with code <strong>{data.playerView.shortCode}</strong> <button class="btn" on:click={copyCode}><Fa icon={faCopy}/> Copy</button> {copied}</p>
{/if}
<div class="relative flex justify-center">
<div class="flex-[0_0_30%] max-w-[30%]">
	<h3>Players</h3>
	<ul class="grid grid-cols-[1.4rem_1.4rem_8rem_1fr] gap-0.5">
		{#each data.playerView.players as player}
				{#if pregame}
					<PregamePlayer {player} showButton={player.self && hasAction('playerReady')} on:click|once={() => action('playerReady')}/>
				{:else}
					<NuoPlayer {player} />
				{/if}
		{/each}
	</ul>
	{#if showAddBot}
	<div><button class="btn" on:click={() => action('addBot')}>Add Bot</button></div>
	{/if}
	{#if data.playerView.players.length > 2 && !pregame}
	<h3>
		Direction <Fa icon={data.playerView.reversedDirection ? faArrowUp : faArrowDown} />
	</h3>
	{/if}
	<GameLog log={data.playerView.log}/> 
</div>

{#if self.hand}
<div class="flex relative flex-col justify-start ml-2 flex-1" class:turn="{hasAction('drawCard')}">
<div class="flex justify-center gap-1">
	<fieldset>
		<legend>Match</legend>
	<NuoCard card={data.playerView.current} highlight={false} wildColor={data.playerView.wildColor}/>
</fieldset>
<fieldset>
	<legend class="ml-auto">Draw</legend>
	<CardBack on:drawCard={handleAction} highlight={hasAction('drawCard')}/>
</fieldset>
</div>

<fieldset class="pt-2 flex-wrap flex justify-center">
	<legend>Hand</legend>
	{#each self.hand as card}
		<NuoCard {card} on:playCard={handleAction} highlight={hasAction('playCard', card)}/>
	{/each}
</fieldset>
{#if showPlayDrawn}
<div class="overlay turn">
	Drew card <NuoCard card={data.playerView.drewCard} highlight={true} on:playCard={evt => action('playDrawn', true)}/>
	Play it now?
	<button class="btn" on:click={() => action('playDrawn', true)}>Yes</button>
	<button class="btn" on:click={() => action('playDrawn', false)}>No</button>
</div>
{/if}
{#if showChooseColor}
<div class="overlay chooseColor">
	<span>Choose colour:</span>
	<ColourButton colour='r' on:chooseColor={handleAction} />
	<ColourButton colour='g' on:chooseColor={handleAction} />
	<ColourButton colour='b' on:chooseColor={handleAction} />
	<ColourButton colour='y' on:chooseColor={handleAction} />
</div>
{/if}
</div>
{:else}
<div class="flex relative flex-col justify-start ml-2 flex-1"></div>
{/if}

{#if showPlayAgain}
<div class="overlay">
	<div>{winner} is the winner!</div>
	<div>Play again?</div>
	<button class="btn" on:click={() => action('playAgain', true)}>Yes</button>
	<button class="btn" on:click={() => action('playAgain', false)}>No</button>
</div>
{/if}

</div>

<footer><em>Players can use code <strong>{data.playerView.shortCode}</strong> to rejoin.</em></footer>
<style>
h3 {
	text-align: center;
	margin: 0.4em;
}
ul {
	padding: 0;
}
.chooseColor {
	display: grid;
	grid-template-columns: repeat(4, 1fr);
	gap: 2px;
}
.chooseColor span {
	grid-column: 1 / 5;
}
</style>