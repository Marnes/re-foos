<script lang="ts">
    import TopBar from '$src/components/layout/TopBar.svelte';
    import AppRail from '$src/components/layout/AppRail.svelte';
    import Match from '$src/components/game/match.svelte';
    import PlayerSpotlight from '$src/components/stats/PlayerSpotlight.svelte';
    import MainPage from '$src/components/layout/MainPage.svelte';

    import { AppShell, Drawer } from '@skeletonlabs/skeleton';
    import { MatchSettings } from '$src/models/constants';
    import { menuDrawerStore } from '$src/stores/menu-store';
    import { captureDrawerStore } from '$src/stores/game-store';
    import { page } from '$app/stores';

    import type { PageData } from '$src/$types';
    import { sessionStore } from '$src/stores/sessionStore.js';
    import { leagueStore } from '$src/stores/leagueStore.js';

    export let data: PageData;

    $: rails = [
        { title: 'Leaderboard', link: '/', icon: 'iconoir:leaderboard-star', selected: $page.route.id === '/(app)' },
    ]

    const captureGame = () => {
        $captureDrawerStore = true
    };

</script>

<Drawer open={captureDrawerStore} position="right">
  <Match
      players={data.players}
      minScore={MatchSettings.MIN_SCORE}
      maxScore={MatchSettings.MAX_SCORE}
  />
</Drawer>

<Drawer open={menuDrawerStore} position="left" width="w-24" class="lg:hidden">
  <AppRail rails={rails}/>
</Drawer>

<AppShell>
  <svelte:fragment slot="header">
    <TopBar>
      <svelte:fragment slot="actions">
        {#if $sessionStore?.user}
          <button
              class="hidden md:inline-flex btn bg-primary-500"
              disabled={$leagueStore.isClosed}
              on:click={captureGame}
          >
            Capture
          </button>
        {/if}
      </svelte:fragment>
    </TopBar>
  </svelte:fragment>
  <svelte:fragment slot="sidebarLeft">
    <div class="hidden lg:block h-full">
      <AppRail rails={rails}/>
    </div>
  </svelte:fragment>
  <MainPage>
    <slot players={data.players}></slot>

    {#if $sessionStore?.user}
      <div class="flex md:hidden h-14">
        <button
            class="btn bg-primary-500 absolute bottom-4 right-6"
            disabled={$leagueStore.isClosed}
            on:click={captureGame}
        >
          Capture
        </button>
      </div>
    {/if}

    <svelte:fragment slot="right-content">
      <PlayerSpotlight/>
    </svelte:fragment>
  </MainPage>
</AppShell>



