<script lang="ts">
    import Icon from '@iconify/svelte';
    import LinkDialog from '$src/components/league/LinkDialog.svelte';
    import { menu, modalStore, toastStore } from '@skeletonlabs/skeleton';
    import { League, LeagueSummary } from '$src/models/league/league';
    import { isoDate } from '$src/lib/util/dateUtil.js';
    import { sessionStore } from '$src/stores/sessionStore.js';
    import { Modal } from '$src/models/modal';
    import { ToastMessage } from '$src/models/toastMessage';
    import { post } from '$src/lib/utils';
    import { invalidateAll } from '$app/navigation';
    import { LeagueType } from '../../models/league/league.js';

    export let leagues: LeagueSummary[]

    const getJoinLink = (league: League) => {
        return () => {
            modalStore.trigger(Modal.component(`Join link for <strong>${ league.name }</strong>`, LinkDialog, { league }));
        }
    }

    const closeLeague = (league: League) => {
        return () => {
            modalStore.trigger(Modal.confirm(
                `Close <strong>${ league.name }</strong>`,
                `Are you sure you want to close <strong>${ league.name }</strong>? Once closed, no scores can be captured and players will not be able to join?`,
                async () => {
                    const response = await post(`/leagues/${ league.id }/close`, {});
                    if (response.ok) {
                        await invalidateAll();
                        toastStore.trigger(ToastMessage.success('League closed successfully'))
                    }
                }
            ))
        }
    }
</script>

<div class="table-container card">
  <table class="table table-hover">
    <thead>
    <tr>
      <th>Name</th>
      <th style="width: 250px">Type</th>
      <th style="width: 250px" class="text-center">Start Date</th>
      <th style="width: 250px" class="text-center">End Date</th>
      <th style="width: 300px" class="text-center">Team Composition</th>
      <th style="width: 150px" class="text-center">Closed</th>
      {#if $sessionStore?.user?.admin }
        <th style="width: 50px" class="text-center"></th>
      {/if}
    </tr>
    </thead>
    <tbody>
    {#each leagues as league}
      <a href="/leagues/{league.id}/leaderboard" class="table-row">
        <td>{league.name}</td>
        <td>{LeagueType.humanName(league.type)}</td>
        <td class="text-center">{isoDate(league.startDate)}</td>
        <td class="text-center">{league.endDate ? isoDate(league.endDate) : '-'}</td>
        <td class="text-center">{league.teamComposition}</td>
        <td class="text-center">{league.isClosed ? 'Yes' : 'No' }</td>
        {#if $sessionStore?.user?.admin }
          <td style="width: 30px" class="text-center">
            <button use:menu={{ menu: `leagueOptions${league.id}` }} on:click|preventDefault|stopPropagation>
              <Icon icon="ic:round-more-vert" class="text-2xl"/>
            </button>
            <nav class="list-nav card p-4 w-64 shadow-xl" data-menu="{`leagueOptions${league.id}`}">
              <ul>
                <li>
                  <button class="option w-full" on:click|preventDefault|stopPropagation={getJoinLink(league)}>Link</button>
                </li>
                {#if !league.endDate}
                  <li>
                    <button class="option w-full" on:click|preventDefault|stopPropagation={closeLeague(league)}>Close</button>
                  </li>
                {/if}
              </ul>
            </nav>
          </td>
        {/if}
      </a>
    {/each}
    </tbody>
  </table>
</div>

