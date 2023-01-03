import _ from 'lodash';
import { userNameSentence } from '$src/lib/util/stringUtil';
import { GameResult } from '$src/models/match/matchResult';
import type { Player } from '$src/models/player/player';

export const updateScore = (score: number, index: number, maxScore: number, teamScore: number[], otherScore: (number | null)[]) => {
    teamScore[index] = score;

    if (score === maxScore) {
        if (maxScore === 1) {
            otherScore[index] = 0;
            return;
        }

        if (otherScore[index] === maxScore) {
            otherScore[index] = null;
        }
    } else {
        otherScore[index] = maxScore;
    }
}

export const getGameResult = (bestOf: number, maxScore: number, isEvenGames: boolean, team1Scores: number[], team2Scores: number[], team1: Player[], team2: Player[]): GameResult | null => {
    const team1Wins = getNumberOfWins(team1Scores, maxScore);
    const team2Wins = getNumberOfWins(team2Scores, maxScore);

    if (team1Scores.filter(hasValue).length !== team2Scores.filter(hasValue).length) {
        return null;
    }

    if (team1Wins === bestOf) {
        return GameResult.win(team1);
    }

    if (team2Wins === bestOf) {
        return GameResult.win(team2);
    }

    if (isEvenGames && (team1Wins + team2Wins === bestOf)) {
        const team1Total = _.sum(team1Scores);
        const team2Total = _.sum(team2Scores);

        if (team1Total > team2Total) {
            return GameResult.win(team1);
        }

        if (team2Total > team1Total) {
            return GameResult.win(team2);
        }

        return GameResult.draw();
    }

    return null;
}

export const canCaptureGame = (bestOf: number, isEvenGames: boolean, isComplete: boolean, index: number, team1Scores: number[], team2Scores: number[]): boolean => {
    const gameNumber = index + 1;
    const gamesPlayed = getGamesPlayed(team1Scores, team2Scores);

    if (isEvenGames || gameNumber <= bestOf) {
        return true;
    }

    if (isComplete) {
        return gameNumber <= gamesPlayed;
    }

    return gamesPlayed + 1 === gameNumber || hasScore(index, team1Scores, team2Scores);
}

export const sanitizeExtraScores = (bestOf: number, maxScore: number, gameCount: number, team1Scores: (number | undefined)[], team2Scores: (number | undefined)[]) => {
    let wins1 = 0;
    let wins2 = 0;

    for (let i = 0; i < gameCount; i++) {
        if (wins1 === bestOf || wins2 === bestOf) {
            team1Scores[i] = undefined;
            team2Scores[i] = undefined;
        } else {
            if (team1Scores[i] === maxScore) {
                wins1++;
            }

            if (team2Scores[i] === maxScore) {
                wins2++;
            }
        }
    }
}

export const getSubmitString = (winners: Player[] | null, losers: Player[] | null): string => {
    if (winners && losers) {
        return `Submit a win for&nbsp;<strong>${ userNameSentence(winners) }</strong>&nbsp;and a loss for <strong>${ userNameSentence(losers) }</strong>`;
    }

    if (winners) {
        return `Submit a win for&nbsp;<strong>${ userNameSentence(winners) }</strong>`;
    }

    if (losers) {
        return `Submit a loss for&nbsp;<strong>${ userNameSentence(losers) }</strong>`;
    }

    return `Submit a draw`;
}

export const hasValue = (value: number): Boolean => value === 0 || !_.isNil(value);

const getGamesPlayed = (team1Scores: number[], team2Scores: number[]): number => {
    return Math.max(team1Scores.filter(hasValue).length, team2Scores.filter(hasValue).length);
}

const getNumberOfWins = (scores: number[], maxScore: number): number => {
    return scores.filter(score => score === maxScore).length
}

const hasScore = (index: number, team1Scores: number[], team2Scores: number[]): boolean => {
    return !_.isNil(team1Scores[index]) || !_.isNil(team2Scores[index]);
}

