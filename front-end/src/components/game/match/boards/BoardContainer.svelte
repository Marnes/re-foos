<script lang="ts">
    import { Divider } from '@skeletonlabs/skeleton';
    import type { Player } from '$src/models/player/player';
    import _ from 'lodash';

    export let leftTeam: Player[] = [];
    export let rightTeam: Player[] = [];
</script>

<div class="flex flex-col bg-black w-full overflow-x-hidden h-full">
  <div class="flex flex-row h-full match-screen">
    <div class="flex flex-col h-full w-full justify-between card-glass-surface">
      <div class="flex flex-row bg-blue-500/10 w-[90%] lg:w-[70%] team1-container items-center">
        <div class="flex flex-row team1 w-full h-full pl-5 p-4">
          {#if _.isEmpty(leftTeam)}
            <span class="text-sm lg:text-2xl px-3 lg:px-5 py-1 font-mono block font-bold">&nbsp;</span>
          {/if}
          {#each leftTeam as player, i}
               <span class="text-sm lg:text-2xl px-3 lg:px-5 py-1
                font-mono block font-bold">
              {player.username}
            </span>
            {#if (i + 1 !== leftTeam.length)}
              <Divider vertical={true} borderWidth="border-l-2" class="opacity-50 team1-divider"/>
            {/if}
          {/each}
        </div>
      </div>

      <slot />

      <div class="flex flex-row self-end bg-red-600/30 w-[90%] lg:w-[70%] team2-container items-center">
        <div class="flex flex-row team2 w-full h-full pl-5 p-4 justify-end">
          {#if _.isEmpty(rightTeam)}
            <span class="text-sm lg:text-2xl px-3 lg:px-5 py-1 font-mono block font-bold">&nbsp;</span>
          {/if}
          {#each rightTeam as player, i}
            {#if (i !== 0) }
              <Divider vertical={true} borderWidth="border-l-2" class="opacity-50 team2-divider"/>
            {/if}
            <span class="text-sm lg:text-2xl px-3 lg:px-5 py-1 font-mono block font-bold">
                {player.username}
            </span>
          {/each}
        </div>
      </div>
    </div>
  </div>
  <div>
    <slot name="action"/>
  </div>
</div>

<style lang="scss">
  .match-screen {
    background-image: url('$lib/assets/match-background.jpg');
    background-repeat: no-repeat;
    background-position: center center;
    background-size: 100%;
  }

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
    transform: skewX(20deg);
    margin-right: -20px;
    padding-right: 20px;

    .team2 {
      text-align: right;
      transform: skewX(-20deg);
    }
  }
</style>
