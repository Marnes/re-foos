<script lang="ts">
    import PlayerAvatar from '$src/components/player/PlayerAvatar.svelte';
    import { User } from '$src/models/user.ts';
    import { humanDate } from '$src/lib/util/dateUtil.js';
    import { Player, PlayerMin } from '$src/models/player/player';
    import { Divider } from '@skeletonlabs/skeleton';

    export let player: User | Player | PlayerMin;
</script>

<div class="text-center flex flex-col items-center">
  <div class="mb-3">
    <PlayerAvatar width="w-32" player="{player}"/>
  </div>
  <span class="text-3xl font-bold text-surface-500">{player.username}</span>
</div>

<div class="py-3 mb-3">
  <h6 class="font-bold text-surface-500 text-center">Joined</h6>
  <div class="font-bold text-surface-700 text-center text-sm">
    {humanDate(player.createdDate)}
  </div>
</div>

<div class="pl-7 pr-7 mt-5 mb-5">
  <Divider />
</div>

<slot></slot>
