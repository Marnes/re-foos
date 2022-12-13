<script lang="ts">
    import Leaderboard from '$src/components/stats/leaderboard.svelte';
    import Highlights from '$src/components/highlight/Highlights.svelte';

    import { Player } from '$src/models/player/player';
    import { spotlightStore } from '$src/stores/spotlightStore';
    import { highlightStore } from '$src/stores/highlightStore';
    import type { PageData } from '$src/$types';
    import _ from 'lodash';

    export let data: PageData;

    const changeSpotlight = async (event: CustomEvent<Player>) => {
        $spotlightStore = event.detail
    }

    $: hasHighlights = !_.isEmpty($highlightStore)
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

