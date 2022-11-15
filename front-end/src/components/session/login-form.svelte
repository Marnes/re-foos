<script lang='ts'>
    import { goto } from '$app/navigation';
    import { dialogStore } from "$src/stores/dialog-store";
    import { toastStore } from "@brainandbones/skeleton";
    import { post } from "$src/lib/utils";
    import { KeyCodes } from "$src/models/key-codes";
    import _ from 'lodash';

    let username = '';
    let password = '';
    let loginFailure = false;

    let elm;

    $: canSubmit = !_.isEmpty(username) && !_.isEmpty(password);

    dialogStore.subscribe(value => {
        if (value) {
            setTimeout(() => {
                elm.focus();
            }, 200)
        }
    });

    const login = async () => {
        const response = await post('auth/login', { username, password });

        if (response.status === 200) {
            await goto('/', { invalidateAll: true, noScroll: true });
            $dialogStore = null;
            toastStore.trigger({ message: 'Successfully logged in', autohide: true, timeout: 3000 });
        } else {
            loginFailure = true;
        }
    }

    const onKeyPress = e => {
        if (e.keyCode === KeyCodes.ENTER && canSubmit) {
            login();
        }
    };
</script>


<div class="space-y-5">
  <div class="space-y-2">
    <label for="username">
      <span>Username</span>
      <input
          required
          id="username"
          name="username"
          type="text"
          minlength="2"
          bind:this={elm}
          bind:value={username}
          on:keydown={onKeyPress}
      />
    </label>
  </div>
  <div class="space-y-2">
    <label for="password">
      <span>Password</span>
      <input
          required
          id="password"
          name="password"
          type="password"
          minlength="5"
          bind:value={password}
          on:keydown={onKeyPress}
      />
    </label>
  </div>
  {#if loginFailure}
    <div class='text-center text-warning-500'>
      Could not sign into RE-Foos. Please try again.
    </div>
  {/if}
  <div>
    <button
        class="btn bg-primary-500 btn-lg text-white w-full"
        on:click={login}
        disabled={!canSubmit}
    >
      Sign in
    </button>
  </div>
</div>
