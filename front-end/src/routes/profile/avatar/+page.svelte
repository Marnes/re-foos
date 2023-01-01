<script lang="ts">
    import AvatarSelect from '$src/components/profile/avatar/AvatarSelect.svelte';
    import { session } from '$src/stores/sessionStore';
    import { put } from '$src/lib/utils';
    import { invalidateAll } from '$app/navigation';
    import { toastStore } from '@skeletonlabs/skeleton';
    import { ToastMessage } from '$src/models/toastMessage';

    export let data;

    const setAvatar = async (avatar: CustomEvent) => {
        const response = await put(`/profile/avatar`, avatar.detail);

        if (response.ok) {
            await invalidateAll()
            toastStore.trigger(ToastMessage.success("Avatar updated"));
        }
    }
</script>

<AvatarSelect
    avatars={data.avatars}
    bind:value={$session.user.avatar}
    on:change={setAvatar}
/>
