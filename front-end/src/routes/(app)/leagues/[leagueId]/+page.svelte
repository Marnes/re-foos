<script lang="ts">
    import Leaderboard from '$src/components/stats/leaderboard.svelte';
    import Highlights from '$src/components/highlight/Highlights.svelte';
    import JoinDialog from '$src/components/league/JoinDialog.svelte';
    import LoginForm from '$src/components/session/LoginForm.svelte';

    import { Player } from '$src/models/player/player';
    import { page } from '$app/stores'
    import { onMount } from 'svelte';
    import { modalStore } from '@skeletonlabs/skeleton';
    import { Modal } from '$src/models/modal';
    import { sessionStore } from '$src/stores/sessionStore';
    import { league, players, playerSpotlight, highlights } from '$src/stores/leagueStore';
    import type { PageData } from '$src/$types';
    import _ from 'lodash';

    export let data: PageData;

    const changeSpotlight = async (event: CustomEvent<Player>) => {
        $playerSpotlight = event.detail;
    }

    $: hasHighlights = !_.isEmpty($highlights);

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
            league: $league,
            code
        }));
    }
</script>

{#if hasHighlights}
  <div class="mb-2">
    <Highlights highlights={$highlights}/>
  </div>
{/if}

<Leaderboard
    players="{$players}"
    on:selected={changeSpotlight}
/>

