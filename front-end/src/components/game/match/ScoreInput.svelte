<script lang="ts">
    import { createEventDispatcher } from 'svelte';
    import _ from "lodash";

    export let minScore: number;
    export let maxScore: number;
    export let value: number;
    export let focus = false;
    export let reversed = false;

    const dispatch = createEventDispatcher();
    $: scoreArray =  _.times(maxScore + 1, (score) => score)
    $: scores = reversed ? scoreArray.reverse() : scoreArray;

    function onInput(event) {
        let inputElement = event.target;
        let score = inputElement.valueAsNumber;

        if (_.isNaN(score))
            score = minScore;
        else if (score < minScore)
            score = minScore
        else if (score > maxScore)
            score = maxScore;

        dispatch('input', {score: score, inputElement: inputElement});
    }

    function onSelect(score: number) {
        return () => {
            value = score;
            dispatch('input', {score: score});
        };
    }
</script>

<input class="score-input lg:hidden h-18 w-12 no-spinner text-center text-2xl stat-{value}"
       class:focus={focus}
       type="number"
       pattern="[0-9]*"
       min="{minScore}"
       max="{maxScore}"
       bind:value
       on:input={onInput}
       on:focus={(e) => e.target.select()}
>

{#each scores as i}
    <div class="hidden lg:flex
                basis-1/6 justify-center items-center
                text-2xl cursor-pointer bg-surface-700 stat-{i}"
         class:shadow-2xl="{i === value}"
         class:shadow-black="{i === value}"
         class:selected="{i === value}"
         on:click={onSelect(i)}>

        {i}
    </div>
{/each}

<style lang="scss">
  input.stat-0, .stat-0.selected {
    background-color: #ff3e00;
  }

  input.stat-1, .stat-1.selected {
      background-color: #e64749;
  }

  input.stat-2, .stat-2.selected {
      background-color: #f2a950;
  }

  input.stat-3, .stat-3.selected {
      background-color: #e5c269;
  }

  input.stat-4, .stat-4.selected {
      background-color: #7bb173;
  }

  input.stat-5, .stat-5.selected {
      background-color: #054d00;
  }
</style>
