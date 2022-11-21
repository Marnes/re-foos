<script lang="ts">
    import { formatElo } from '$src/lib/util/elo-util.js';

    export let elo: number;
</script>

{#if elo > 0}
  <span class="text-tertiary-500">
    +{formatElo(elo)}
  </span>
{:else }
  <span class="text-warning-500">
    {formatElo(elo)}
  </span>
{/if}
