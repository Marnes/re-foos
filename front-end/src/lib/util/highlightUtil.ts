import type { HighlightPlayer } from '$src/models/highlight/highlight';
import { HighlightPlayerResult } from '$src/models/highlight/highlight';
import _ from 'lodash';

export const buildHighlightMessage = (message: string, players: HighlightPlayer[]): string => {
    _.templateSettings.interpolate = /{([\s\S]+?)}/g;

    return _.template(message)(getTemplateValues(players))
}

const getTemplateValues = (players: HighlightPlayer[]): any => {
    return {
        winner: toSentence(getPlayers(players, HighlightPlayerResult.WIN)),
        loser: toSentence(getPlayers(players, HighlightPlayerResult.LOSE)),
        other: toSentence(getPlayers(players, HighlightPlayerResult.OTHER))
    }
}

const getPlayers = (players: HighlightPlayer[], result: HighlightPlayerResult): string[] => {
    return players.filter(player => player.result === result)
        .map(player => `<b>${player.player.username}</b>`)
}

const toSentence = (array: any[]): string => {
    return new Intl.ListFormat().format(array);
}
