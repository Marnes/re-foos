import type { Match } from '$src/models/match/match';

export class Player {
    id!: number;
    createdDate!: Date;
    username!: string;
    rank!: number;
    elo!: number;
    eloChange!: number;
    played!: number
    wins!: number;
    losses!: number;
    highestElo!: number;
    lowestElo!: number;
    scoreFor!: number;
    scoreAgainst!: number;
    currentWinningStreak!: number;
    currentLosingStreak!: number;
    longestWinningStreak!: number;
    longestLosingStreak!: number;
}

export class PlayerMin {
    id!: number;
    username!: string;
    elo!: number;
}

export class PlayerSpotlight {
    player!: Player;
    match!: Match;
}
