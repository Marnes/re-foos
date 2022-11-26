<script lang="ts">
    import '@brainandbones/skeleton/themes/theme-skeleton.css';
    import '@brainandbones/skeleton/styles/all.css';
    import '$src/app.postcss';

    import TopBar from '$src/components/layout/TopBar.svelte';
    import AppRail from '$src/components/layout/AppRail.svelte';
    import Match from '$src/components/game/match.svelte';

    import { AppShell, Drawer } from '@brainandbones/skeleton';
    import { MatchSettings } from '$src/models/constants';
    import { menuDrawerStore } from "$src/stores/menu-store";
    import { captureDrawerStore } from '$src/stores/game-store';
    import { page } from '$app/stores';

    import type { PageData } from '$src/$types';
    import PlayerSpotlight from '$src/components/stats/PlayerSpotlight.svelte';

    export let data: PageData;

    const rails = [
        { title: 'Leaderboard', link: '/', icon: 'iconoir:leaderboard-star', selected: $page.route.id === '(app)' },
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
        <button class="btn bg-primary-500 btn-md text-white" on:click={captureGame}>Capture</button>
      </svelte:fragment>
    </TopBar>
  </svelte:fragment>
  <svelte:fragment slot="sidebarLeft">
    <div class="hidden lg:block">
      <AppRail rails={rails}/>
    </div>
  </svelte:fragment>
  <div
      class="sidebar-right h-full hidden xl:block"
      slot="sidebarRight"
  >
    <div class="card card-body h-full">
      <PlayerSpotlight/>
    </div>
  </div>
  <div class="card card-body h-full 2xl:flex place-content-center">
    <slot players={data.players}></slot>
  </div>
</AppShell>


