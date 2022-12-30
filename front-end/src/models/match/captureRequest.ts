export class MatchCaptureRequest {
    games: GameCaptureRequest[];

    constructor(games: GameCaptureRequest[]) {
        this.games = games;
    }
}

export class GameCaptureRequest {
    teams: TeamCaptureRequest[]

    constructor(teams: TeamCaptureRequest[]) {
        this.teams = teams;
    }
}

export class TeamCaptureRequest {
    players: number[];
    scores: number[];

    constructor(players: number[], scores: number[]) {
        this.players = players;
        this.scores = scores;
    }
}
