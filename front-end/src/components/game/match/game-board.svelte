<script lang="ts">
    import ScoreInput from '$src/components/game/match/score-input.svelte'
    import type { Game } from 'front-end/src/models/match/game';
    import type { Player } from 'front-end/src/models/player/player';

    export let game: Game;
    export let maxScore: number;
    export let winner: Player = null;
    export let loser: Player = null;

    $: currentPlayer = null;

    function updateScore(game: Game, left: boolean, first: boolean) {
        return ({ detail }) => {
            setScore(game, detail, left, first);
            setScore(game, detail === maxScore ? null : maxScore, !left, first);
        };
    }

    function setScore(game: Game, score: number, left: boolean, first: boolean) {
        if (left) {
            if (first) {
                game.leftScore1 = score;
            } else {
                game.leftScore2 = score;
            }
        } else {
            if (first) {
                game.rightScore1 = score;
            } else {
                game.rightScore2 = score;
            }
        }
    }
</script>

<div class="flex items-center gap-4">
  <div class="grow">
    <div class="mb-1">
      <ScoreInput
          player={game.leftPlayer1}
          maxScore={maxScore}
          bind:value={game.leftScore1}
          on:input={updateScore(game, true, true)}
      />
    </div>
    <div>
      <ScoreInput
          player={game.leftPlayer2}
          maxScore={maxScore}
          bind:value={game.leftScore2}
          on:input={updateScore(game, true, false)}
      />
    </div>
  </div>
  <div class="grow-0">
    <span class="text-lg font-bold">VS</span>
  </div>
  <div class="grow">
    <div class="mb-1">
      <ScoreInput
          reverse
          player={game.rightPlayer1}
          maxScore={maxScore}
          bind:value={game.rightScore1}
          on:input={updateScore(game, false, true)}
      />
    </div>
    <div>
      <ScoreInput
          reverse
          player={game.rightPlayer2}
          maxScore={maxScore}
          bind:value={game.rightScore2}
          on:input={updateScore(game, false, false)}
      />
    </div>
  </div>
</div>

