import { GameCaptureRequest, MatchCaptureRequest, TeamCaptureRequest } from '$src/models/match/captureRequest';
import type { Player } from '$src/models/player/player';

export const buildMatch = (team1: Player[], team2: Player[], team1Scores: number[], team2Scores: number[]): MatchCaptureRequest => {
    return new MatchCaptureRequest(buildGameRequests(team1, team2, team1Scores, team2Scores));
}

const buildGameRequests = (team1: Player[], team2: Player[], team1Scores: number[], team2Scores: number[]): GameCaptureRequest[] => {
    const gameRequests: GameCaptureRequest[] = [];

    for (let i = 0; i < team1Scores.filter(Number).length; i++) {
        const team1Request = buildTeamsRequest(team1, team1Scores[i]);
        const team2Request = buildTeamsRequest(team2, team2Scores[i]);
        gameRequests.push(new GameCaptureRequest([team1Request, team2Request]));
    }

    return gameRequests;
}

const buildTeamsRequest = (team: Player[], score: number): TeamCaptureRequest => {
    return new TeamCaptureRequest(team.map(player => player.id), [score]);

}
