<script lang="ts">
    import PlayerSelect from '$src/components/game/PlayerSelect.svelte'
    import ScoreCapture from '$src/components/game/ScoreCapture.svelte'
    import { Player } from '$src/models/player/player';
    import { post } from '$src/lib/utils';
    import { drawerStore, toastStore } from '@skeletonlabs/skeleton';
    import { invalidateAll } from '$app/navigation';
    import { League } from '$src/models/league/league';

    export let league: League;
    export let players: Player[];

    let captureScore = false;
    let selectedPlayers: Player[] = [];

    const reset = () => {
        captureScore = false;
        selectedPlayers = [];
    }

    const startGame = ({ detail }: CustomEvent) => {
        selectedPlayers = detail;
        captureScore = true;
    }

    const submitGame = async ({ detail }: CustomEvent) => {
        const response = await post(`/leagues/${league.id}/matches`, detail);

        if (response.ok) {
            drawerStore.close();
            toastStore.trigger({ message: 'Match Captured', autohide: true, timeout: 3000 });
            await invalidateAll();
        }
    }
</script>

<div class="w-full h-full">
  <div class="w-full h-full relative">
    {#if captureScore}
      <ScoreCapture league={league} players={selectedPlayers} on:submit={submitGame}/>
    {:else }
      <PlayerSelect
          players={players}
          league={league}
          on:startGame={startGame}
      />
    {/if}
  </div>
</div>
