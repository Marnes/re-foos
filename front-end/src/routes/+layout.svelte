<script lang="ts">
    import '@brainandbones/skeleton/themes/theme-skeleton.css';
    import '@brainandbones/skeleton/styles/all.css';
    import '$src/app.postcss';

    import TopBar from '$src/components/layout/top-bar.svelte';
    import PlayerSpotlight from '$src/components/stats/player-spotlight.svelte';
    import AppRail from '$src/components/layout/app-rail.svelte';
    import Match from '$src/components/game/match.svelte';
    import Dialog from '$src/components/layout/dialog.svelte';

    import { AppShell, Drawer, Toast } from '@brainandbones/skeleton';
    import { MatchSettings } from '$src/models/constants';
    import { drawerStore } from '$src/stores/game-store';
    import type { PageData } from './$types';

    export let data: PageData;
</script>

<Drawer open={drawerStore} position="right">
  <Match
      players={data.players}
      maxScore={MatchSettings.MAX_SCORE}
  />
</Drawer>

<Dialog/>
<Toast background="bg-accent-500" variant="filled" duration={250}/>

<AppShell>
  <svelte:fragment slot="header">
    <TopBar/>
  </svelte:fragment>
  <svelte:fragment slot="sidebarLeft">
    <AppRail/>
  </svelte:fragment>
  <svelte:fragment slot="sidebarRight">
    <div class="card card-body h-full hidden xl:block">
      <PlayerSpotlight playerSpotlight={data.playerSpotlight}></PlayerSpotlight>
    </div>
  </svelte:fragment>
  <div class="card card-body h-full 2xl:flex place-content-center">
    <slot players={data.players}></slot>
  </div>
</AppShell>


