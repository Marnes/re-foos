<script lang="ts">
    import { Player, PlayerMin } from '$src/models/player/player';
    import { Avatar } from '@brainandbones/skeleton';
    import { getAvatarUrl } from '$src/lib/api';
    import { User } from '$src/models/user';

    export let player: Player | PlayerMin | User
</script>

{#if player?.avatar}
  <Avatar src="{getAvatarUrl(player.avatar)}" {...$$props}/>
{:else }
  <Avatar initials="{player.username[0]}" {...$$props}/>
{/if}
