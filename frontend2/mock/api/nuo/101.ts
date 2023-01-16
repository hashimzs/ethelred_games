export default () => {
    return {
        path: '/api/nuo/101',
        playerView: {
            status: 'PRESTART',
            self: {
                id: 123,
                name: 'Mocky'
            },
            shortCode: 'ABCD',
            availableActions: [
                {
                    name: 'playerReady',
                    possibleArguments: []
                }
            ],
            players: [
                {
                    id: 123,
                    name: 'Mocky',
                    ready: false
                },
                {
                    id: 456,
                    name: 'Faker',
                    ready: true
                }
            ],
            reversedDirection: false,

        }
    }
}