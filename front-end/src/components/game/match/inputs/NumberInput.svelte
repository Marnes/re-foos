<script lang="ts">
    import { hasValue } from '$src/lib/util/match/matchUtil';
    import { createEventDispatcher } from 'svelte';

    export let value: number;
    export let minScore: number;
    export let maxScore: number;
    export let leftTeam: boolean;
    export let disabled = false;
    export let visible = false;
    export let input: any;

    export let action: any = () => {
    };

    const dispatch = createEventDispatcher();

    $: valueSet = hasValue(value)

    function onInput(event: InputEvent) {
        if (disabled) {
            return;
        }

        value = event.target.value
        dispatch('input', value);
    }
</script>

<input
    class="h-12 lg:h-16 w-12 text-2xl lg:w-16 lg:text-4xl lg:hidden
    score-input no-spinner text-center
    !bg-transparent focus:border-primary-500"
    class:!block={visible}
    type="number"
    pattern="[0-9]*"
    min="{minScore}"
    max="{maxScore}"
    data-team={leftTeam ? '1' : '2'}
    disabled={disabled}
    class:!border-error-500="{valueSet && Number(value) !== maxScore}"
    class:!border-tertiary-500="{valueSet && Number(value) === maxScore}"
    use:action
    bind:value
    bind:this={input}
    on:change={onInput}
/>
