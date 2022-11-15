<script lang='ts'>
    import LoginForm from '$src/components/session/login-form.svelte'
    import PasswordChangeForm from '$src/components/session/password-change-form.svelte'
    import Icon from "@iconify/svelte";
    import { AppBar, menu, toastStore } from '@brainandbones/skeleton';
    import { Dialog } from "$src/models/dialog";
    import { drawerStore } from "$src/stores/game-store";
    import { dialogStore } from "$src/stores/dialog-store";
    import { sessionStore } from "$src/stores/session-store";
    import { del } from "$src/lib/utils";
    import { goto } from "$app/navigation";
    import _ from 'lodash';

    $: hasSession = !_.isNil($sessionStore);

    function showLogin() {
        $dialogStore = Dialog.create('Login to <strong>RE-Foos</strong>', LoginForm);
    }

    function showChangePassword() {
        $dialogStore = Dialog.create('Change your Password', PasswordChangeForm);
    }

    async function logout() {
        await del('auth/logout')
        await goto('/', { invalidateAll: true, noScroll: true });
        $sessionStore = null;
        toastStore.trigger({ message: 'Successfully logged out', autohide: true, timeout: 3000 });
    }

    const captureGame = () => {
        $drawerStore = true
    };
</script>

<AppBar class='navbar bg-transparent' padding='p-3'>
  <svelte:fragment slot='lead'>
    <a href="/" class="text-sm sm:text-lg md:text-3xl font-bold uppercase mr-4" title="Return to Homepage">Re-foos</a>
  </svelte:fragment>
  <svelte:fragment slot='trail'>
    {#if hasSession}
      <button class="btn bg-primary-500 btn-md text-white" on:click={captureGame}>Capture</button>
      <span class="relative">
        <button use:menu={{ menu: 'session' }} class="btn-icon profile-button">
              <span><Icon icon="carbon:user-avatar-filled" class="text-4xl"/></span>
        </button>
        <nav class="list-nav card p-4 w-64 shadow-xl" data-menu="session">
          <ul>
            <li><a on:click={showChangePassword}>Change Password</a></li>
            <li><a on:click={logout}>Logout</a></li>
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

<style lang="scss">
  .profile-button {
    padding: 0;
  }
</style>
