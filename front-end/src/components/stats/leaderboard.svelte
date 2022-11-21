<script lang='ts'>
    import Icon from '@iconify/svelte';
    import EloChange from '$src/components/stats/elo-change.svelte';
    import type { Player } from '$src/models/player/player';
    import { formatElo } from '$src/lib/util/elo-util';

    export let players: Player[];

    const shouldDisplayIcon = (rank: Number): Boolean => {
        return rank <= 3;
    }

    const getIcon = (rank: Number): String => {
        if (rank <= 3) {
            return 'mdi:trophy';
        }

        return 'mdi:silverware-spoon';
    }

    const getClasses = (rank: Number): String => {
        if (rank === 1) {
            return 'text-gold-700 text-2xl'
        }

        if (rank === 2) {
            return 'text-silver-700 text-xl'
        }

        if (rank === 3) {
            return 'text-bronze-600 text-lg'
        }
    }
</script>

<div class="table-container">
  <table class="table">
    <thead>
    <tr class>
      <th class="hidden sm:table-cell sm:w-1"></th>
      <th>Player</th>
      <th class="sm:w-1 text-center">#</th>
      <th class="sm:w-1 text-center">W</th>
      <th class="sm:w-1 text-center">L</th>
      <th class="text-center" colspan="2">Elo</th>
      <th class="hidden sm:table-cell sm:w-1 text-center"></th>
    </tr>
    </thead>
    <tbody>
    {#each players as player, index}
      <tr class="rank-{player.rank}">
        <td class="hidden sm:table-cell">{player.rank}</td>
        <td>{player.username}</td>
        <td class="text-center">{player.played}</td>
        <td class="text-center">{player.wins}</td>
        <td class="text-center">{player.losses}</td>
        <td class="sm:w-2 text-center">{formatElo(player.elo)}</td>
        <td class="sm:w-2 text-center">
          <EloChange elo={player.eloChange}/>
        </td>
        <td class="hidden sm:table-cell text-center">
          {#if shouldDisplayIcon(player.rank)}
            <div style="display: inline-block">
              <Icon class="{getClasses(player.rank)}" icon="{getIcon(player.rank)}"/>
            </div>
          {/if}
        </td>
      </tr>
    {/each}
    </tbody>
  </table>
</div>

<style lang='scss'>
  .rank-1 {
    border: 4px double #B18601;
  }
</style>
