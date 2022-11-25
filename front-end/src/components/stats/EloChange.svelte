<script lang="ts">
    import { formatElo } from '$src/lib/util/elo-util.js';

    export let elo: number;

    $: formattedElo = formatElo(elo);
</script>

{#if formattedElo > 0}
  <span class="text-positive">
    +{formattedElo}
  </span>
{:else if formattedElo === 0}
  <span>
    {formatElo(formattedElo)}
  </span>
{:else}
  <span class="text-negative">
    {formatElo(formattedElo)}
  </span>
{/if}
