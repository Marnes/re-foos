<script lang="ts">
    import { createEventDispatcher } from 'svelte';
    import { Avatar } from "@brainandbones/skeleton";
    import type { Player } from '$src/models/player/player';
    import { formatElo } from "$src/lib/util/elo-util";

    export let player: Player;
    export let selected = false;
    export let reverse = false;

    const dispatch = createEventDispatcher();

    function onClick(player: Player) {
        return () => {
            dispatch('click', player);
        };
    }
</script>

<div
    class="player card flex  !bg-surface-500 {$$props.class}"
    class:flex-row={!reverse}
    class:flex-row-reverse={reverse}
    class:selected
    on:click="{onClick(player)}"
>
  <div class="flex">
    <Avatar
        width="w-16"
        rounded="rounded-none"
        background="bg-tertiary-500"
        initials={player.username[0]}
    />
  </div>
  <div class="flex flex-col grow ml-2 justify-between py-1">
    <div class="flex">
      <div>
        <h1 class="text-xl font-extrabold">{player.username}</h1>
      </div>
    </div>
    <div class="flex justify-between">
      <span class="text-sm">{formatElo(player.elo)}</span>
    </div>
  </div>
</div>

<style lang="scss">
  .player {
    opacity: 1;
    transition: opacity 0.5s;

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
