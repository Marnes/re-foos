<script lang="ts">
    import RoundRobinBoard from '$src/components/game/match/boards/RoundRobinBoard.svelte';
    import HeadToHeadBoard from '$src/components/game/match/boards/HeadToHeadBoard.svelte';
    import type { League } from '$src/models/league/league';
    import { LeagueType } from '$src/models/league/league';
    import { createEventDispatcher } from 'svelte';
    import type { Player } from '$src/models/player/player';

    export let league: League;
    export let players: Player[];

    const dispatch = createEventDispatcher();

    function submit({ detail }) {
        dispatch('submit', detail);
    }
</script>

{#if league.config.type === LeagueType.HEAD_TO_HEAD}
  <HeadToHeadBoard league={league} players={players} on:submit/>
{:else if (league.config.type === LeagueType.ROUND_ROBIN)}
  <RoundRobinBoard league={league} players={players} on:submit/>
{/if}
