<script lang="ts">
    import '@brainandbones/skeleton/themes/theme-skeleton.css';
    import '@brainandbones/skeleton/styles/all.css';
    import '$src/app.postcss';

    import TopBar from '$src/components/layout/TopBar.svelte'
    import AppRail from '$src/components/layout/AppRail.svelte';
    import PlayerProfile from '$src/components/profile/PlayerProfile.svelte';

    import { AppShell } from '@brainandbones/skeleton';
    import { sessionStore } from '$src/stores/sessionStore';
    import { page } from '$app/stores';

    const rails = [
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

<AppShell>
  <svelte:fragment slot="header">
    <TopBar/>
  </svelte:fragment>
  <svelte:fragment slot="sidebarLeft">
    <AppRail rails={rails}/>
  </svelte:fragment>
  <div
      class="sidebar-right h-full hidden xl:block"
      slot="sidebarRight"
  >
    <div class="card card-body h-full">
      <PlayerProfile user={$sessionStore.user}/>
    </div>
  </div>
  <div class="card card-body h-full">
    <slot/>
  </div>
</AppShell>
