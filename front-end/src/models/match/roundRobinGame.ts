import type { Player } from '$src/models/player/player';
import type { League } from '$src/models/league/league';

export class RoundRobinGame {
    leftPlayer1: Player;
    leftPlayer2: Player;
    rightPlayer1: Player;
    rightPlayer2: Player;
    leftScores: number[];
    rightScores: number[];

    public constructor(leftPlayer1: Player, leftPlayer2: Player, rightPlayer1: Player, rightPlayer2: Player, leftScores: number[], rightScores: number[]) {
        this.leftPlayer1 = leftPlayer1;
        this.leftPlayer2 = leftPlayer2;
        this.rightPlayer1 = rightPlayer1;
        this.rightPlayer2 = rightPlayer2;
        this.leftScores = leftScores;
        this.rightScores = rightScores;
    }

    get leftTeam(): Player[] {
        return [this.leftPlayer1, this.leftPlayer2];
    }

    get rightTeam(): Player[] {
        return [this.rightPlayer1, this.rightPlayer2];
    }
}

export const buildRoundRobinGames = (league: League, players: Player[]): RoundRobinGame[] => {
    return [
        buildGame(players[0], players[1], players[2], players[3], league.config.scoresPerTeam),
        buildGame(players[0], players[2], players[1], players[3], league.config.scoresPerTeam),
        buildGame(players[0], players[3], players[1], players[2], league.config.scoresPerTeam)
    ];
}

const buildGame = (leftPlayer1: Player, leftPlayer2: Player, rightPlayer1: Player, rightPlayer2: Player, scoresCount: number): RoundRobinGame => {
    return new RoundRobinGame(leftPlayer1, leftPlayer2, rightPlayer1, rightPlayer2, [...Array(scoresCount)], [...Array(scoresCount)]);
}
