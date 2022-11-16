<script lang="ts">
    import PlayerCard from '$src/components/game/player/player-card.svelte';
    import { Player } from "$src/models/player";
    import { createEventDispatcher } from 'svelte';
    import _ from 'lodash';

    export let player: Player[];
    export let maxScore: number;
    export let value: number;
    export let reverse = false;

    const dispatch = createEventDispatcher();

    const scores = _.times(maxScore + 1, (score) => score);

    function onSelect(score) {
        return () => {
            value = score;
            dispatch('input', value);
        };
    }
</script>

<div
    class="flex"
    class:flex-row={!reverse}
    class:flex-row-reverse={reverse}
>
  <div class="shrink">
    <PlayerCard
        reverse={reverse}
        player={player}
        class="w-64"
    />
  </div>
  {#each scores as i}
    <div
        class="basis-1/6 stat stat-{i} bg-surface-700 relative"
        class:shadow-2xl="{i === value}"
        class:shadow-black="{i === value}"
        class:selected="{i === value}"
        on:click={onSelect(i)}
    >
      <div class="text-2xl absolute stat-text">{i}</div>
    </div>
  {/each}
</div>

<style lang="scss">
  .stat {
    cursor: pointer;

    &-text {
      top: 50%;
      left: 50%;
      -ms-transform: translateX(-50%) translateY(-50%);
      -webkit-transform: translate(-50%, -50%);
      transform: translate(-50%, -50%);
    }

    &.selected {
      opacity: 1;
      transition: opacity 0.5s;
    }
  }

  .stat-0 {
    &.selected {
      background-color: #ff3e00;
      color: white;
    }
  }

  .stat-1 {
    &.selected {
      background-color: #e64749;
      color: white;
    }
  }

  .stat-2 {
    &.selected {
      background-color: #f2a950;
      color: white;
    }
  }

  .stat-3 {
    &.selected {
      background-color: #e5c269;
      color: white;
    }
  }

  .stat-4 {
    &.selected {
      background-color: #7bb173;
      color: white;
    }
  }

  .stat-5 {
    &.selected {
      background-color: #24443b;
      color: white;
    }
  }
</style>
