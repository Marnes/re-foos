<script lang="ts">
    import PlayerAvatar from '$src/components/player/PlayerAvatar.svelte';
    import StatItem from '$src/components/stats/item/StatItem.svelte';
    import { league } from '$src/stores/leagueStore'
    import { humanDate, humanDateWithTime, isoDate } from '$src/lib/util/dateUtil.js';
    import { Divider } from '@skeletonlabs/skeleton';
    import { getBestOf, getTeamComposition } from '$src/lib/util/match/leagueUtil';
    import { userNameSentence } from '$src/lib/util/stringUtil.js';
    import { PlayerMin } from '$src/models/player/player';
    import { first } from '$src/lib/util/objectUtil.js';
    import { formatElo } from '$src/lib/util/elo-util';
    import { Game } from '$src/models/match/match';
    import _ from 'lodash';

    const bestOf = getBestOf($league);
    const teamComposition = getTeamComposition($league);

    const match = $league.latestMatch

    const getPlayers = (playerIds: number[]): PlayerMin[] => {
        return playerIds.map(id => match?.players[id]);
    }

    const getPlayer = (playerId: number): PlayerMin => {
        return match?.players[playerId];
    }

    const getTotalGoals = (): number => {
        return _.sum(match.games.flatMap(game => game.teams).flatMap(team => team.scores));
    }

    const getGameEloChange = (game: Game): string => {
        let eloChange = formatElo(first(game.playerStats).eloChange);

        if (eloChange === 0) {
            return '0';
        }

        if (eloChange < 0) {
            eloChange = eloChange * -1;
        }

        return `± ${ eloChange }`
    }

</script>

<div class="spotlight-container h-full">
  <div class="text-center flex flex-col items-center">
    <span class="text-3xl font-bold text-surface-500">{$league.name}</span>
    <div class="mt-2">
      <span class="text-xl font-bold text-surface-700">Season {$league.season}</span>
    </div>
    {#if $league.leader}
      <div class="flex flex-col mt-10 items-center">
        <span class="text-2xl font-bold text-surface-500">Current Leader</span>
        <div class="mt-3">
          <PlayerAvatar player={$league.leader} width="w-24"/>
        </div>
        <div class="mt-2">
          <span class="text-xl font-bold text-surface-500">{$league.leader.username}</span>
        </div>
      </div>
    {/if}
    <div class="px-7 my-5 w-full">
      <Divider/>
    </div>
    <div class="w-full px-5">
      <div class="mb-10">
        <h6 class="font-bold text-surface-500 text-center">Created</h6>
        <div class="font-bold text-surface-700 text-center text-sm">
          {humanDate($league.createdDate)}
        </div>
      </div>
      <div class="grid grid-cols-3 gap-4 mb-3 text-right">
        <div/>
        <StatItem title="Start Date" value={isoDate($league.startDate)}/>
        <StatItem title="End Date" value={$league.endDate ? isoDate($league.endDate) : '-'}/>
      </div>
      <div class="grid grid-cols-3 gap-4 mb-3 text-right">
        <div/>
        <StatItem title="Players" value={$league.players}/>
        <StatItem title="Matches" value={$league.matches}/>
      </div>
      <div class="grid grid-cols-3 gap-4 mb-3 text-right">
        <StatItem title="Best of" value={bestOf}/>
        <StatItem title="Team Composition" value={teamComposition}/>
        <StatItem title="Max Score" value={$league.config.maxScore}/>
      </div>
    </div>
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
          title="Winner"
          value={userNameSentence(getPlayers(match.winners))}
      />
      <StatItem
          title="Loser"
          value={userNameSentence(getPlayers(match.losers))}
      />
      <StatItem title="Total Goals" value="{getTotalGoals()}"/>
    </div>
    <div class="pt-3">
      <div class="table-container">
        <table class="table table-hover">
          <tbody>
          {#each match.games as game}
            <tr>
              {#each game.teams[0].players as playerId}
                <td>
                  {getPlayer(playerId).username}
                </td>
              {/each}
              {#each game.teams[0].scores as score}
                <td class="text-center">{score}</td>
              {/each}
              {#each game.teams[1].scores as score}
                <td class="text-center">{score}</td>
              {/each}
              {#each game.teams[1].players as playerId}
                <td>
                  {getPlayer(playerId).username}
                </td>
              {/each}
              <td class="text-center">
                {getGameEloChange(game)}
              </td>
            </tr>
          {/each}
          </tbody>
        </table>
      </div>
    </div>
  {/if}
</div>

<style lang="scss">
  .spotlight-container {
    width: 34rem;
  }
</style>
