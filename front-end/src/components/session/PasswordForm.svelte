<script lang='ts'>
    import { toastStore } from '@skeletonlabs/skeleton';
    import { put } from '$src/lib/utils';
    import { KeyCodes } from '$src/models/key-codes';
    import { invalidateAll } from '$app/navigation';
    import _ from 'lodash';

    let oldPassword = '';
    let newPassword = '';
    let confirmPassword = '';
    let changeFailure = false;

    let elm;

    $: canSubmit = !_.isEmpty(oldPassword) && !_.isEmpty(newPassword) && !_.isEmpty(confirmPassword) && newPassword === confirmPassword;

    const resetForm = () => {
        oldPassword = '';
        newPassword = '';
        confirmPassword = '';
        changeFailure = false;
        elm.focus();
    }

    const changePassword = async () => {
        const response = await put('/profile/auth/password', { oldPassword, newPassword });

        if (response.status === 200) {
            await invalidateAll()
            resetForm();
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

<div class="card-body grid grid-cols-1 md:grid-cols-2 gap-4">
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
  <div class="hidden md:block"></div>
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
  {#if changeFailure}
    <div class='col-span-1 md:col-span-2 text-center text-warning-500'>
      Could not change password. Please try again.
    </div>
  {/if}
  <div class="col-span-1 md:col-span-2 mt-3">
    <button
        class="btn bg-primary-500 btn-lg text-white w-full md:w-auto"
        on:click={changePassword}
    >
      Change Password
    </button>
  </div>
</div>
