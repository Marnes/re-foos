<script lang="ts">
    import OverallStats from '$src/components/stats/overall-stats.svelte';
    import type { PageData } from './$types';

    export let data: PageData;
</script>

<OverallStats players="{data.players}"/>

