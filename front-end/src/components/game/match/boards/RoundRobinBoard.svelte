<script lang="ts">
    import ScoreInput from '$src/components/game/match/ScoreInput.svelte';
    import BoardContainer from '$src/components/game/match/boards/BoardContainer.svelte';
    import GameBoard from '$src/components/game/match/boards/GameBoard.svelte';
    import { League } from '$src/models/league/league';
    import { Player } from '$src/models/player/player';
    import { GameResult, MatchResult, Result } from '$src/models/match/matchResult';
    import { buildRoundRobinGames, RoundRobinGame } from '$src/models/match/roundRobinGame';
    import { canCaptureGame, getGameResult, sanitizeExtraScores, updateScore } from '$src/lib/util/match/matchUtil';
    import { buildMatch, getLoser, getWinner } from '$src/lib/util/match/roundRobinUtil';
    import { getSubmitString } from '$src/lib/util/match/matchUtil.js';
    import { createEventDispatcher, onMount } from 'svelte';
    import { onScoreInput } from '$src/lib/actions/scoreInput';
    import { getBestOf } from '$src/lib/util/match/leagueUtil';
    import _ from 'lodash';

    export let league: League;
    export let players: Player[];

    const inputElements = [];

    const dispatch = createEventDispatcher();

    const minScore = 0;
    const maxScore = league.config.maxScore;
    const gameCount = league.config.scoresPerTeam;
    const isEvenGames = gameCount % 2 === 0;
    const bestOf = getBestOf(league);

    let roundRobinGames = buildRoundRobinGames(league, players)

    $: gameResults = getGameResults(roundRobinGames);
    $: matchResult = getMatchResult(gameResults);

    const setScore = (gameIndex: number, scoreIndex: number, team: number) => {
        return (event) => {
            let score = Number(event.detail);
            let game = roundRobinGames[gameIndex];

            if (team === 1) {
                updateScore(score, scoreIndex, maxScore, game.leftScores, game.rightScores);
            } else {
                updateScore(score, scoreIndex, maxScore, game.rightScores, game.leftScores);
            }

            sanitizeExtraScores(bestOf, maxScore, gameCount, game.leftScores, game.rightScores);
            roundRobinGames = roundRobinGames //Forces update after each score set
        }
    }

    const getGameResults = (roundRobinGames: RoundRobinGame[]): GameResult[] => {
        const results = [];

        for (let i = 0; i < roundRobinGames.length; i++) {
            const game = roundRobinGames[i];
            results[i] = getGameResult(bestOf, maxScore, isEvenGames, game.leftScores, game.rightScores, game.leftTeam, game.rightTeam);
        }

        return results;
    }

    const canCapture = (gameIndex: number, scoreIndex: number, team1Scores: number[], team2Scores: number[]): boolean => {
        const gameResult = gameResults[gameIndex];

        return canCaptureGame(bestOf, isEvenGames, !_.isNil(gameResult), scoreIndex, team1Scores, team2Scores);
    }

    const getMatchResult = (gameResults: GameResult[]): MatchResult | null => {
        if (gameResults.some(_.isNil)) return null;

        if (gameResults.filter(result => result.result === Result.DRAW).length >= 2) {
            return MatchResult.draw();
        }

        const winner = getWinner(gameResults, players);
        const loser = getLoser(gameResults, players);

        if (winner && loser) {
            return MatchResult.winLose([winner], [loser]);
        } else if (winner) {
            return MatchResult.win([winner])
        } else if (loser) {
            return MatchResult.lose([loser]);
        }

        return MatchResult.draw(); //Shouldn't happen
    }

    const elementPosition = (teamNumber: number, gameIndex: number, scoreIndex: number): number => {
        const startIndex = gameIndex * league.config.scoresPerTeam * 2;
        return startIndex + teamNumber + (scoreIndex * 2);
    }

    const submit = () => {
        dispatch('submit', buildMatch(roundRobinGames));
    }

    onMount(() => {
        inputElements[0]?.focus();
    })
</script>

<BoardContainer>
  <div class="flex flex-col justify-between h-full">
    {#each roundRobinGames as game, gameIndex}
      <GameBoard leftTeam={game.leftTeam} rightTeam={game.rightTeam}>
        <div class="flex flex-col gap-2">
          {#each game.leftScores as _, scoreIndex}
            <div class="flex flex-row gap-2 justify-center h-12 lg:h-16 overflow-hidden">
              <ScoreInput
                  leftTeam={true}
                  minScore={minScore}
                  maxScore={maxScore}
                  disabled={!canCapture(gameIndex, scoreIndex, game.leftScores, game.rightScores)}
                  action={onScoreInput(inputElements, minScore, maxScore)}
                  bind:input={inputElements[elementPosition(0, gameIndex, scoreIndex)]}
                  bind:value={game.leftScores[scoreIndex]}
                  on:input={setScore(gameIndex, scoreIndex, 1)}
              />
              <ScoreInput
                  leftTeam={false}
                  minScore={minScore}
                  maxScore={maxScore}
                  disabled={!canCapture(gameIndex, scoreIndex, game.leftScores, game.rightScores)}
                  action={onScoreInput(inputElements, minScore, maxScore)}
                  bind:input={inputElements[elementPosition(1, gameIndex, scoreIndex)]}
                  bind:value={game.rightScores[scoreIndex]}
                  on:input={setScore(gameIndex, scoreIndex, 2)}
              />
            </div>
          {/each}
        </div>
      </GameBoard>
    {/each}
  </div>

  <svelte:fragment slot="action">
    <button
        class="btn btn-lg text-white w-full h-[60px] bg-neutral-500"
        class:!bg-primary-500={matchResult?.isWin()}
        class:!bg-error-500={matchResult?.isLose()}
        class:!bg-warning-500={matchResult?.isWinLose()}
        disabled={_.isNil(matchResult)}
        on:click={submit}
    >
      {#if _.isNil(matchResult)}
        Cannot submit game yet
      {:else}
        {@html getSubmitString(matchResult.winners, matchResult.losers)}
      {/if}
    </button>
  </svelte:fragment>
</BoardContainer>
