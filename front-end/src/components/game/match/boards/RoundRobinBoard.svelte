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
    import { createEventDispatcher, onMount } from 'svelte';
    import { onScoreInput } from '$src/lib/actions/scoreInput.js';
    import _ from 'lodash';
    import BoardContainer from '$src/components/game/match/boards/BoardContainer.svelte';

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

    onMount(() => {
        inputElements[0].focus();
    })
</script>

<BoardContainer leftTeam={players}>
  <div class="flex flex-col items-center gap-4 w-full">
    {#each roundRobinGames as game, gameIndex}
      <div class="flex flex-row py-7">
        <div class="flex flex-col gap-2 justify-center">
          <PlayerCard player={game.leftPlayer1} class="w-full"/>
          <PlayerCard player={game.leftPlayer2} class="w-full"/>
        </div>
        <div class="flex flex-col gap-2 justify-center h-full">
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
        <div class="flex flex-col justify-center mx-3 flex-1">
          <span class="text-md xl:text-2xl font-bold text-center">VS</span>
        </div>
        <div class="flex flex-col gap-2 justify-center">
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
        <div class="flex flex-col gap-2 justify-center">
          <PlayerCard player={game.rightPlayer1} class="w-full" reversed={true}/>
          <PlayerCard player={game.rightPlayer2} class="w-full" reversed={true}/>
        </div>
      </div>
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
