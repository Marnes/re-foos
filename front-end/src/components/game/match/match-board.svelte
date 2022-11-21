<script lang="ts">
    import GameBoard from '$src/components/game/match/game-board.svelte';
    import type { MatchCaptureRequest } from 'front-end/src/models/matchCaptureRequest';
    import type { Player } from 'front-end/src/models/player/player';

    export let match: MatchCaptureRequest;
    export let maxScore: number;
    export let winner: Player = null;
    export let loser: Player = null;
</script>

{#each match.games as game}
  <div class="mb-3">
    <GameBoard bind:game maxScore="{maxScore}" winner="{winner}" loser="{loser}"/>
  </div>
{/each}
