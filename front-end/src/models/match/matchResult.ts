import type { Player } from '$src/models/player/player'
import _ from 'lodash';

export enum Result {
    WIN = 'WIN',
    LOSE = 'LOSE',
    DRAW = 'DRAW',
    WIN_LOSE = 'WIN_LOSE',
}

export class MatchResult {
    result: Result;
    winners: Player[] | null;
    losers: Player[] | null

    public static win(winners: Player[]): MatchResult {
        return new MatchResult(Result.WIN, winners, null);
    }

    public static lose(losers: Player[]): MatchResult {
        return new MatchResult(Result.LOSE, null, losers);
    }

    public static winLose(winners: Player[], losers: Player[]): MatchResult {
        return new MatchResult(Result.WIN_LOSE, winners, losers);
    }

    public static draw(): MatchResult {
        return new MatchResult(Result.DRAW, null, null);
    }

    private constructor(result: Result, winners: Player[] | null, losers: Player[] | null) {
        this.result = result;
        this.winners = winners;
        this.losers = losers;
    }

    isWin(): boolean {
        return this.result === Result.WIN;
    }

    isLose(): boolean {
        return this.result === Result.LOSE;
    }

    isWinLose(): boolean {
        return this.result === Result.WIN_LOSE;
    }
}

export class GameResult {
    result: Result;
    players: Player[] | null;

    public static win(players: Player[]): GameResult {
        return new GameResult(Result.WIN, players);
    }

    public static draw(): GameResult {
        return new GameResult(Result.DRAW, null);
    }

    private constructor(result: Result, players: Player[] | null) {
        this.result = result;
        this.players = players;
    }

    isWin(): boolean {
        return this.result === Result.WIN;
    }

    isDraw(): boolean {
        return this.result === Result.DRAW;
    }

    isWinner(player: Player): boolean {
        return this.result === Result.WIN && _.includes(this.players, player);
    }
}
