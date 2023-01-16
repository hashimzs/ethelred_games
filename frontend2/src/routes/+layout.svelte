<script lang="ts">
    import Fa from 'svelte-fa';
    import { faHome } from '@fortawesome/free-solid-svg-icons';
    import { page } from '$app/stores';

    let title = $page.data.title ? `Ethelred Games - ${$page.data.title}` : 'Ethelred Games';
</script>

<div class="fl">
<nav>
    <a href="/"><Fa icon={faHome}/> Home</a>
    </nav>
<h1>{title}</h1>
<nav></nav>
</div>
<slot></slot>

<svelte:head>
    <title>{title}</title>
</svelte:head>

<style>
    .fl {
        align-items: center;
    }
    h1 {
        flex-grow: 1;
        text-align: center;
        margin: 0.4em;
    }
    nav {
        flex: 0 0 15%;
    }
</style>