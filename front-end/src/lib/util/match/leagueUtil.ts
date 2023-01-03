import type { League } from '$src/models/league/league';
import { LeagueType } from '$src/models/league/league';
import _ from 'lodash';

export const getBestOf = (league: League): number => {
    let gameCount = league.config.games;

    if (league.config.type === LeagueType.ROUND_ROBIN) {
        gameCount = league.config.scoresPerTeam
    }

    if (gameCount % 2 === 0) {
        return gameCount;
    }

    return Math.ceil(gameCount / 2);
}

export const getTeamComposition = (league: League): string => {
    return [...Array(league.config.teams)].map(_ => league.config.playersPerTeam)
        .join(' v ');
}

export const showScores = (league: League): boolean => {
    return league.config.maxScore > 1 || getBestOf(league) > 1;
}

export const scoreString = (league: League, score: number): string => {
    if (league.config.maxScore === 1) {
        if (score === 1) return 'Winner';

        return '-';
    }

    return score.toString();
}

export const seasonPath = (path: string, season: string | number | null | undefined): string => {
    if (_.isEmpty(season)) {
        return path;
    }

    if (_.includes(path, '?')) {
        return `${ path }&season=${ season }`;
    }

    return `${ path }?season=${ season }`;
}
