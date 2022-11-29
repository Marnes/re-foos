<script lang="ts">
    import Leaderboard from '$src/components/stats/leaderboard.svelte';
    import type { PageData } from '$src/$types';
    import { spotlightStore } from '$src/stores/spotlightStore';
    import { Player } from '$src/models/player/player';

    export let data: PageData;

    const changeSpotlight = async (event: CustomEvent<Player>) => {
        $spotlightStore = event.detail
    }
</script>

<Leaderboard
    players="{data.players}"
    on:selected={changeSpotlight}
/>

