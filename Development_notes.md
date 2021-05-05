# Development Notes and Links

## Frontend / client

* Node 14.whatever
* [Yarn 2](https://yarnpkg.com/getting-started/install)
* [Vue.js 3](https://v3.vuejs.org/guide/introduction.html)
* [Vite 2 with vue-ts template](https://vitejs.dev/guide/#scaffolding-your-first-vite-project)

## Backend / server

* JDK 16 (living on the edge)
* [Javalin](https://javalin.io/)
* Redis

## Deployment

Development environment deployment will use Vite dev server to serve client assets. It will proxy the Javalin server.

Production environment deployment will use Javalin server to serve client assets as static files.

## Non-Goals

It would be nice to separate Lobby and game engine implementations, but for the initial MVP I'll just build what works.

Similarly, I'm trying to bear in mind an eventual scalable architecture but getting a working game in a single server is more important.