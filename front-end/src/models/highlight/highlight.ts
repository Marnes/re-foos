import type { PlayerMin } from '$src/models/player/player';

export class Highlight {
    matchId!: number;
    gameId!: number;
    message!: string;
    messageType!: MessageType;
    players!: HighlightPlayer[];
}

export class HighlightPlayer {
    result!: HighlightPlayerResult
    player!: PlayerMin
}

export enum MessageType {
    WIN = 'WIN',
    LOSE = 'LOSE',
    WIN_LOSE = 'WIN_LOSE',
    DRAW = 'DRAW',
    WHITEWASH = 'WHITEWASH'
}

export enum HighlightPlayerResult {
    WIN = 'WIN',
    LOSE = 'LOSE',
    OTHER = 'OTHER'
}

