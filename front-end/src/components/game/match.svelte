<script lang="ts">
    import PlayerBoard from '$src/components/game/player/player-board.svelte';
    import ScoreBoard from '$src/components/game/match/match-board.svelte';
    import { Player } from "$src/models/player";
    import { Match } from "$src/models/match";
    import { post } from "$src/lib/utils";
    import { toastStore } from "@brainandbones/skeleton";
    import { drawerStore } from "$src/stores/game-store";
    import { goto } from "$app/navigation";

    export let players;
    export let maxScore;

    let selectedPlayers: Player[] = []
    let captureScore: boolean = false;
    let match: Match

    drawerStore.subscribe(value => {
        if (!value) {
            reset();
        }
    })

    const reset = () => {
        selectedPlayers = [];
        captureScore = false;
    }

    const startGame = () => {
        if (selectedPlayers.length !== 4) {
            return;
        }

        match = Match.create(selectedPlayers);
        captureScore = true;
    }

    const submitGame = async () => {
        try {
            const response = await post(`matches`, match);
            await goto('/', { invalidateAll: true, noScroll: true });
            $drawerStore = false;
            toastStore.trigger({ message: 'Match Captured', autohide: true, timeout: 3000 });
        } catch (e) {

        }
    }
</script>

<div class="w-full h-full">
  <div class="w-full h-full relative">
    {#if captureScore}
      <ScoreBoard bind:match maxScore={maxScore}/>
      <div class="absolute bottom-0 left-0 right-0">
        <button
            class="btn bg-primary-500 btn-lg text-white w-full"
            disabled={!match.canSubmit()}
            on:click={submitGame}
        >
          Submit Game
        </button>
      </div>
    {:else }
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
    {/if}
  </div>
</div>
