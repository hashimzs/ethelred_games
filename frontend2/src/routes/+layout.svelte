<script lang="ts">
    import '../app.css';
    import Fa from 'svelte-fa';
    import { faHome } from '@fortawesome/free-solid-svg-icons';
    import { page } from '$app/stores';

    let title = $page.data.title ? `Ethelred Games - ${$page.data.title}` : 'Ethelred Games';
</script>

<div class="m-2">
<div class="navbar">
<nav class="navbar-start btn btn-ghost">
    <a href="/"><Fa icon={faHome}/> Home</a>
    </nav>
<h1 class="navbar-center">{title}</h1>
<nav class="navbar-end"></nav>
</div>
<slot></slot>
</div>

<svelte:head>
    <title>{title}</title>
</svelte:head>