<script lang="ts">
	import { createEventDispatcher, tick } from "svelte";


    let show = false;
    export let name:string;

    const dispatch = createEventDispatcher();

    let textRef: HTMLInputElement;

    function updateName() {
        fetch('/api/player/name', {
            method: 'POST',
			headers: {
				'Content-Type': 'text/plain'
			},
            body: name
        })
        .then(close, close);
    }

    async function open() {
        show = true;
        dispatch('open');
        await tick();
        textRef.focus();
        textRef.select();
    }

    function close() {
        show = false;
        dispatch('close');
    }

    function handleEscape(event:KeyboardEvent) {
        if (event.key == 'Escape') {
            close();
        }
    }
</script>
<svelte:window on:keydown={handleEscape} on:click={close}/>
<div class="rel">
{#if show}
<div class="modal">
    <!-- svelte-ignore a11y-autofocus -->
    <input type="text" bind:this={textRef} bind:value={name} on:change={updateName} on:click|stopPropagation>
    <button on:click|stopPropagation={updateName}>Update Name</button>
</div>
{/if}
<button on:click|stopPropagation={open} disabled={show}>Change Name</button>
</div>

<style>
    .rel {
        position: relative;
    }
    .modal {
        position: absolute;
        z-index: 10;
    }
</style>