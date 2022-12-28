<script lang="ts">
    import Leaderboard from '$src/components/stats/leaderboard.svelte';
    import Highlights from '$src/components/highlight/Highlights.svelte';
    import JoinDialog from '$src/components/league/JoinDialog.svelte';
    import LoginForm from '$src/components/session/LoginForm.svelte';

    import { Player } from '$src/models/player/player';
    import { spotlightStore } from '$src/stores/spotlightStore';
    import { highlightStore } from '$src/stores/highlightStore';
    import { page } from '$app/stores'
    import { onMount } from 'svelte';
    import { modalStore } from '@skeletonlabs/skeleton';
    import { Modal } from '$src/models/modal';
    import { sessionStore } from '$src/stores/sessionStore';
    import { leagueStore } from '$src/stores/leagueStore';
    import type { PageData } from '$src/$types';
    import _ from 'lodash';

    export let data: PageData;

    const changeSpotlight = async (event: CustomEvent<Player>) => {
        $spotlightStore = event.detail;
    }

    $: hasHighlights = !_.isEmpty($highlightStore);

    onMount(() => {
        const code = $page.url.searchParams.get('join');

        if (!code) {
            return;
        }

        if (!$sessionStore?.user) {
            modalStore.trigger(Modal.component('Login to <strong>ELO-Musk</strong>', LoginForm, {}, () => {
                showJoinDialog(code);
            }));
        } else {
            showJoinDialog(code);
        }
    })

    const showJoinDialog = (code: string) => {
        modalStore.trigger(Modal.component('', JoinDialog, {
            league: $leagueStore,
            code
        }));
    }
</script>

{#if hasHighlights}
  <div class="mb-2">
    <Highlights highlights={$highlightStore}/>
  </div>
{/if}

<Leaderboard
    players="{data.players}"
    on:selected={changeSpotlight}
/>

