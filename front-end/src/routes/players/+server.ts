import api from '$src/lib/api';
import _ from 'lodash';
import type { Player } from '$src/models/player';
import type { RequestHandler } from '@sveltejs/kit';
import { json } from '@sveltejs/kit';

export const GET: RequestHandler = async ({ url }) => {
    let response;

    const players = url.searchParams.get('players');

    if (_.isNil(players)) {
        response = await api.get(`players`);
    } else {
        response = await api.get(`players?players=${ players }`);
    }

    return json(rankPlayers(await response.json()));
}

const rankPlayers = (players: Player[]): Player[] => {
    return _.sortBy(players, (player: Player) => -player.elo).map((player: Player, i: number) => {
        player.rank = i + 1;
        return player;
    });
}
