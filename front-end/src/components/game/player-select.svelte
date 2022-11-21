<script lang="ts">
    import PlayerBoard from '$src/components/game/player/player-board.svelte';
    import { Player } from '$src/models/player/player';
    import { createEventDispatcher } from 'svelte';

    const dispatch = createEventDispatcher();

    export let players;

    let selectedPlayers: Player[] = []

    function startGame() {
        dispatch('startGame', selectedPlayers);
    }
</script>

<PlayerBoard players="{players}" bind:selectedPlayers/>
<div class="absolute bottom-0 left-0 right-0">
  <button
      class="btn bg-primary-500 btn-lg text-white w-full"
      disabled={selectedPlayers.length !== 4}
      on:click={startGame}
  >
    Start Game
  </button>
</div>
