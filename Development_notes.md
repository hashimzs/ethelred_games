# Development Notes and Links

## Frontend / client

* Node 18
* pnpm
* sveltekit

## Backend / server

* JDK 17 
* [Javalin](https://javalin.io/)
* ~~Redis~~

## Games

* [UNO rules](https://service.mattel.com/instruction_sheets/42001pr.pdf)


## Deployment

Development environment deployment will use Vite dev server to serve client assets. It will proxy the Javalin server.

Production environment deployment will use Javalin server to serve client assets as static files.

## Non-Goals

It would be nice to separate Lobby and game engine implementations, but for the initial MVP I'll just build what works.

Similarly, I'm trying to bear in mind an eventual scalable architecture but getting a working game in a single server is more important.