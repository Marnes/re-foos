<script lang='ts'>
    import Icon from '@iconify/svelte';
    import EloChange from '$src/components/stats/EloChange.svelte';
    import PlayerAvatar from '$src/components/player/PlayerAvatar.svelte'
    import type { Player } from '$src/models/player/player';
    import { formatElo } from '$src/lib/util/elo-util';
    import { createEventDispatcher } from 'svelte';
    import _ from 'lodash';

    const dispatch = createEventDispatcher();

    export let players: Player[];

    $: sortedPlayers = _.sortBy(players.filter(player => player.rank), player => player.rank)

    let selected: Player;

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

    const select = (player: Player) => {
        return async () => {
            selected = player;
            dispatch('selected', player);
        }
    }
</script>

<div class="table-container sticky-header">
  <table class="table leaderboard">
    <thead>
    <tr>
      <th class="w-1"></th>
      <th class="hidden sm:table-cell w-1 text-center"></th>
      <th>Player</th>
      <th class="sm:w-1 text-center">#</th>
      <th class="sm:w-1 text-center">W</th>
      <th class="sm:w-1 text-center">L</th>
      <th class="text-center" colspan="2">Elo</th>
      <th class="hidden sm:table-cell w-1 text-center"></th>
    </tr>
    </thead>
    <tbody>
    {#each sortedPlayers as player}
      <tr
          class="rank-{player.rank}"
          class:table-row-checked={selected === player}
          on:click={select(player)}
      >
        <td>{player.rank}</td>
        <td class="hidden sm:table-cell text-center">
          <PlayerAvatar player={player} width="w-10"/>
        </td>
        <td>{player.username}</td>
        <td class="text-center">{player.played}</td>
        <td class="text-center">{player.wins}</td>
        <td class="text-center">{player.losses}</td>
        <td class="text-center w-1">{formatElo(player.elo)}</td>
        <td class="hidden sm:table-cell text-center w-1">
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
  .leaderboard {
    .rank-1 {
      border: 4px double #B18601;
    }

    tr {
      cursor: pointer;
    }
  }
</style>
