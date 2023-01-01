<script lang="ts">
    import LeagueTable from '$src/components/league/LeagueTable.svelte';

    import { session } from '$src/stores/sessionStore';
    import type { PageData } from '$src/$types';

    export let data: PageData;

    $: joined = data.leagues.filter(league => league.joined)
    $: other = data.leagues.filter(league => !league.joined)
</script>

<div class="flex flex-col p-5">
  {#if $session?.user}
    <div class="card card-glass-surface p-3">
      <h3 class="mb-3">My Leagues</h3>
      <LeagueTable leagues={joined}/>
    </div>

    <div class="card card-glass-surface p-3 mt-10">
      <h3 class="mb-3">Other Leagues</h3>
      <LeagueTable leagues={other}/>
    </div>
  {:else }
    <div class="card card-glass-surface p-3">
      <h3 class="mb-3">Leagues</h3>
      <LeagueTable leagues={other}/>
    </div>
  {/if}
</div>


