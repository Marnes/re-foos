<script lang="ts">
    import PlayerAvatar from '$src/components/player/PlayerAvatar.svelte';
    import { Divider } from '@skeletonlabs/skeleton';
    import type { Player } from '$src/models/player/player';

    export let leftTeam: Player[] = [];
    export let rightTeam: Player[] = [];
    export let extraClass: string = '';
</script>


<div class="flex flex-row w-full">
  <div class="flex flex-row bg-blue-500/10 h-20 w-[55%] team1-container">
    <div class="flex flex-row team1 w-full h-full overflow-hidden">
      {#each leftTeam as player, i}
        <div class="h-full relative flex flex-row items-end justify-center w-28 lg:w-48">
              <span class="text-sm lg:text-2xl font-extrabold lg:mb-[-5px] opacity-100 z-[100]">
                {player.username}
              </span>
          {#if player.avatar}
            <PlayerAvatar
                player={player}
                class="absolute w-16 lg:w-32 opacity-25 test-avatar z-[99]"
                background="bg-transparent"
                rounded="rounded-none"
            />
          {/if}
        </div>
        {#if (i + 1 !== leftTeam.length)}
          <Divider vertical={true} borderWidth="border-l-2" class="opacity-50 team1-divider"/>
        {/if}
      {/each}
    </div>
  </div>
  <div class="flex flex-row bg-red-500/10 h-20 w-[55%] team2-container item">
    <div class="flex flex-row team2 w-full h-full overflow-hidden justify-end">
      {#each rightTeam as player, i}
        <div class="h-full relative flex flex-row items-end justify-center w-28 lg:w-48">
              <span class="text-sm lg:text-2xl font-extrabold lg:mb-[-5px] opacity-100 z-[2]">
                {player.username}
              </span>
          {#if player.avatar}
            <PlayerAvatar
                player={player}
                class="absolute w-16 lg:w-32 opacity-25 test-avatar z-[1]"
                background="bg-transparent"
                rounded="rounded-none"
            />
          {/if}
        </div>
        {#if (i + 1 !== leftTeam.length)}
          <Divider vertical={true} borderWidth="border-l-2" class="opacity-50 team1-divider"/>
        {/if}
      {/each}
    </div>
  </div>
</div>
<slot/>

<style lang="scss">
  .team1-container {
    transform: skewX(-20deg);
    margin-left: -20px;
    padding-left: 20px;

    .team1 {
      transform: skewX(20deg);
    }
  }

  :global(.team1-divider) {
    transform: skewX(-15deg);
  }

  :global(.team2-divider) {
    transform: skewX(15deg);
  }

  .team2-container {
    transform: skewX(-20deg);
    margin-right: -20px;
    padding-right: 20px;

    .team2 {
      text-align: right;
      transform: skewX(20deg);
    }
  }

  :global(.test-avatar) {
    top: 50%;
    left: 50%;
    transform: translate(-50%, -55%);
  }
</style>
