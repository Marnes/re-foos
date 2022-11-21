<script lang="ts">
    import { AppRail, AppRailTile } from "@brainandbones/skeleton";
    import { railStore } from "$src/stores/app-store.js";
    import Icon from '@iconify/svelte';

</script>

<AppRail selected={railStore} class="hidden lg:block">
  <AppRailTile label="Leaderboard" title="Leaderboard" value={1}>
    <Icon icon="iconoir:leaderboard-star" class="text-2xl" />
  </AppRailTile>
</AppRail>
