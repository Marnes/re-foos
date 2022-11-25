<script lang="ts">
    import Leaderboard from '$src/components/stats/leaderboard.svelte';
    import type { PageData } from '$src/$types';
    import { spotlightStore } from '$src/stores/spotlightStore';
    import { get } from '$src/lib/utils';
    import { Player } from '$src/models/player/player';

    export let data: PageData;

    const changeSpotlight = async (event: CustomEvent<Player>) => {
        const response = await get(`/leagues/1/players/spotlight?playerId=${ event.detail.id }`)
        $spotlightStore = await response.json();
    }
</script>

<Leaderboard
    players="{data.players}"
    on:selected={changeSpotlight}
/>

