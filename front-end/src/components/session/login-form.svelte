<script lang='ts'>
    import { createEventDispatcher } from 'svelte';
    import { goto } from '$app/navigation';
    import { dialogStore } from "$src/stores/dialog-store";
    import { toastStore } from "@brainandbones/skeleton";
    import { postForm } from "$src/lib/utils";
    import _ from 'lodash';

    const dispatch = createEventDispatcher();

    let username = '';
    let password = '';
    let loginFailure = false;

    $: canSubmit = !_.isEmpty(username) && !_.isEmpty(password);

    async function handleSubmit() {
        const response = await postForm(this.action, new FormData(this));
        const result = await response.json();

        if (result.status === 200) {
            await goto('/', { invalidateAll: true, noScroll: true });
            $dialogStore = null;
            toastStore.trigger({ message: 'Successfully logged in', autohide: true, timeout: 3000 });
        } else {
            loginFailure = true;
        }
    }
</script>

<form method="POST" action="auth?/login" on:submit|preventDefault={handleSubmit}>
  <div class="space-y-5">
    <div class="space-y-2">
      <label for="username">
        <span>Username</span>
        <input name="username" type="text" id="username" minlength="2" required bind:value={username}/>
      </label>
    </div>
    <div class="space-y-2">
      <label for="password">
        <span>Password</span>
        <input name="password" type="password" id="password" minlength="2" required bind:value={password}/>
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
          disabled={!canSubmit}
      >
        Sign in
      </button>
    </div>
  </div>
</form>
