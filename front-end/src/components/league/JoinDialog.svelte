<script lang="ts">
    import type { League } from '$src/models/league/league';
    import { post } from '$src/lib/utils';
    import { goto, invalidateAll } from '$app/navigation';
    import { modalStore, toastStore } from '@skeletonlabs/skeleton';
    import { ToastMessage } from '$src/models/toastMessage';

    export let league: League;
    export let code: string;

    const join = async () => {
        const response = await post(`/leagues/${ league.id }/join?code=${ code }`)

        if (response.ok) {
            await invalidateAll();
            toastStore.trigger(ToastMessage.success(`Successfully joined ${ league.name }`))
            close();
        } else {
            toastStore.trigger(ToastMessage.failure(`Failed to join ${ league.name }`))
        }
    }

    const close = () => {
        goto(`/leagues/${ league.id }/`);
        modalStore.close();
    }

</script>

<div class="card-body">
  {#if league.joined}
    <div class="text-center mb-7">
      <h4>You already joined <strong>{league.name}</strong></h4>
    </div>
    <button class="btn bg-primary-500 md text-white w-full" on:click={close}>
      Close
    </button>
  {:else }
    <button class="btn bg-primary-500 btn-lg text-white w-full" on:click={join}>
      Join {league.name}
    </button>
  {/if}
</div>

