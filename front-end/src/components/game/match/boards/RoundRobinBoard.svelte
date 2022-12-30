<script lang="ts">
    import PlayerCard from '$src/components/game/player/PlayerCard.svelte';
    import ScoreInput from '$src/components/game/match/ScoreInput.svelte';
    import { League } from '$src/models/league/league';
    import { Divider } from '@skeletonlabs/skeleton';
    import { Player } from '$src/models/player/player';
    import { GameResult, MatchResult, Result } from '$src/models/match/matchResult';
    import { buildRoundRobinGames, RoundRobinGame } from '$src/models/match/roundRobinGame';
    import { canCaptureGame, getGameResult, sanitizeExtraScores, updateScore } from '$src/lib/util/match/matchUtil';
    import { buildMatch, getLoser, getWinner } from '$src/lib/util/match/roundRobinUtil';
    import { getSubmitString } from '$src/lib/util/match/matchUtil.js';
    import { createEventDispatcher } from 'svelte';
    import { onScoreInput } from '$src/lib/actions/scoreInput.js';
    import _ from 'lodash';

    export let league: League;
    export let players: Player[];

    const inputElements = [];

    const dispatch = createEventDispatcher();

    const minScore = 0;
    const maxScore = league.config.maxScore;
    const gameCount = league.config.scoresPerTeam;
    const isEvenGames = gameCount % 2 === 0;
    const bestOf = isEvenGames ? gameCount : Math.ceil(gameCount / 2);

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
</script>

<div class="flex flex-col bg-black w-full overflow-x-hidden h-full">
  <div class="flex flex-row h-full match-screen">
    <div class="flex flex-col h-full w-full justify-between bg-black/40">
      <div class="flex flex-row bg-blue-500/10 w-[90%] lg:w-[70%] team1-container items-center">
        <div class="flex flex-row team1 w-full h-full pl-5 p-4">
          {#each players as player, i}
               <span class="text-sm lg:text-2xl px-3 lg:px-5 py-1
                font-mono block font-bold">
              {player.username}
            </span>
            {#if (i + 1 !== players.length)}
              <Divider vertical={true} borderWidth="border-l-2" class="opacity-50 team1-divider"/>
            {/if}
          {/each}
        </div>
      </div>
      <div class="flex flex-col items-center gap-4 w-full">
        {#each roundRobinGames as game, gameIndex}
          <div class="flex flex-row !bg-tertiary-500/5 p-7 justify-between">
            <div class="flex flex-col">
              <PlayerCard tiny player={game.leftPlayer1} class="w-full"/>
              <PlayerCard tiny player={game.leftPlayer2} class="w-full"/>
            </div>
            <div class="flex flex-col gap-2">
              {#each game.leftScores as score, scoreIndex}
                <ScoreInput
                    teamNumber="1"
                    minScore={minScore}
                    maxScore={maxScore}
                    disabled={!canCapture(gameIndex, scoreIndex, game.leftScores, game.rightScores)}
                    action={onScoreInput(inputElements, minScore, maxScore)}
                    bind:input={inputElements[elementPosition(0, gameIndex, scoreIndex)]}
                    bind:value={score}
                    on:input={setScore(gameIndex, scoreIndex, 1)}
                />
              {/each}
            </div>
            <div class="flex flex-col gap-2">
              {#each game.rightScores as score, scoreIndex}
                <ScoreInput
                    teamNumber="2"
                    minScore={minScore}
                    maxScore={maxScore}
                    reversed={true}
                    disabled={!canCapture(gameIndex, scoreIndex, game.leftScores, game.rightScores)}
                    action={onScoreInput(inputElements, minScore, maxScore)}
                    bind:input={inputElements[elementPosition(1, gameIndex, scoreIndex)]}
                    bind:value={score}
                    on:input={setScore(gameIndex, scoreIndex, 2)}
                />
              {/each}
            </div>
            <div class="flex flex-col">
              <PlayerCard tiny player={game.rightPlayer1} class="w-full" reversed={true}/>
              <PlayerCard tiny player={game.rightPlayer2} class="w-full" reversed={true}/>
            </div>
          </div>
        {/each}
      </div>
      <div></div>
    </div>
  </div>
  <div>
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
</style>
