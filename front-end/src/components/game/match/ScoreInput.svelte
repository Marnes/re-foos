<script lang="ts">
    import { createEventDispatcher } from 'svelte';
    import { hasValue } from '$src/lib/util/match/matchUtil';
    import _ from 'lodash';

    export let teamNumber: number;
    export let minScore: number;
    export let maxScore: number;
    export let value: number;
    export let reversed = false;
    export let action: any = () => {};
    export let input: any;
    export let disabled = false;

    const dispatch = createEventDispatcher();
    $: scoreArray = _.times(maxScore + 1, (score) => score)
    $: scores = reversed ? scoreArray.reverse() : scoreArray;
    $: showNumberInput = maxScore > 11;
    $: valueSet = hasValue(value)

    function onSelect(score: number) {
        return () => {
            if (disabled) {
                return;
            }

            value = score;
            dispatch('input', value);
        };
    }

    function onInput(event: InputEvent) {
        if (disabled) {
            return;
        }

        value = event.target.value
        dispatch('input', value);
    }

</script>

<input
    class="h-18 w-12 text-2xl lg:h-24 lg:w-24 lg:text-4xl lg:hidden
    score-input no-spinner text-center
    !bg-transparent focus:border-primary-500"
    type="number"
    pattern="[0-9]*"
    min="{minScore}"
    max="{maxScore}"
    data-team={teamNumber}
    disabled={disabled}
    class:!block={showNumberInput}
    class:!border-error-500="{valueSet && value !== maxScore}"
    class:!border-tertiary-500="{valueSet && value === maxScore}"
    use:action
    bind:value
    bind:this={input}
    on:change={onInput}
/>

<div
    class="flex flex-row hidden lg:flex h-full"
    class:!hidden={showNumberInput}
>
  {#each scores as i}
    <div
        class="flex flex-row items-center
        w-20 items-center gap-2  cursor-pointer  border-2 border-white"
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
