import { MatchSettings } from '$src/models/constants';
import type { Player } from '$src/models/player/player';
import _ from 'lodash';

export class MatchCaptureRequest {
    players: Player[];
    games: GameCaptureRequest[];

    private constructor(players: Player[], games: GameCaptureRequest[]) {
        this.players = players;
        this.games = games;
    }

    static create(players: Player[]): MatchCaptureRequest | undefined {
        if (players.length != MatchSettings.NUMBER_OF_PLAYERS) {
            return;
        }

        return new MatchCaptureRequest(players, buildGames(players));
    }

    canSubmit(): boolean {
        return this.games.length > 0 && this.players.length > 0 && this.games.every(game => game.isComplete());
    }
}

export class GameCaptureRequest {
    leftPlayer1: Player;
    leftPlayer2: Player;
    rightPlayer1: Player;
    rightPlayer2: Player;

    leftScore1!: number;
    leftScore2!: number;
    rightScore1!: number;
    rightScore2!: number;

    private constructor(
        leftPlayer1: Player,
        leftPlayer2: Player,
        rightPlayer1: Player,
        rightPlayer2: Player
    ) {
        this.leftPlayer1 = leftPlayer1;
        this.leftPlayer2 = leftPlayer2;
        this.rightPlayer1 = rightPlayer1;
        this.rightPlayer2 = rightPlayer2;
    }

    static create(
        leftPlayer1: Player,
        leftPlayer2: Player,
        rightPlayer1: Player,
        rightPlayer2: Player
    ): GameCaptureRequest {
        return new GameCaptureRequest(leftPlayer1, leftPlayer2, rightPlayer1, rightPlayer2);
    }

    get leftTotal() {
        return this.leftScore1 + this.leftScore2;
    }

    get rightTotal() {
        return this.rightScore1 + this.rightScore2;
    }

    get winners(): Player[] {
        if (this.isDraw() || !this.isComplete()) {
            return [];
        }

        if (this.isRightWin()) {
            return [this.rightPlayer1, this.rightPlayer2];
        }

        return [this.leftPlayer1, this.leftPlayer2];
    }

    get losers(): Player[] {
        if (this.isDraw() || !this.isComplete()) {
            return [];
        }

        if (this.isRightWin()) {
            return [this.leftPlayer1, this.leftPlayer2];
        }

        return [this.rightPlayer1, this.rightPlayer2];
    }

    isComplete(): boolean {
        return (
            !_.isNil(this.leftPlayer1) &&
            !_.isNil(this.leftPlayer2) &&
            !_.isNil(this.rightPlayer1) &&
            !_.isNil(this.rightPlayer2) &&
            !_.isNil(this.leftScore1) &&
            !_.isNil(this.leftScore2) &&
            !_.isNil(this.rightScore1) &&
            !_.isNil(this.rightScore2) &&
            (this.leftScore1 === MatchSettings.MAX_SCORE || this.rightScore1 === MatchSettings.MAX_SCORE) &&
            (this.leftScore2 === MatchSettings.MAX_SCORE || this.rightScore2 === MatchSettings.MAX_SCORE)
        );
    }

    isDraw(): boolean {
        return this.leftTotal === this.rightTotal;
    }

    isWinner(player: Player): boolean {
        return _.includes(this.winners, player);
    }

    isLoser(player: Player): boolean {
        return _.includes(this.losers, player);
    }

    isRightWin(): boolean {
        return this.rightTotal > this.leftTotal;
    }

    isLeftWin(): boolean {
        return this.leftTotal > this.rightTotal;
    }
}

function buildGames(players: Player[]): GameCaptureRequest[] {
    return [
        GameCaptureRequest.create(players[0], players[1], players[2], players[3]),
        GameCaptureRequest.create(players[0], players[2], players[1], players[3]),
        GameCaptureRequest.create(players[0], players[3], players[1], players[2])
    ];
}
