<script lang="ts">
    import PlayerAvatar from '$src/components/player/PlayerAvatar.svelte';
    import { User } from '$src/models/user.ts';
    import { humanDate } from '$src/lib/util/date-util.js';

    export let user: User;
</script>

<div class="text-center flex flex-col items-center">
  <div class="mb-3">
    <PlayerAvatar width="w-36" player="{user}"/>
  </div>
  <span class="text-3xl font-bold text-surface-500">{user.username}</span>
</div>

<div class="text-center flex flex-col items-center mt-5">
  <span class="text-md text-surface-500 mb-1">Joined</span>
  <span class="text-lg font-bold text-surface-500">{humanDate(user.createdDate)}</span>
</div>
