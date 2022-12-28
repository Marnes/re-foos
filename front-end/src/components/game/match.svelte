<script lang="ts">
    import PlayerSelect from '$src/components/game/PlayerSelect.svelte'
    import ScoreCapture from '$src/components/game/ScoreCapture.svelte'
    import { Player } from '$src/models/player/player';
    import { post } from '$src/lib/utils';
    import { toastStore } from '@skeletonlabs/skeleton';
    import { captureDrawerStore } from '$src/stores/game-store';
    import { invalidateAll } from '$app/navigation';
    import { onDestroy, onMount } from 'svelte';

    export let players;
    export let minScore;
    export let maxScore;

    let captureScore = false;
    let selectedPlayers: Player[] = [];
    let unsubscribeStore;

    onMount(() => {
        unsubscribeStore = captureDrawerStore.subscribe(value => {
            if (!value) {
                reset();
            }
        })
    });

    onDestroy(() => {
        if (unsubscribeStore) unsubscribeStore();
    });

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
            $captureDrawerStore = false;
            toastStore.trigger({ message: 'Match Captured', autohide: true, timeout: 3000 });
        }
    }
</script>

<div class="w-full h-full">
  <div class="w-full h-full relative">
    {#if captureScore}
      <ScoreCapture players={selectedPlayers} minScore="{minScore}" maxScore={maxScore} on:submitGame={submitGame}/>
    {:else }
      <PlayerSelect players={players} on:startGame={startGame}/>
    {/if}
  </div>
</div>
