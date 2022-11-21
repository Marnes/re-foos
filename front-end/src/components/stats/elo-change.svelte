<script lang="ts">
    import { formatElo } from '$src/lib/util/elo-util.js';

    export let elo: number;
</script>

{#if elo > 0}
  <span class="text-positive">
    +{formatElo(elo)}
  </span>
{:else }
  <span class="text-negative">
    {formatElo(elo)}
  </span>
{/if}
