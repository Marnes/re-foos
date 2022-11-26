<script lang="ts">
    import PlayerBoard from '$src/components/game/player/player-board.svelte';
    import { Player } from '$src/models/player/player';
    import { createEventDispatcher } from 'svelte';
    import { MatchSettings } from '$src/models/constants';

    const dispatch = createEventDispatcher();

    export let players;

    let selectedPlayers: Player[] = [];

    $: readyToStart = selectedPlayers.length == MatchSettings.NUMBER_OF_PLAYERS;
    $: startButtonText = readyToStart ? 'Start game' : `Select another ${MatchSettings.NUMBER_OF_PLAYERS - selectedPlayers.length} player(s)`

    function startGame() {
        dispatch('startGame', selectedPlayers);
    }
</script>
<div class="flex flex-col">
    <PlayerBoard players="{players}" bind:selectedPlayers/>
    <button
            class="start-game sticky bottom-0 btn btn-xl"
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
