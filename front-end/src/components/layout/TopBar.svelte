<script lang='ts'>
    import LoginForm from '$src/components/session/LoginForm.svelte'
    import PlayerAvatar from '$src/components/player/PlayerAvatar.svelte'
    import Icon from '@iconify/svelte';
    import { AppBar, Divider, menu, toastStore } from '@brainandbones/skeleton';
    import { menuDrawerStore } from "$src/stores/menu-store";
    import { dialogStore } from '$src/stores/dialogStore';
    import { sessionStore } from '$src/stores/sessionStore';
    import { del } from '$src/lib/utils';
    import { goto } from '$app/navigation';
    import { Dialog } from '$src/models/dialog';

    const menuDrawerOpen: any = () => {
      $menuDrawerStore = true
    };

    function showLogin() {
        $dialogStore = Dialog.create('Login to <strong>ELO-Musk</strong>', LoginForm);
    }

    async function logout() {
        await del('/profile/auth/session')
        await goto('/', { invalidateAll: true, noScroll: true });
        $sessionStore = null;
        toastStore.trigger({ message: 'Successfully logged out', autohide: true, timeout: 3000 });
    }
</script>

<AppBar class='navbar bg-transparent' padding='p-3'>
  <svelte:fragment slot='lead'>
    <button class="lg:!hidden btn btn-sm" on:click={menuDrawerOpen}>
      <Icon icon="material-symbols:menu" class="text-2xl" />
    </button>
    <a href="/" class="text-xl font-bold uppercase mr-4" title="Return to Homepage">ELO-Musk</a>
    <Divider borderWidth="border-l" vertical={true} class="mr-4 ml-2 hidden lg:block"/>
    <a href="/" class="hidden lg:block">Home</a>
  </svelte:fragment>
  <svelte:fragment slot='trail'>
    {#if $sessionStore}
      <slot name="actions"/>
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
