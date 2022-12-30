<script lang="ts">
    import ScoreInput from '$src/components/game/match/ScoreInput.svelte';
    import { League } from '$src/models/league/league';
    import { onScoreInput } from '$src/lib/actions/scoreInput';
    import { canCaptureGame, getGameResult, sanitizeExtraScores, updateScore } from '$src/lib/util/match/matchUtil';
    import { Divider } from '@skeletonlabs/skeleton';
    import { Player } from '$src/models/player/player';
    import { Result } from '$src/models/match/matchResult';
    import { getSubmitString } from '$src/lib/util/match/matchUtil.js';
    import { buildMatch } from '$src/lib/util/match/headToHeadUtil';
    import { createEventDispatcher } from 'svelte';
    import _ from 'lodash';

    export let league: League;
    export let players: Player[];

    const inputElements = [];
    const dispatch = createEventDispatcher();

    const minScore = 0;
    const maxScore = league.config.maxScore;
    const gameCount = league.config.games;
    const isEvenGames = gameCount % 2 === 0;
    const bestOf = isEvenGames ? gameCount : Math.ceil(gameCount / 2);

    let team1 = players.slice(0, league.config.playersPerTeam);
    let team2 = players.slice(league.config.playersPerTeam, players.length);

    let team1Scores = [...Array(league.config.games)];
    let team2Scores = [...Array(league.config.games)];

    $: gameResult = getGameResult(bestOf, maxScore, isEvenGames, team1Scores, team2Scores, team1, team2);
    $: winner = gameResult?.result === Result.WIN ? gameResult.players : null;

    const setScore = (gameIndex: number, team: number) => {
        return (event) => {
            let score = Number(event.detail);

            if (team === 1) {
                updateScore(score, gameIndex, maxScore, team1Scores, team2Scores);
            } else {
                updateScore(score, gameIndex, maxScore, team2Scores, team1Scores);
            }

            sanitizeExtraScores(bestOf, maxScore, gameCount, team1Scores, team2Scores);

            team1Scores = team1Scores; //Force Update
            team2Scores = team2Scores; //Force Update
        }
    }

    const canCapture = (gameIndex: number, team1Scores: number[], team2Scores: number[]): boolean => {
        return canCaptureGame(bestOf, isEvenGames, !_.isNil(gameResult), gameIndex, team1Scores, team2Scores);
    }

    const submit = () => {
        dispatch('submit', buildMatch(team1, team2, team1Scores, team2Scores));
    }
</script>

<div class="flex flex-col bg-black w-full overflow-x-hidden h-full">
  <div class="flex flex-row h-full match-screen">
    <div class="flex flex-col h-full w-full justify-between bg-black/40">
      <div class="flex flex-row bg-blue-500/10 w-[90%] lg:w-[70%] team1-container items-center">
        <div class="flex flex-row team1 w-full h-full pl-5 p-4">
          {#each team1 as player, i}
               <span class="text-sm lg:text-2xl px-3 lg:px-5 py-1
                font-mono block font-bold">
              {player.username}
            </span>
            {#if (i + 1 !== team1.length)}
              <Divider vertical={true} borderWidth="border-l-2" class="opacity-50 team1-divider"/>
            {/if}
          {/each}
        </div>
      </div>
      <div class="flex flex-row justify-center overflow-y-auto py-5">
        <div class="flex-1"></div>
        <div class="flex flex-col gap-2 items-center justify-between">
          {#each team1Scores as _, i}
            <ScoreInput
                teamNumber="1"
                minScore={minScore}
                maxScore={maxScore}
                disabled={!canCapture(i, team1Scores, team2Scores)}
                action={onScoreInput(inputElements, minScore, maxScore)}
                bind:input={inputElements[i * 2]}
                bind:value={team1Scores[i]}
                on:input={setScore(i, 1)}
            />
          {/each}
        </div>
        <div class="flex-1"></div>
        <div class="flex flex-col gap-2 items-center justify-between">
          {#each team2Scores as _, i}
            <ScoreInput
                reversed
                teamNumber="2"
                minScore={minScore}
                maxScore={maxScore}
                disabled={!canCapture(i, team1Scores, team2Scores)}
                action={onScoreInput(inputElements, minScore, maxScore)}
                bind:input={inputElements[1 + (i * 2)]}
                bind:value={team2Scores[i]}
                on:input={setScore(i, 2)}
            />
          {/each}
        </div>
        <div class="flex-1"></div>
      </div>
      <div class="flex flex-row self-end bg-red-600/30 w-[90%] lg:w-[70%] team2-container items-center">
        <div class="flex flex-row team2 w-full h-full pl-5 p-4 justify-end">
          {#each team2 as player, i}
            {#if (i !== 0) }
              <Divider vertical={true} borderWidth="border-l-2" class="opacity-50 team2-divider"/>
            {/if}
            <span class="text-sm lg:text-2xl px-3 lg:px-5 py-1
            font-mono block font-bold">
                {player.username}
              </span>
          {/each}
        </div>
      </div>
    </div>
  </div>
  <div>
    <button
        class="btn btn-lg text-white w-full h-[60px] bg-neutral-500"
        class:!bg-primary-500={gameResult?.isWin()}
        disabled={_.isNil(gameResult)}
        on:click={submit}
    >
      {#if _.isNil(gameResult)}
        Cannot submit game yet
      {:else}
        {@html getSubmitString(gameResult.players, null)}
      {/if}
    </button>
  </div>
</div>

<style lang="scss">
  .match-screen {
    background-image: url('$lib/assets/match-background.jpg');
    background-repeat: no-repeat;
    background-position: center center;
    background-size: 100%;
  }

  .team1-container {
    transform: skewX(-20deg);
    margin-left: -20px;
    padding-left: 20px;

    .team1 {
      transform: skewX(20deg);
    }
  }

  :global(.team1-divider) {
    transform: skewX(-15deg);
  }

  :global(.team2-divider) {
    transform: skewX(15deg);
  }

  .team2-container {
    transform: skewX(20deg);
    margin-right: -20px;
    padding-right: 20px;

    .team2 {
      text-align: right;
      transform: skewX(-20deg);
    }
  }
</style>
