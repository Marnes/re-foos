<script lang="ts">
    import StatItem from '$src/components/stats/item/stat-item.svelte'
    import EloChange from '$src/components/stats/elo-change.svelte';
    import { Avatar, Divider } from '@brainandbones/skeleton';
    import { PlayerMin, PlayerSpotlight } from '$src/models/player/player.ts';
    import { formatElo } from '$src/lib/util/elo-util.js';
    import { humanDate } from '$src/lib/util/date-util.js';
    import { Team } from '$src/models/match/match';

    export let playerSpotlight: PlayerSpotlight;

    let player = playerSpotlight.player
    let match = playerSpotlight.match

    const getPlayer = (playerId: number): PlayerMin => {
        return match.players[playerId];
    }

    const getGameClass = (team: Team): string => {
        if (team.players.includes(player.id)) {
            if (team.winner === true)
                return 'text-positive';
            else if (team.loser === true)
                return 'text-negative';
        }

        return '';
    }

    $: winner = match.players[match.winner]
    $: loser = match.players[match.loser]

</script>

<div>
  <h3 class="font-bold text-surface-500 text-center mb-3">Player Spotlight</h3>
  <div class="card">
    <div class="card-body space-y-4">
      <h3>
        <div class="flex items-center space-x-4 relative">
          <Avatar initials={player.username[0]} />
          <span>{player.username}</span>
        </div>
      </h3>
    </div>
    <Divider/>
    <div class="p-3">
      <h6 class="font-bold text-surface-500 mb-3 text-center">Stats</h6>
      <div class="grid grid-cols-3 gap-4 mb-3 text-right">
        <StatItem title="Played" value="{player.played}"/>
        <StatItem title="Wins" value="{player.wins}"/>
        <StatItem title="Losses" value="{player.wins}"/>
      </div>
      <div class="grid grid-cols-3 gap-4 mb-3 text-right">
        <StatItem title="Elo" value="{formatElo(player.elo)}"/>
        <StatItem title="Highest Elo" value="{formatElo(player.highestElo)}"/>
        <StatItem title="Lowest Elo" value="{formatElo(player.lowestElo)}"/>
      </div>
      <div class="grid grid-cols-3 gap-4 mb-3 text-right">
        <div/>
        <StatItem title="Total Goals" value="{player.scoreFor}"/>
        <StatItem title="Total Goals Against" value={player.scoreAgainst}/>
      </div>
      <div class="grid grid-cols-3 gap-4 mb-3 text-right">
        <div/>
        <StatItem title="Current Winning Streak" value={player.currentWinningStreak}/>
        <StatItem title="Longest Winning Streak" value={player.longestWinningStreak}/>
      </div>
      <div class="grid grid-cols-3 gap-4 mb-3 text-right">
        <div/>
        <StatItem title="Current Losing Streak" value={player.currentLosingStreak}/>
        <StatItem title="Longest Losing Streak" value={player.longestLosingStreak}/>
      </div>
      <div class="w-full flex place-content-center">
        <Divider class="w-[90%] mt-3 mb-3"/>
      </div>
      <div class="py-3 mb-3">
        <h6 class="font-bold text-surface-500 text-center">Latest Match</h6>
        <div class="font-bold text-surface-700 text-center text-sm">
          {humanDate(match.createdDate)}
        </div>
      </div>
      <div class="grid grid-cols-3 gap-4 mb-3 text-right">
        <StatItem
            class={winner?.id === player.id ? 'text-positive' : ''}
            title="Winner"
            value={winner?.username}
        />
        <StatItem
            class={loser?.id === player.id ? 'text-negative' : ''}
            title="Loser"
            value={loser?.username}
        />
        <StatItem title="Elo Change" eloChange={match.playerStats[player.id].eloChange}/>
      </div>
      <div class="grid grid-cols-3 gap-4 mb-3 text-right">
        <div/>
        <StatItem title="Total Goals" value={match.playerStats[player.id].scoreFor}/>
        <StatItem title="Total Goals Against" value={match.playerStats[player.id].scoreAgainst}/>
      </div>
      <div class="pt-3">
        <div class="table-container">
          <table class="table table-hover">
            <tbody>
            {#each match.games as game}
              <tr>
                {#each game.teams[0].players as playerId}
                  <td class="{getGameClass(game.teams[0])}">
                    {getPlayer(playerId).username}
                  </td>
                {/each}
                {#each game.teams[0].scores as score}
                  <td class="text-center {getGameClass(game.teams[0])}">{score}</td>
                {/each}
                {#each game.teams[1].scores as score}
                  <td class="text-center {getGameClass(game.teams[1])}">{score}</td>
                {/each}
                {#each game.teams[1].players as playerId}
                  <td class="{getGameClass(game.teams[1])}">
                    {getPlayer(playerId).username}
                  </td>
                {/each}
                <td class="text-center">
                  <EloChange elo={game.playerStats[player.id].eloChange}/>
                </td>
              </tr>
            {/each}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</div>
