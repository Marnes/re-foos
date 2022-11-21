import api from '$src/lib/api';
import _ from 'lodash';
import type { Player } from 'front-end/src/models/player/player';
import type { RequestHandler } from '@sveltejs/kit';
import { json } from '@sveltejs/kit';

export const GET: RequestHandler = async ({ params }) => {
    let response = await api.get(`/leagues/${ params.slug }/players`);

    return json(rankPlayers(await response.json()));
}

const rankPlayers = (players: Player[]): Player[] => {
    return _.sortBy(players, (player: Player) => -player.elo).map((player: Player, i: number) => {
        player.rank = i + 1;
        return player;
    });
}
