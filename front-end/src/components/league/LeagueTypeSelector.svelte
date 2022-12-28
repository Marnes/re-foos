<script lang="ts">
    import { LeagueType } from '$src/models/league/league';
    import { writable, type Writable } from 'svelte/store';
    import { RadioGroup, RadioItem } from '@skeletonlabs/skeleton';

    const storeLeagueType: Writable<string> = writable(LeagueType.HEAD_TO_HEAD);
</script>




<style lang="scss">
  .leagueType {
    height: 300px !important;
  }
</style>
