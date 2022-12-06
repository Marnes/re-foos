<script lang='ts'>
    import PlayerCard from '$src/components/game/player/PlayerCard.svelte';
    import type { Player } from '$src/models/player/player';
    import _ from 'lodash';

    export let players: Player[];
    export let selectedPlayers: Player[] = [];

    $: currentPlayer = players.find((player) => player.username === 'REPLACE_WITH_CURRENT_PLAYER');
    $: remainingPlayers = _.sortBy(players, 'username').filter((player) => player.username !== currentPlayer?.username);

    function togglePlayer(player: Player) {
        if (_.includes(selectedPlayers, player)) {
            deselectPlayer(player);
        } else {
            selectPlayer(player);
        }
    }

    function selectPlayer(player: Player) {
        if (selectedPlayers.length < 4) {
            selectedPlayers = [...selectedPlayers, player];
        }
    }

    function deselectPlayer(player: Player) {
        selectedPlayers = _.without(selectedPlayers, player);
    }
</script>

<div class="player-board flex flex-col min-h-screen">
  <div class='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-1'>
    {#each remainingPlayers as player}
      <PlayerCard
          player={player}
          selected={_.includes(selectedPlayers, player)}
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