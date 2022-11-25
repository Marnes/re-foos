<script lang='ts'>
    import { dialogStore } from '$src/stores/dialogStore';
    import { toastStore } from '@brainandbones/skeleton';
    import { post } from '$src/lib/utils';
    import { KeyCodes } from '$src/models/key-codes';
    import { invalidateAll } from '$app/navigation';
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
        const response = await post('/profile/auth/session', { username, password });

        if (response.status === 200) {
            await invalidateAll();
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


<div class="card-body grid grid-cols-1 gap-4">
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
  {#if loginFailure}
    <div class='text-center text-warning-500'>
      Could not sign into RE-Foos. Please try again.
    </div>
  {/if}
  <button
      class="btn bg-primary-500 btn-lg text-white w-full"
      on:click={login}
      disabled={!canSubmit}
  >
    Sign in
  </button>
</div>
