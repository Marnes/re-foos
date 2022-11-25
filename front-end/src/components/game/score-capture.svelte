<script lang="ts">
    import ScoreBoard from '$src/components/game/match/match-board.svelte';
    import { MatchCaptureRequest } from '$src/models/match/capture-request';
    import { createEventDispatcher } from 'svelte';
    import { getLoser, getSubmitString, getWinner } from '$src/lib/util/match-util';
    import type { Player } from '$src/models/player/player';

    export let maxScore: Number;
    export let players: Player[];

    let match = MatchCaptureRequest.create(players);

    const dispatch = createEventDispatcher();

    $: winner = getWinner(match, players);
    $: loser = getLoser(match, players);
    $: submitString = match.canSubmit() ? getSubmitString(winner, loser) : 'Submit Game';

    function submitGame() {
        dispatch('submitGame', match);
    }
</script>

<ScoreBoard bind:match maxScore={maxScore}/>
<div class="absolute bottom-0 left-0 right-0">

  <button
      class="btn btn-lg text-white w-full"
      class:bg-primary-500={winner}
      class:bg-warning-500={loser}
      class:bg-neutral-500={(winner && loser) || (!winner && !loser) || !match.canSubmit() }
      disabled={!match.canSubmit()}
      on:click={submitGame}
  >
    { @html submitString }
  </button>
</div>
