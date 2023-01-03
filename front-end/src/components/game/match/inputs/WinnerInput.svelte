<script lang="ts">
    import { createEventDispatcher } from 'svelte';

    export let value: number;
    export let disabled: boolean = false;
    export let leftTeam: boolean = true;

    const dispatch = createEventDispatcher();

    function onSelect() {
        return () => {
            if (disabled) return;

            value = 1;
            dispatch('input', 1);
        };
    }
</script>

<div class="logo-cloud w-full">
  <div
      class="logo-item opacity-30"
      class:!opacity-10={disabled}
      class:!opacity-100={value === 1}
      class:!bg-blue-500={leftTeam}
      class:!bg-red-600={!leftTeam}
      on:click={onSelect()}
  >
    Winner
  </div>

</div>

<style lang="scss">
  .logo-item {
    cursor: pointer;
  }
</style>
