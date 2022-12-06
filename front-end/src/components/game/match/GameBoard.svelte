<script lang="ts">
    import type { Game } from '$src/models/match/game';
    import type { Player } from '$src/models/player/player';
    import ScoreInput from '$src/components/game/match/ScoreInput.svelte'
    import PlayerCard from '$src/components/game/player/PlayerCard.svelte';
    import { onMount } from 'svelte';
    import _ from 'lodash';

    export let game: Game;
    export let minScore: number;
    export let maxScore: number;
    export let winner: Player = null;
    export let loser: Player = null;
    export let focus: boolean;

    $: currentPlayer = null;

    onMount(async () => {
        document.querySelector('input.focus').focus();
    });

    function updateScore(scoreSide: string, scoreNumber: number) {
        return (event) => {
            let score = event.detail.score;
            let inputElement = event.detail.inputElement;
            let scoreId = `${scoreSide}Score${scoreNumber}`;

            game[scoreId] = score;
            let inverseScoreId = `${scoreSide === 'left' ? 'right' : 'left'}Score${scoreNumber}`;
            game[inverseScoreId] = score < maxScore ? maxScore : minScore;

            if (inputElement && !_.isNull(score))
                focusNext(inputElement, !(score === maxScore || scoreSide === 'right'));
        }
    }

    function focusNext(inputElement: HTMLInputElement, skipOne = false) {
        let inputs = document.querySelectorAll('input.score-input');
        let updatedInputIndex = Array.prototype.indexOf.call(inputs, inputElement);
        let nextInputIndex = updatedInputIndex + (skipOne ? 2 : 1);

        if (nextInputIndex < inputs.length)
            setTimeout(() => {
                inputs[nextInputIndex].focus()
            });
    }
</script>

<div class="flex mb-2">
    <PlayerCard tiny player={game.leftPlayer1} class="w-full"/>

    <ScoreInput minScore={minScore}
                maxScore={maxScore}
                focus={focus}
                reversed={true}
                bind:value={game.leftScore1}
                on:input={updateScore('left', 1)}
    />

    <div class="hidden lg:block w-64 text-center text-xl relative bottom-[-4rem]">
        VS
    </div>

    <ScoreInput minScore={minScore}
                maxScore={maxScore}
                bind:value={game.rightScore1}
                on:input={updateScore('right', 1)}
    />

    <PlayerCard reverse tiny player={game.rightPlayer1} class="w-full"/>
</div>

<div class="flex">
    <PlayerCard tiny player={game.leftPlayer2} class="w-full"/>

    <ScoreInput minScore={minScore}
                maxScore={maxScore}
                reversed={true}
                bind:value={game.leftScore2}
                on:input={updateScore('left', 2)}
    />

    <div class="hidden lg:block w-64"></div>

    <ScoreInput minScore={minScore}
                maxScore={maxScore}
                bind:value={game.rightScore2}
                on:input={updateScore('right', 2)}
    />

    <PlayerCard reverse tiny player={game.rightPlayer2} class="w-full"/>
</div>
