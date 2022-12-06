<script lang="ts">
    import { Player, PlayerMin } from '$src/models/player/player';
    import { Avatar } from '@skeletonlabs/skeleton';
    import { getAssetPath } from '$src/lib/utils';
    import { User } from '$src/models/user';

    export let player: Player | PlayerMin | User
</script>

{#if player?.avatar}
  <Avatar src="{getAssetPath(player.avatar)}" {...$$props}/>
{:else }
  <Avatar initials="{player.username[0]}" {...$$props}/>
{/if}
