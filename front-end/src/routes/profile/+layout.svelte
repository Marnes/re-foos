<script lang="ts">
    import TopBar from '$src/components/layout/TopBar.svelte'
    import AppRail from '$src/components/layout/AppRail.svelte';
    import PlayerProfile from '$src/components/profile/PlayerProfile.svelte';
    import MainPage from '$src/components/layout/MainPage.svelte';

    import { AppShell, Drawer } from '@skeletonlabs/skeleton';
    import { sessionStore } from '$src/stores/sessionStore';
    import { page } from '$app/stores';
    import { createRail } from '$src/lib/util/railUtil';

    $: rails = [
        createRail('Avatar', `/profile/avatar`, 'carbon:user-avatar', $page),
        createRail('Password', `/profile/password`, 'mdi:form-textbox-password', $page)
    ]
</script>

<Drawer position="left" width="w-24" class="lg:hidden">
  <AppRail rails={rails}/>
</Drawer>

<AppShell>
  <svelte:fragment slot="header">
    <TopBar/>
  </svelte:fragment>
  <svelte:fragment slot="sidebarLeft">
    <div class="hidden lg:block h-full">
      <AppRail rails={rails}/>
    </div>
  </svelte:fragment>
  <MainPage>
    <slot/>

    <PlayerProfile slot="right-content" player={$sessionStore.user}/>
  </MainPage>
</AppShell>
