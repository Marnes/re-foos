<script lang="ts">
    import BoardContainer from '$src/components/game/match/boards/BoardContainer.svelte';
    import GameBoard from '$src/components/game/match/boards/GameBoard.svelte';
    import ScoreInput from '$src/components/game/match/ScoreInput.svelte';
    import { League } from '$src/models/league/league';
    import { onScoreInput } from '$src/lib/actions/scoreInput';
    import { canCaptureGame, getGameResult, sanitizeExtraScores, updateScore } from '$src/lib/util/match/matchUtil';
    import { Player } from '$src/models/player/player';
    import { Result } from '$src/models/match/matchResult';
    import { getSubmitString } from '$src/lib/util/match/matchUtil.js';
    import { buildMatch } from '$src/lib/util/match/headToHeadUtil';
    import { createEventDispatcher, onMount } from 'svelte';
    import { getBestOf } from '$src/lib/util/match/leagueUtil';
    import _ from 'lodash';

    export let league: League;
    export let players: Player[];

    const inputElements = [];
    const dispatch = createEventDispatcher();

    const minScore = 0;
    const maxScore = league.config.maxScore;
    const gameCount = league.config.games;
    const isEvenGames = gameCount % 2 === 0;
    const bestOf = getBestOf(league);

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

    onMount(() => {
        if (!_.isNil(inputElements[0])) inputElements[0].focus();
    })
</script>

<BoardContainer>
  <GameBoard leftTeam={team1} rightTeam={team2}>
    <div class="flex flex-col gap-2 mt-5">
      {#each team1Scores as _, i}
        <div class="flex flex-row gap-2 justify-center items-center flex-1 overflow-hidden">
          <ScoreInput
              leftTeam={true}
              minScore={minScore}
              maxScore={maxScore}
              disabled={!canCapture(i, team1Scores, team2Scores)}
              action={onScoreInput(inputElements, minScore, maxScore)}
              bind:input={inputElements[i * 2]}
              bind:value={team1Scores[i]}
              on:input={setScore(i, 1)}
          />
          <ScoreInput
              leftTeam={false}
              minScore={minScore}
              maxScore={maxScore}
              disabled={!canCapture(i, team1Scores, team2Scores)}
              action={onScoreInput(inputElements, minScore, maxScore)}
              bind:input={inputElements[1 + (i * 2)]}
              bind:value={team2Scores[i]}
              on:input={setScore(i, 2)}
          />
        </div>
      {/each}
    </div>
  </GameBoard>

  <svelte:fragment slot="action">
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
  </svelte:fragment>
</BoardContainer>
