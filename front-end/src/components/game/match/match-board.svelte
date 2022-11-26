<script lang="ts">
    import GameBoard from '$src/components/game/match/game-board.svelte';
    import type { MatchCaptureRequest } from '$src/models/matchCaptureRequest';
    import type { Player } from '$src/models/player/player';

    export let match: MatchCaptureRequest;
    export let minScore: number;
    export let maxScore: number;
    export let winner: Player = null;
    export let loser: Player = null;
</script>


{#each match.games as game, index}
    <div class="mb-4">
        <GameBoard minScore="{minScore}"
                   maxScore="{maxScore}"
                   winner="{winner}"
                   loser="{loser}"
                   focus="{index === 0}"
                   bind:game/>
    </div>
{/each}
