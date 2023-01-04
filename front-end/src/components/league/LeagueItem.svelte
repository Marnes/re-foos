<script lang="ts">
    import PlayerAvatar from '$src/components/player/PlayerAvatar.svelte';
    import LinkDialog from '$src/components/league/LinkDialog.svelte';
    import Icon from '@iconify/svelte';
    import { League } from '$src/models/league/league';
    import { LeagueType } from '$src/models/league/league.js';
    import { ModalType } from '$src/lib/util/skeletonUtil';
    import { Modal } from '$src/models/modal';
    import { isoDate } from '$src/lib/util/dateUtil.js';
    import { menu, type ModalSettings, modalStore } from '@skeletonlabs/skeleton';
    import { post } from '$src/lib/utils';
    import { invalidateAll } from '$app/navigation';
    import { session } from '$src/stores/sessionStore.js';
    import _ from 'lodash';

    export let league: League;
    const menuNum = _.uniqueId('league_');

    const getJoinLink = (league: League) => {
        return () => {
            modalStore.trigger(Modal.component(`Join link for <strong>${ league.name }</strong>`, LinkDialog, { league }));
        }
    }

    const startNewSeason = (league: League) => {
        return () => {
            const confirmValues: ModalSettings = {
                type: ModalType.COFIRM,
                title: `Start new Season for <strong>${ league.name }</strong>`,
                body: `Are you sure you want to start a new season for <strong>${ league.name }</strong>? ` +
                    `The current season will end at the end of today, and the new season will start tomorrow.`,
                response: async (r: boolean) => {
                    if (r) {
                        await post(`/leagues/${league.id}/season`);
                        await invalidateAll();
                    }
                },
                buttonTextCancel: 'Cancel',
                buttonTextConfirm: 'Create Season',
            };

            modalStore.trigger(confirmValues);
        }
    }

</script>

<a class="card overflow-hidden flex flex-col" href="/leagues/{league.id}/leaderboard">
  <header class="p-4 space-y-4">
    <div class="flex flex-row justify-between">
      <h3>{league.name}</h3>
      {#if $session?.user?.admin}
      <span class="relative">
        <button use:menu={{ menu: `leagueOptions${menuNum}`, fixed: true }} on:click|preventDefault|stopPropagation>
          <Icon icon="ic:round-more-vert" class="text-2xl"/>
        </button>
        <nav class="list-nav card p-4 w-64 shadow-xl absolute top-2 right-2" data-menu="{`leagueOptions${menuNum}`}">
          <ul>
            <li>
              <button class="option w-full" on:click|preventDefault|stopPropagation={getJoinLink(league)}>Link</button>
            </li>
            <li>
              <button class="option w-full" on:click|preventDefault|stopPropagation={startNewSeason(league)}>Start New Season</button>
            </li>
          </ul>
        </nav>
      </span>
      {/if}
    </div>
    <span class="text-sm text-surface-500">{LeagueType.humanName(league.config.type)}</span>
    <span class="block text-md mt-2 text-surface-500">Players: {league.players}</span>
  </header>
  <hr/>
  <div class="p-4 space-y-4 flex-1">
    <h4 class="text-primary-500">Season {league.season}</h4>
    {#if league.leader}
      <div class="flex flex-col mt-3 items-center">
        <span class="text-md text-surface-500 text-bold">Current Leader</span>
        <PlayerAvatar class="mt-3 hidden lg:flex" player={league.leader}/>
        <span class="block mt-1">{league.leader.username}</span>
      </div>
    {/if}
    <div class="flex flex-col">
      <div class="grid grid-cols-2">
        <div>
          <span class="text-sm text-surface-500/70">Start Date</span>
          <br/>
          {isoDate(league.startDate)}
        </div>
        <div>
          <span class="text-sm text-surface-500/70">End Date</span>
          <br/>
          {league.endDate ? isoDate(league.endDate) : '-'}
        </div>
      </div>
      <div class="grid grid-cols-2 mt-3">
        <div>
          <span class="text-sm text-surface-500/70">Matches</span>
          <br/>
          {league.matches || '-'}
        </div>
      </div>
    </div>
  </div>
  <hr/>
  <footer class="p-4 flex space-x-4">
    <div class="flex flex-col w-full">
      <div>
        <span class="text-sm text-surface-500/70">Created by</span>
      </div>

      <div class="flex flex-row items-end justify-between">
        <h6 class="font-bold">{league.createdBy.username}</h6>
        <small>On {isoDate(league.createdDate)}</small>
      </div>
    </div>
  </footer>
</a>
