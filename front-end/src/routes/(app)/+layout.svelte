<script lang="ts">
    import '@brainandbones/skeleton/themes/theme-skeleton.css';
    import '@brainandbones/skeleton/styles/all.css';
    import '$src/app.postcss';

    import TopBar from '$src/components/layout/TopBar.svelte';
    import AppRail from '$src/components/layout/AppRail.svelte';
    import Match from '$src/components/game/match.svelte';
    import PlayerSpotlight from '$src/components/stats/PlayerSpotlight.svelte';
    import MainPage from '$src/components/layout/MainPage.svelte';
    import Icon from '@iconify/svelte';

    import { AppShell, Drawer } from '@brainandbones/skeleton';
    import { MatchSettings } from '$src/models/constants';
    import { menuDrawerStore } from '$src/stores/menu-store';
    import { captureDrawerStore } from '$src/stores/game-store';
    import { page } from '$app/stores';

    import type { PageData } from '$src/$types';

    export let data: PageData;

    const rails = [
        { title: 'Leaderboard', link: '/', icon: 'iconoir:leaderboard-star', selected: $page.route.id === '(app)' },
    ]

    const captureGame = () => {
        console.log('ploooop');
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
        <button class="md:hidden btn-icon btn-filled-primary" on:click={captureGame}>
          <span>
            <Icon icon="system-uicons:capture" class="text-2xl"/>
          </span>
        </button>
        <button class="hidden md:block btn bg-primary-500 btn-md text-white" on:click={captureGame}>Capture</button>
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

    <PlayerSpotlight slot="right-content"/>
  </MainPage>
</AppShell>


