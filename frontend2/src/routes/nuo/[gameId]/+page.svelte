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
	<p>Invite players to join with code <strong>{data.playerView.shortCode}</strong><button on:click={copyCode}><Fa icon={faCopy}/> Copy</button> {copied}</p>
{/if}
<div class="container fl">
<div class="left-pane">
	<h3>Players</h3>
	<ul class="players">
		{#each data.playerView.players as player}
				{#if pregame}
					<PregamePlayer {player} showButton={player.self && hasAction('playerReady')} on:click|once={() => action('playerReady')}/>
				{:else}
					<NuoPlayer {player} />
				{/if}
		{/each}
	</ul>
	{#if showAddBot}
	<div><button on:click={() => action('addBot')}>Add Bot</button></div>
	{/if}
	{#if data.playerView.players.length > 2 && !pregame}
	<div>
		Direction <Fa icon={data.playerView.reversedDirection ? faArrowUp : faArrowDown} />
	</div>
	{/if}
	<GameLog log={data.playerView.log}/> 
</div>

{#if self.hand}
<div class="right-pane fl col" class:turn="{hasAction('drawCard')}">
<div class="table fl">
	<NuoCard card={data.playerView.current} highlight={false} wildColor={data.playerView.wildColor}/>
	<CardBack on:drawCard={handleAction} highlight={hasAction('drawCard')}/>
</div>

<div class="hand fl">
	{#each self.hand as card}
		<NuoCard {card} on:playCard={handleAction} highlight={hasAction('playCard', card)}/>
	{/each}
</div>
{#if showPlayDrawn}
<div class="overlay turn">
	Drew card <NuoCard card={data.playerView.drewCard} highlight={true} on:playCard={evt => action('playDrawn', true)}/>
	Play it now?
	<button on:click={() => action('playDrawn', true)}>Yes</button>
	<button on:click={() => action('playDrawn', false)}>No</button>
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
{/if}

{#if showPlayAgain}
<div class="overlay">
	<div>{winner} is the winner!</div>
	<div>Play again?</div>
	<button on:click={() => action('playAgain', true)}>Yes</button>
	<button on:click={() => action('playAgain', false)}>No</button>
</div>
{/if}

</div>

<strong>{data.playerView.shortCode}</strong>
<style>
.hand {
	flex-wrap: wrap;
	padding-top: 0.5rem;
}
.left-pane {
	flex: 0 0 30%;
	max-width: 30%;
}
.right-pane, .container {
	position: relative;
}
.right-pane {
	margin-left: 0.5rem;
	flex: 1;
}
.overlay {
	position: absolute;
	top: 50%;
	left: 50%;
	transform:translate(-50%, -50%);
    z-index: 10;
	background-color: white;
	box-shadow: 0px 0px 20px 20px rgba(0,0,0, 0.3);
	padding: 2rem;
	margin: 2rem;
}
h3 {
	text-align: center;
	margin: 0.4em;
}
ul {
	padding: 0;
}
.players {
	display: grid;
	grid-template-columns: 1.4rem 1.4rem 8rem 1fr;
	gap: 2px;
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