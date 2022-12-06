<script lang="ts">
    import { createEventDispatcher } from 'svelte';
    import { Avatar } from "@skeletonlabs/skeleton";
    import { formatElo } from "$src/lib/util/elo-util";
    import type { Player } from '$src/models/player/player';

    export let player: Player;
    export let selected = false;
    export let reverse = false;
    export let tiny = false;

    const dispatch = createEventDispatcher();

    function onClick(player: Player) {
        return () => {
            dispatch('click', player);
        };
    }
</script>

<div class="player card flex p-{tiny ? 1 : 2} lg:p-2 !bg-surface-600 {$$props.class}"
     class:flex-row={!reverse}
     class:flex-row-reverse={reverse}
     class:selected
     on:click="{onClick(player)}">

  <div class="flex items-center">
    <Avatar width="{tiny ? 'w-10' : 'w-14'} lg:w-14"
            border="border-2 border-primary-500"
            background="bg-{selected ? 'primary' : 'surface'}-500"
            initials={player.username[0]}/>
  </div>

  <div class="flex flex-col grow ml-2 justify-center py-1">
    <div class="flex">
      <div>
        <h1 class="text-{tiny ? 'sm' : 'xl'} lg:text-xl font-extrabold">{player.username}</h1>
      </div>
    </div>
    <div class="flex justify-between">
      <span class="text-{tiny ? 'xs' : 'sm'} lg:text-sm">{formatElo(player.elo)}</span>
    </div>
  </div>
</div>

<style lang="scss">
  .player {
    &.selected {
      background-image: url('$lib/assets/football.png');
      background-position: 95% center;
      background-size: 30%;
      background-repeat: no-repeat;
    }
  }

  .faded {
    opacity: 0.55;
    transition: opacity 0.5s;
  }

  .player .card-body {
    padding: 0;
  }
</style>
