import type { UserMin } from '$src/models/user';
import type { PlayerMin } from '$src/models/player/player';
import type { PlayerStats } from '$src/models/player/player-stats';

export class Match {
    id!: number;
    createdDate!: Date;
    user!: UserMin;
    players!: Map<number, PlayerMin[]>;
    playerStats!: Map<number, PlayerStats[]>;
    games!: Game[];
    winner!: number;
    loser!: number;
}

export class Game {
    playerStats!: Map<number, PlayerStats[]>;
    teams!: Team[];
    winner!: number;
    loser!: number;
}

export class Team {
    winner!: boolean;
    scores!: number[];
    players!: number[];
}
