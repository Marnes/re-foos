<script lang="ts">
    import '@brainandbones/skeleton/themes/theme-skeleton.css';
    import '@brainandbones/skeleton/styles/all.css';
    import '$src/app.postcss';

    import TopBar from '$src/components/layout/TopBar.svelte'
    import AppRail from '$src/components/layout/AppRail.svelte';
    import PlayerProfile from '$src/components/profile/PlayerProfile.svelte';
    import MainPage from '$src/components/layout/MainPage.svelte';

    import { AppShell, Drawer } from '@brainandbones/skeleton';
    import { sessionStore } from '$src/stores/sessionStore';
    import { page } from '$app/stores';
    import { menuDrawerStore } from '$src/stores/menu-store.js';

    $: rails = [
        {
            title: 'Avatar',
            link: '/profile/avatar',
            icon: 'carbon:user-avatar',
            selected: $page.route.id === '/profile/avatar'
        },
        {
            title: 'Password',
            link: '/profile/password',
            icon: 'mdi:form-textbox-password',
            selected: $page.route.id === '/profile/password'
        },
    ]
</script>

<Drawer open={menuDrawerStore} position="left" width="w-24" class="lg:hidden">
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
