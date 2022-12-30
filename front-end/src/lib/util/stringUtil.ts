import type { Player, PlayerMin } from '$src/models/player/player';

export const toSentence = (array: any[]): string => {
    return new Intl.ListFormat().format(array);
}

export const userNameSentence = (array: Player[] | PlayerMin[]): string => {
    return new Intl.ListFormat().format(array.map(player => player.username));
}
