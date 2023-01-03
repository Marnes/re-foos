<script lang="ts">
    import LeagueItem from '$src/components/league/LeagueItem.svelte';
    import { League } from '$src/models/league/league';
    import { sortAlphaNum } from '$src/lib/util/stringUtil';

    export let leagues: League[];

    $: sortedLeagues = leagues.sort((l1, l2) => sortAlphaNum(l1.name, l2.name));
</script>

<div class="grid grid-cols-1 lg:grid-cols-4 gap-6 lg:gap-3">
  {#each sortedLeagues as league}
    <LeagueItem league={league}/>
  {/each}
</div>
