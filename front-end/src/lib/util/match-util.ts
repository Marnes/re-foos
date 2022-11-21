import type { Player } from "$src/models/player/player";
import type { MatchCaptureRequest, GameCaptureRequest } from "$src/models/match/capture-request";
import _ from 'lodash';

export const getWinner = (match: MatchCaptureRequest, players: Player[]): Player | null => {
    if (_.isNil(match) || _.isEmpty(players)) {
        return null;
    }

    const winners = players.filter((player) =>
        match.games.every((game: GameCaptureRequest) => game.isDraw() || game.isWinner(player))
    );

    if (winners.length !== 1) {
        return null;
    }

    return winners[0];
}

export const getLoser = (match: MatchCaptureRequest, players: Player[]): Player | null => {
    if (_.isNil(match) || _.isEmpty(players)) {
        return null;
    }

    const losers = players.filter((player) =>
        match.games.every((game: GameCaptureRequest) => game.isDraw() || game.isLoser(player))
    );

    if (losers.length !== 1) {
        return null;
    }

    return losers[0];
}

export const getSubmitString = (winner: Player, loser: Player): string => {
    if (winner && loser) {
        return `Submit a win for&nbsp;<strong>${winner.username}</strong>&nbsp;and a loss for <strong>${loser.username}</strong>`;
    }

    if (winner) {
        return `Submit a win for&nbsp;<strong>${winner.username}</strong>`;
    }

    if (loser) {
        return `Submit a loss for&nbsp;<strong>${loser.username}</strong>`;
    }

    return `Submit with no winners or losers`;
}
