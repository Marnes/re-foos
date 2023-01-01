<script lang="ts">
    import PlayerAvatar from '$src/components/player/PlayerAvatar.svelte';
    import StatItem from '$src/components/stats/item/StatItem.svelte';
    import { league } from '$src/stores/leagueStore'
    import { isoDate, humanDate } from '$src/lib/util/dateUtil.js';
    import { Divider } from '@skeletonlabs/skeleton';
    import { getBestOf, getTeamComposition } from '$src/lib/util/match/leagueUtil';

    const bestOf = getBestOf($league);
    const teamComposition = getTeamComposition($league);
</script>

<div class="spotlight-container h-full">
  <div class="text-center flex flex-col items-center">
    <span class="text-3xl font-bold text-surface-500">{$league.name}</span>
    <div class="mt-2">
      <span class="text-xl font-bold text-surface-700">Season {$league.season}</span>
    </div>
    <div class="flex flex-col mt-10 items-center">
      <span class="text-2xl font-bold text-surface-500">Current Leader</span>
      <div class="mt-3">
        <PlayerAvatar player={$league.leader} width="w-24"/>
      </div>
      <div class="mt-2">
        <span class="text-xl font-bold text-surface-500">{$league.leader.username}</span>
      </div>
    </div>
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
</div>

<style lang="scss">
  .spotlight-container {
    width: 34rem;
  }
</style>
