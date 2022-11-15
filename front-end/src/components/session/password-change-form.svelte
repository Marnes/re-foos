<script lang='ts'>
    import { goto } from '$app/navigation';
    import { dialogStore } from "$src/stores/dialog-store";
    import { toastStore } from "@brainandbones/skeleton";
    import { post } from "$src/lib/utils";
    import { KeyCodes } from "$src/models/key-codes";
    import _ from 'lodash';

    let oldPassword = '';
    let newPassword = '';
    let confirmPassword = '';
    let changeFailure = false;

    let elm;

    $: canSubmit = !_.isEmpty(oldPassword) && !_.isEmpty(newPassword) && !_.isEmpty(confirmPassword) && newPassword === confirmPassword;

    dialogStore.subscribe(value => {
        if (value) {
            setTimeout(() => {
                elm.focus();
            }, 200)
        }
    });

    const changePassword = async () => {
        const response = await post('auth/changePassword', { oldPassword, newPassword });

        if (response.status === 200) {
            await goto('/', { invalidateAll: true, noScroll: true });
            $dialogStore = null;
            toastStore.trigger({ message: 'Successfully changed password.', autohide: true, timeout: 3000 });
        } else {
            changeFailure = true;
        }
    }

    const onKeyPress = e => {
        if (e.keyCode === KeyCodes.ENTER && canSubmit) {
            changePassword();
        }
    };
</script>

<div class="space-y-5">
  <div class="space-y-2">
    <label for="oldPassword">
      <span>Old Password</span>
      <input
          required
          id="oldPassword"
          name="oldPassword"
          type="password"
          minlength="6"
          bind:this={elm}
          bind:value={oldPassword}
          on:keydown={onKeyPress}
      />
    </label>
  </div>
  <div class="space-y-2">
    <label for="newPassword">
      <span>New Password</span>
      <input
          required
          id="newPassword"
          name="newPassword"
          type="password"
          minlength="6"
          bind:value={newPassword}
          on:keydown={onKeyPress}
      />
    </label>
  </div>
  <div class="space-y-2">
    <label for="confirmPassword">
      <span>Confirm Password</span>
      <input
          required
          id="confirmPassword"
          name="confirmPassword"
          type="password"
          minlength="6"
          bind:value={confirmPassword}
          on:keydown={onKeyPress}
      />
    </label>
  </div>
  {#if changeFailure}
    <div class='text-center text-warning-500'>
      Could not change password. Please try again.
    </div>
  {/if}
  <div>
    <button
        class="btn bg-primary-500 btn-lg text-white w-full"
        disabled={!canSubmit}
        on:click={changePassword}
    >
      Change Password
    </button>
  </div>
</div>
