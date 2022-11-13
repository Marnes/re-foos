import { MatchSettings } from '$src/models/constants';

import { Game } from '$src/models/game';
import type { Player } from '$src/models/player';

export class Match {
	players: Player[];
	games: Game[];

	private constructor(players: Player[], games: Game[]) {
		this.players = players;
		this.games = games;
	}

	static create(players: Player[]): Match | undefined {
		if (players.length != MatchSettings.NUMBER_OF_PLAYERS) {
			return;
		}

		return new Match(players, buildGames(players));
	}

	canSubmit(): boolean {
		return this.games.length > 0 && this.players.length > 0 && this.games.every(game => game.isComplete());
	}
}

function buildGames(players: Player[]): Game[] {
	return [
		Game.create(players[0], players[1], players[2], players[3]),
		Game.create(players[0], players[2], players[1], players[3]),
		Game.create(players[0], players[3], players[1], players[2])
	];
}
