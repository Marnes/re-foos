<script lang="ts">
    import StatItem from '$src/components/stats/item/StatItem.svelte'
    import EloChange from '$src/components/stats/EloChange.svelte';
    import PlayerProfile from '$src/components/profile/PlayerProfile.svelte';
    import { Divider } from '@skeletonlabs/skeleton';
    import { Player, PlayerMin } from '$src/models/player/player.ts';
    import { formatElo } from '$src/lib/util/elo-util.js';
    import { humanDateWithTime } from '$src/lib/util/dateUtil.js';
    import { Match, Team } from '$src/models/match/match';
    import { get } from '$src/lib/utils';
    import { onDestroy, onMount } from 'svelte';
    import { league, playerSpotlight } from '$src/stores/leagueStore';
    import { userNameSentence } from '$src/lib/util/stringUtil.js';
    import { page } from '$app/stores';
    import { seasonPath } from '$src/lib/util/match/leagueUtil';

    let player: Player
    let match: Match

    let spotlightUnsubscribe;

    onMount(() => {
        spotlightUnsubscribe = playerSpotlight.subscribe(async p => {
            if (p) {
                const season = $page.url.searchParams.get('season');
                const response = await get(seasonPath(`/leagues/${ $league.id }/players/${ p.id }/spotlight`, season))
                const playerSpotlight = await response.json();

                player = playerSpotlight.player
                match = playerSpotlight.match
            }
        })
    })

    onDestroy(() => {
        if (spotlightUnsubscribe) spotlightUnsubscribe();
    })

    const getPlayer = (playerId: number): PlayerMin => {
        return match?.players[playerId];
    }

    const getPlayers = (playerIds: number[]): PlayerMin[] => {
        return playerIds.map(id => match?.players[id]);
    }

    const getGameClass = (team: Team): string => {
        if (team?.players.includes(player.id)) {
            if (team.winner === true)
                return 'text-positive';
            else if (team.loser === true)
                return 'text-negative';
        }

        return '';
    }


</script>

<div class="spotlight-container h-full">
  {#if player}
    <div class="h-full" style="width: 34rem">
      <PlayerProfile player={player}>
        <h6 class="font-bold text-surface-500 mb-3 text-center">Stats</h6>
        <div class="grid grid-cols-3 gap-4 mb-3 text-right">
          <StatItem title="Played" value="{player.played}"/>
          <StatItem title="Wins" value="{player.wins}"/>
          <StatItem title="Losses" value="{player.losses}"/>
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
        {#if match}
          <div class="pl-7 pr-7 mt-5 mb-5">
            <Divider/>
          </div>
          <div class="py-3 mb-3">
            <h6 class="font-bold text-surface-500 text-center">Latest Match</h6>
            <div class="font-bold text-surface-700 text-center text-sm">
              {humanDateWithTime(match.createdDate)}
            </div>
          </div>
          <div class="grid grid-cols-3 gap-4 mb-3 text-right">
            <StatItem
                class={match.winners.includes(player.id) ? 'text-positive' : ''}
                title="Winner"
                value={userNameSentence(getPlayers(match.winners))}
            />
            <StatItem
                class={match.losers.includes(player.id) ? 'text-negative' : ''}
                title="Loser"
                value={userNameSentence(getPlayers(match.losers))}
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
        {/if}
      </PlayerProfile>
    </div>
  {/if}
</div>

<style lang="scss">
  .spotlight-container {
    width: 34rem;
  }
</style>
