export class League {
    id!: number;
    name!: string;
    startDate!: Date;
    endDate!: Date;
    isClosed!: boolean;
    joined!: boolean;
    config!: LeagueConfig;
}

export class LeagueCreation {
    name!: string;
    startDate!: Date;
    endDate!: Date;
    config!: LeagueConfig;
}

export class LeagueConfig {
    startingElo!: number;
    type!: LeagueType;
    games!: number;
    teams!: number;
    players!: number;
    scoresPerTeam!: number;
    playersPerTeam!: number;
    maxScore!: number;
}

export class LeagueSummary {
    id!: number;
    name!: string;
    startDate!: Date;
    endDate!: Date;
    joined!: boolean;
    teamComposition!: string;
    players!: number;
    type!: LeagueType;
    isClosed!: boolean;
}

export enum LeagueType {
    HEAD_TO_HEAD = 'HEAD_TO_HEAD',
    ROUND_ROBIN = 'ROUND_ROBIN'
}

export namespace LeagueType {
    export const humanName = (type: LeagueType): string => {
        switch (type) {
            case LeagueType.ROUND_ROBIN:
                return 'Round Robin';
            case LeagueType.HEAD_TO_HEAD:
                return 'Head to Head'
        }
    }
}
