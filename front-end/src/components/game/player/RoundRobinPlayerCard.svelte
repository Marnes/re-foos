<script lang="ts">
    import PlayerAvatar from '$src/components/player/PlayerAvatar.svelte';
    import { createEventDispatcher } from 'svelte';
    import type { Player } from '$src/models/player/player';

    export let player: Player;
    export let reversed = false;
    export let tiny = false;

    const dispatch = createEventDispatcher();

    function onClick(player: Player) {
        return () => {
            dispatch('click', player);
        };
    }
</script>

<div class="relative">
  <PlayerAvatar
      class="h-16 w-16"
      rounded="rounded-none"
      background="bg-black/20"
      player={player}
  />
  <div class="absolute">
    <span>{player.username}</span>
  </div>
</div>
