<script lang="ts">
    import { createEventDispatcher } from 'svelte';
    import _ from 'lodash';

    export let value: number;
    export let maxScore: number;
    export let reversed = false;
    export let disabled = false;

    const dispatch = createEventDispatcher();
    const scoreArray = _.times(maxScore + 1, (score) => score)
    const scores = reversed ? scoreArray.reverse() : scoreArray;

    function onSelect(score: number) {
        return () => {
            value = score;
            dispatch('input', value);
        };
    }
</script>

<div class="flex flex-row hidden lg:flex h-full">
  {#each scores as i}
    <div
        class="flex flex-row items-center
        h-full w-20 items-center gap-2  cursor-pointer  border-2 border-white"
        class:!text-error-500="{value === i && value !== maxScore}"
        class:!text-tertiary-500="{value === i && value === maxScore}"
        class:!border-error-500="{value === i && value !== maxScore}"
        class:!border-tertiary-500="{value === i && value === maxScore}"
        class:opacity-40={disabled}
        on:click={onSelect(i)}>
      <span class="block text-center w-full 2xl:text-2xl">{i}</span>

    </div>
  {/each}
</div>
