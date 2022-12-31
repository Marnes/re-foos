<script lang='ts'>
    import LoginForm from '$src/components/session/LoginForm.svelte'
    import PlayerAvatar from '$src/components/player/PlayerAvatar.svelte'
    import Icon from '@iconify/svelte';
    import { AppBar, Divider, drawerStore, menu, modalStore, toastStore } from '@skeletonlabs/skeleton';
    import { sessionStore } from '$src/stores/sessionStore';
    import { del } from '$src/lib/utils';
    import { invalidateAll } from '$app/navigation';
    import { Modal } from '$src/models/modal';

    export let hasDrawer = true

    const menuDrawerOpen: any = () => {
        drawerStore.open({ id: 'app-rail', width: 'w-24' })
    };

    function showLogin() {
        modalStore.trigger(Modal.component('Login to <strong>ELO-Musk</strong>', LoginForm));
    }

    async function logout() {
        await del('/profile/auth/session')
        await invalidateAll();
        $sessionStore = null;
        toastStore.trigger({ message: 'Successfully logged out', autohide: true, timeout: 3000 });
    }
</script>

<AppBar class='navbar bg-transparent' padding='p-3'>
  <svelte:fragment slot='lead'>
    {#if hasDrawer}
      <button class="lg:!hidden btn btn-sm" on:click={menuDrawerOpen}>
        <Icon icon="material-symbols:menu" class="text-2xl"/>
      </button>
    {/if}
    <a href="/" class="text-xl font-bold uppercase mr-4" title="Return to Homepage">ELO-Musk</a>
    <Divider borderWidth="border-l" vertical={true} class="mr-4 ml-2 hidden lg:block"/>
    <a href="/" class="hidden lg:block mr-3">Home</a>
    <a href="/leagues">Leagues</a>
  </svelte:fragment>
  <svelte:fragment slot='trail'>
    <slot name="actions"/>
    {#if $sessionStore}
      <span class="relative">
        <span use:menu={{ menu: 'session' }} class="cursor-pointer">
          <PlayerAvatar player={$sessionStore.user} width="w-12"/>
        </span>
        <nav class="list-nav card p-4 w-64 shadow-xl" data-menu="session">
          <ul>
            <li><a href="/profile/avatar">Profile</a></li>
            <li><a href="#" on:click={logout}>Logout</a></li>
          </ul>
        </nav>
      </span>
    {:else }
      <button on:click={showLogin} class="btn-icon profile-button">
        <span><Icon icon="carbon:user-avatar-filled" class="text-4xl"/></span>
      </button>
    {/if}
  </svelte:fragment>
</AppBar>
