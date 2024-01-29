import SoundControl from '$lib/SoundControl.svelte';
import type { PageLoad } from './$types';

export const load = (async ({params, fetch}) => {
    const res = await fetch(`/api/nuo/${params.gameId}`);
    const json = await res.json();
    json.fetch = fetch;
    json.title = 'Nuo';
    json.nav = SoundControl;
    return json;
}) satisfies PageLoad;