import type { PageLoad } from './$types';

export const load = (async ({params, fetch}) => {
    const res = await fetch(`/api/nuo/${params.gameId}`);
    const json = await res.json();
    json.fetch = fetch;
    json.title = 'Nuo';
    return json;
}) satisfies PageLoad;