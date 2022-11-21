<script lang="ts">
    import Leaderboard from '$src/components/stats/leaderboard.svelte';
    import type { PageData } from './$types';

    export let data: PageData;
</script>

<Leaderboard players="{data.players}"/>

