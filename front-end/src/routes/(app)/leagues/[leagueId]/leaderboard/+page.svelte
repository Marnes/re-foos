<script lang="ts">
    import Leaderboard from '$src/components/stats/leaderboard.svelte';
    import Highlights from '$src/components/highlight/Highlights.svelte';

    import { Player } from '$src/models/player/player';
    import { highlights, playerSpotlight } from '$src/stores/leagueStore';
    import type { PageData } from '$src/$types';
    import _ from 'lodash';

    export let data: PageData;

    const changeSpotlight = async (event: CustomEvent<Player>) => {
        $playerSpotlight = event.detail;
    }

    $: hasHighlights = !_.isEmpty($highlights);
</script>

{#if hasHighlights}
  <div class="mb-2">
    <Highlights highlights={$highlights}/>
  </div>
{/if}

<Leaderboard
    players="{data.leaderboard}"
    on:selected={changeSpotlight}
/>

