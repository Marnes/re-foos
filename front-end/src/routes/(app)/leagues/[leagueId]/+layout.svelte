<script lang="ts">
    import TopBar from '$src/components/layout/TopBar.svelte';
    import AppRail from '$src/components/layout/AppRail.svelte';
    import Match from '$src/components/game/Match.svelte';
    import PlayerSpotlight from '$src/components/stats/PlayerSpotlight.svelte';
    import MainPage from '$src/components/layout/MainPage.svelte';

    import { AppShell, Drawer, drawerStore } from '@skeletonlabs/skeleton';
    import { page } from '$app/stores';
    import { sessionStore } from '$src/stores/sessionStore.js';
    import { league, players } from '$src/stores/leagueStore.js';
    import type { PageData } from '$src/$types';

    export let data: PageData;

    $: rails = [
        { title: 'Leaderboard', link: '/', icon: 'iconoir:leaderboard-star', selected: $page.route.id === '/(app)' },
    ]

    const captureGame = () => {
        drawerStore.open({ id: 'match-capture', position: 'right' })
    };

    $: captureDisabled = $league.isClosed
    $: showCapture = $sessionStore?.user && $league.joined
</script>

<Drawer>
  {#if $drawerStore.id === 'match-capture'}
    <Match
        players={$players}
        league={$league}
    />
  {:else if $drawerStore.id === 'app-rail'}
    <AppRail rails={rails}/>
  {/if}
</Drawer>


<AppShell>
  <svelte:fragment slot="header">
    <TopBar>
      <svelte:fragment slot="actions">
        {#if showCapture}
          <button
              class="hidden md:inline-flex btn bg-primary-500"
              disabled={captureDisabled}
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

    {#if showCapture}
      <div class="flex md:hidden h-14">
        <button
            class="btn bg-primary-500 absolute bottom-4 right-6"
            disabled={captureDisabled}
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



