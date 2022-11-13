<script lang="ts">
    import GameBoard from '$src/components/game/match/game-board.svelte';
    import type { Match } from '$src/models/match';
    import type { Player } from '$src/models/player';

    export let match: Match;
    export let maxScore: number;
    export let winner: Player = null;
    export let loser: Player = null;
</script>

{#each match.games as game}
  <div class="mb-3">
    <GameBoard bind:game maxScore="{maxScore}" winner="{winner}" loser="{loser}"/>
  </div>
{/each}
