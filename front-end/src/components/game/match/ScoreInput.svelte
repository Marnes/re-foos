<script lang="ts">
    import WinnerInput from '$src/components/game/match/inputs/WinnerInput.svelte';
    import NumberInput from '$src/components/game/match/inputs/NumberInput.svelte';
    import GridInput from '$src/components/game/match/inputs/GridInput.svelte';

    import { createEventDispatcher } from 'svelte';
    import { hasValue } from '$src/lib/util/match/matchUtil';

    export let value: number;
    export let minScore: number;
    export let maxScore: number;
    export let leftTeam: boolean = true;
    export let disabled = false;
    export let input: any;

    export let action: any = () => {
    };

    const dispatch = createEventDispatcher();
    $: valueSet = hasValue(value)

    const showWinnerSelector = maxScore === 1;
    const showNumberInput = maxScore > 8;

    function onInput(event: CustomEvent) {
        if (disabled) {
            return;
        }

        value = event.detail
        dispatch('input', Number(value));
    }

</script>

{#if showWinnerSelector}
  <WinnerInput/>
{:else }
  <NumberInput
      action={action}
      minScore={minScore}
      maxScore={maxScore}
      disabled={disabled}
      leftTeam={leftTeam}
      bind:input
      bind:value
      on:input={onInput}
  />
  <GridInput
      maxScore={maxScore}
      reversed={!leftTeam}
      disabled={disabled}
      leftTeam={leftTeam}
      bind:value
      on:input={onInput}
  />
{/if}

