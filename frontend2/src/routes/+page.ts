import type { PageLoad } from './$types';

export const load = (async ({ fetch }) => {
    const res = await fetch('/api/games');
    const gameTypes = await res.json();
    return {gameTypes, fetch};
}) satisfies PageLoad;