import api from '$src/lib/api';
import { json, error } from '@sveltejs/kit';
import type { RequestHandler } from '@sveltejs/kit';
import type { Game } from '$src/models/game';
import type { Match } from '$src/models/match';

export const POST: RequestHandler = async ({ cookies, request }) => {
    const match: Match = await request.json();

    const response = await api.post('matches', normalizeMatch(match), cookies.get('jwt'));

    if (response.status !== 200) {
        throw error(response.status)
    }

    return json(await response.json());
}

function normalizeMatch(match: Match) {
    return {
        games: match.games.map(normalizeGame)
    }
}

function normalizeGame(game: Game) {
    return {
        leftPlayer1: game.leftPlayer1.username,
        leftPlayer2: game.leftPlayer2.username,
        rightPlayer1: game.rightPlayer1.username,
        rightPlayer2: game.rightPlayer2.username,
        leftScore1: game.leftScore1,
        leftScore2: game.leftScore2,
        rightScore1: game.rightScore1,
        rightScore2: game.rightScore2
    }
}
