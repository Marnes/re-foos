import { GameCaptureRequest, MatchCaptureRequest, TeamCaptureRequest } from '$src/models/match/captureRequest';
import type { GameResult } from '$src/models/match/matchResult';
import type { Player } from '$src/models/player/player';
import type { RoundRobinGame } from '$src/models/match/roundRobinGame';

export const buildMatch = (roundRobinGames: RoundRobinGame[]): MatchCaptureRequest => {
    return new MatchCaptureRequest(roundRobinGames.map(buildGameRequest));
}

export const getWinner = (gameResult: GameResult[], players: Player[]): Player | null => {
    const winners = players.filter((player) =>
        gameResult.every((result) => result.isDraw() || result.isWinner(player))
    );

    if (winners.length !== 1) {
        return null;
    }

    return winners[0];
}

export const getLoser = (gameResult: GameResult[], players: Player[]): Player | null => {
    const losers = players.filter((player) =>
        gameResult.every(result => result.isDraw() || !result.isWinner(player))
    );

    if (losers.length !== 1) {
        return null;
    }

    return losers[0];
}

const buildGameRequest = (roundRobinGame: RoundRobinGame): GameCaptureRequest => {
    return new GameCaptureRequest(buildTeamsRequest(roundRobinGame));
}

const buildTeamsRequest = (roundRobinGame: RoundRobinGame): TeamCaptureRequest[] => {
    const leftTeam = new TeamCaptureRequest(roundRobinGame.leftTeam.map(player => player.id), roundRobinGame.leftScores);
    const rightTeam = new TeamCaptureRequest(roundRobinGame.rightTeam.map(player => player.id), roundRobinGame.rightScores);

    return [leftTeam, rightTeam];
}
