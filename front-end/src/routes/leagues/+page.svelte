<script lang="ts">
    import LeagueItems from '$src/components/league/LeagueItems.svelte';
    import _ from 'lodash';

    import { session } from '$src/stores/sessionStore';
    import type { PageData } from '$src/$types';

    export let data: PageData;

    $: joined = data.leagues.filter(league => league.joined)
    $: other = data.leagues.filter(league => !league.joined)
</script>

<div class="flex flex-col">
  {#if $session?.user}
    <h2 class="mb-3">My Leagues</h2>
    <div class="card card-glass-surface p-3">
      {#if !_.isEmpty(joined) }
        <LeagueItems leagues={joined}/>
      {:else }
        <div class="text-center p-5">
          <span class="text-2xl text-white">No leagues joined</span>
        </div>
      {/if}
    </div>

    <h2 class="mb-3 mt-10">Other Leagues</h2>
    <div class="card card-glass-surface p-3">
      {#if !_.isEmpty(other) }
        <LeagueItems leagues={other}/>
      {:else }
        <div class="text-center p-5">
          <span class="text-2xl text-white">No other leagues</span>
        </div>
      {/if}
    </div>
  {:else }
    <h2 class="mb-3">Leagues</h2>
    <div class="card card-glass-surface p-3">
      <LeagueItems leagues={other}/>
    </div>
  {/if}
</div>


