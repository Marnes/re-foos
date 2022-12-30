<script lang="ts">
    import PlayerBoard from '$src/components/game/player/PlayerBoard.svelte';
    import { Player } from '$src/models/player/player';
    import { League } from '$src/models/league/league';
    import { createEventDispatcher } from 'svelte';

    const dispatch = createEventDispatcher();

    export let league: League;
    export let players: Player[];

    let selectedPlayers: Player[] = [];

    $: readyToStart = selectedPlayers.length == league.config.players;
    $: startButtonText = readyToStart ? 'Start game' : `Select another ${ league.config.players - selectedPlayers.length } player(s)`

    function startGame() {
        dispatch('startGame',  selectedPlayers);
    }
</script>
<div class="flex flex-col">
  <PlayerBoard players={players} league={league} bind:selectedPlayers/>
  <button
      class="start-game sticky bottom-0 btn btn-xl text-white"
      disabled={!readyToStart}
      class:btn-filled-primary={readyToStart}
      class:btn-filled-surface={!readyToStart}
      on:click={startGame}>

    {startButtonText}
  </button>
</div>

<style lang='scss'>
  button.start-game {
    opacity: 1 !important;
  }
</style>
