<script lang="ts">
    import Icon from '@iconify/svelte';
    import { clipboard, toastStore, tooltip } from '@skeletonlabs/skeleton';
    import { League } from '$src/models/league/league';
    import { onMount } from 'svelte';
    import { get, getFrontEndPath, post } from '$src/lib/utils';
    import { ToastMessage } from '$src/models/toastMessage';

    export let league: League
    let code;

    onMount(async () => {
        const response = await get(`/leagues/${ league.id }/codes`)
        const json = await response.json();
        code = json.code
    })

    const generateNew = async () => {
        const response = await post(`/leagues/${ league.id }/codes`)
        const json = await response.json();
        code = json.code
    }

    const copied = () => {
        toastStore.trigger({ message: 'Link copied to clipboard', preset: 'tertiary' })
    }

    $: joinLink = getFrontEndPath(`/leagues/${ league.id }?join=${ code }`);
</script>

<div class="card-body grid grid-cols-1 gap-4">
  <div class="flex flex-row">
    <input
        required
        id="username"
        name="username"
        type="text"
        minlength="2"
        readonly
        bind:value={joinLink}
    />
    <button
        class="btn bg-accent-500 shrink"
        use:clipboard={joinLink}
        use:tooltip={{ content: 'Copy to clipboard', background: '!bg-accent-500' }}
        on:click={copied}
    >
      <Icon icon="material-symbols:content-copy-outline" class="text-2xl"/>
    </button>
  </div>


  <button class="btn bg-primary-500 btn-lg text-white w-full" on:click={generateNew}>
    Generate new link
  </button>
</div>
