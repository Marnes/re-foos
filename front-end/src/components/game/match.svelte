<script lang="ts">
    import PlayerSelect from '$src/components/game/player-select.svelte'
    import ScoreCapture from '$src/components/game/score-capture.svelte'
    import { Player } from '$src/models/player/player';
    import { post } from '$src/lib/utils';
    import { toastStore } from '@brainandbones/skeleton';
    import { drawerStore } from '$src/stores/game-store';
    import { invalidateAll } from '$app/navigation';

    export let players;
    export let maxScore;

    let captureScore: boolean = false;
    let selectedPlayers: Player[] = [];

    drawerStore.subscribe(value => {
        if (!value) {
            reset();
        }
    })

    const reset = () => {
        captureScore = false;
        selectedPlayers = [];
    }

    const startGame = ({ detail }: CustomEvent) => {
        if (detail.length !== 4) {
            return;
        }

        selectedPlayers = detail;
        captureScore = true;
    }

    const submitGame = async ({ detail }: CustomEvent) => {
        const response = await post(`/leagues/1/matches`, detail);

        if (response.ok) {
            await invalidateAll();
            $drawerStore = false;
            toastStore.trigger({ message: 'Match Captured', autohide: true, timeout: 3000 });
        }
    }
</script>

<div class="w-full h-full">
  <div class="w-full h-full relative">
    {#if captureScore}
      <ScoreCapture players={selectedPlayers} maxScore={maxScore} on:submitGame={submitGame}/>
    {:else }
      <PlayerSelect players={players} on:startGame={startGame}/>
    {/if}
  </div>
</div>
