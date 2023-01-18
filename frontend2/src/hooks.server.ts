import type { HandleFetch } from '@sveltejs/kit';
 
export const handleFetch = (({ request, fetch }) => {
    console.log("handleFetch %s", request.url);
  if (request.url.startsWith('https://localhost:3000')) {
    // clone the original request, but change the URL
    request = new Request(
      request.url.replace('https://localhost:3000', 'http://localhost:7000'),
      request
    );
  }
 
  return fetch(request);
}) satisfies HandleFetch;