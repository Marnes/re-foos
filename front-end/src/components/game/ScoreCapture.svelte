<script lang="ts">
    import ScoreBoard from '$src/components/game/match/MatchBoard.svelte';
    import { MatchCaptureRequest } from '$src/models/match/capture-request';
    import { createEventDispatcher } from 'svelte';
    import { getLoser, getSubmitString, getWinner } from '$src/lib/util/match-util';
    import type { Player } from '$src/models/player/player';

    export let minScore: number;
    export let maxScore: number;
    export let players: Player[];

    let match = MatchCaptureRequest.create(players);

    const dispatch = createEventDispatcher();

    $: winner = getWinner(match, players);
    $: loser = getLoser(match, players);
    $: submitString = match.canSubmit() ? getSubmitString(winner, loser) : 'Submit Game';
    $: submitClass = getSubmitClass(winner, loser);

    function submitGame() {
        dispatch('submitGame', match);
    }

    const getSubmitClass = (winner, loser): string => {
        console.log(winner);
        if (winner && !loser) {
            return 'bg-tertiary-500'
        }

        if (loser && !winner) {
            return 'bg-warning-500'
        }

        return 'bg-neutral-500';
    }
</script>

<ScoreBoard bind:match minScore="{minScore}" maxScore={maxScore}/>
<div class="absolute bottom-0 left-0 right-0">
  <button
      class="btn btn-lg text-white w-full {submitClass}"
      disabled={!match.canSubmit()}
      on:click={submitGame}>

    { @html submitString }
  </button>
</div>
