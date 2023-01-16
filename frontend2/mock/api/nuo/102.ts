export default () => {
    return {
        path: '/api/nuo/102',
        playerView: {
            status: 'IN_PROGRESS',
            self: {
                id: 123,
                name: 'Mocky',
                hand: ['r2','g3','g4','xw']
            },
            shortCode: 'ABCD',
            availableActions: [
                {
                    name: 'playCard',
                    possibleArguments: ['r2','g3','g4','xw']
                },
                {
                    name: 'drawCard',
                    possibleArguments: []
                }
            ],
            players: [
                {
                    id: 123,
                    name: 'Mocky',
                    cardCount: 4,
                    turn: true
                },
                {
                    id: 456,
                    name: 'Faker',
                    cardCount: 5,
                    turn: false
                }
            ],
            reversedDirection: false,
            current: 'g5'

        }
    }
}