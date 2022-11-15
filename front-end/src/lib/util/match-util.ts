import type { Player } from "$src/models/player";
import type { Match } from "$src/models/match";
import type { Game } from "$src/models/game";
import _ from 'lodash';

export const getWinner = (match: Match, players: Player[]): Player | null => {
    if (_.isNil(match) || _.isEmpty(players)) {
        return null;
    }

    const winners = players.filter((player) =>
        match.games.every((game: Game) => game.isDraw() || game.isWinner(player))
    );

    if (winners.length !== 1) {
        return null;
    }

    return winners[0];
}

export const getLoser = (match: Match, players: Player[]): Player | null => {
    if (_.isNil(match) || _.isEmpty(players)) {
        return null;
    }

    const losers = players.filter((player) =>
        match.games.every((game: Game) => game.isDraw() || game.isLoser(player))
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
