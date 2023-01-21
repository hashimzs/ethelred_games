export type Colour = 'r' | 'g' | 'b' | 'y'

export function getDisplay(c:Colour) {
    switch(c) {
        case 'b' : return 'Blue';
        case 'g' : return 'Green';
        case 'r' : return 'Red';
        case 'y' : return 'Yellow';
    }
}