<script lang='ts'>
    import PlayerCard from '$src/components/game/player/PlayerCard.svelte';
    import { League, LeagueType } from '$src/models/league/league';
    import { sessionStore } from '$src/stores/sessionStore';
    import type { Player } from '$src/models/player/player';
    import _ from 'lodash';

    export let league: League;
    export let players: Player[];
    export let selectedPlayers: Player[] = [];

    let currentPlayer = players.find((player) => player.userId === $sessionStore.user.id);
    let remainingPlayers = _.sortBy(players, 'username').filter((player) => player.username !== currentPlayer?.username);

    function togglePlayer(player: Player) {
        if (_.includes(selectedPlayers, player)) {
            deselectPlayer(player);
        } else {
            selectPlayer(player);
        }

        currentPlayer = currentPlayer; //Forces update
        remainingPlayers = remainingPlayers; //Forces update
    }

    const selectPlayer = (player: Player) => {
        if (selectedPlayers.length < league.config.players) {
            selectedPlayers = [...selectedPlayers, player];
        }
    }

    const deselectPlayer = (player: Player) => {
        selectedPlayers = _.without(selectedPlayers, player);
    }

    const getTeamColor = (player: Player) => {
        if (!_.includes(selectedPlayers, player)) {
            return 'border-2 border-transparent';
        }

        if (league.config.type === LeagueType.ROUND_ROBIN) {
            return 'border-2 border-blue-500';
        }

        const playerNum = _.indexOf(selectedPlayers, player) + 1;

        if (playerNum <= league.config.playersPerTeam) {
            return 'border-2 border-blue-500';
        } else {
            return 'border-2 border-red-600';
        }
    }
</script>

<div class="player-board flex flex-col min-h-screen">
  <div class='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-1'>
    {#if currentPlayer}
      <PlayerCard
          class={getTeamColor(currentPlayer)}
          player={currentPlayer}
          on:click='{() => togglePlayer(currentPlayer)}'
      />
    {/if}
    {#each remainingPlayers as player}
      <PlayerCard
          class={getTeamColor(player)}
          player={player}
          on:click='{() => togglePlayer(player)}'
      />
    {/each}
  </div>
</div>

<style lang='scss'>
  .player-board :global(.player) {
    cursor: pointer;
  }
</style>
