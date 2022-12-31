<script lang="ts">
    import { Avatar } from '@skeletonlabs/skeleton';
    import { getAssetPath } from '$src/lib/utils';
    import { createEventDispatcher } from 'svelte';

    const dispatch = createEventDispatcher();

    export let value: string;
    export let avatars: { categories: string, avatars: string[] }[] = [];

    $: categories = Object.keys(avatars).sort();

    const getAvatars = (category: string): String[] => {
        return avatars[category].sort();
    }

    const setAvatar = (avatar: string) => {
        value = avatar;
        dispatch("change", avatar);
    }
</script>

{#each categories as category }
  <div class="card card-glass-surface mb-5 p-3">
    <div class="card-body">
      <h3 class="mb-3">{category}</h3>
      <div class="flex flex-row flex-wrap gap-4 md:gap-5">
        {#each getAvatars(category) as avatar}
          {#if avatar === value}
            <div class="avatar selected">
              <Avatar
                  width="w-16 md:w-24"
                  border="border-primary-500 border-2 border-solid"
                  src="{getAssetPath(avatar)}"
              />
            </div>
          {:else }
            <div class="avatar">
              <Avatar
                  width="w-16 md:w-24"
                  src="{getAssetPath(avatar)}"
                  on:click={() => setAvatar(avatar)}
              />
            </div>
          {/if}
        {/each}
      </div>
    </div>
  </div>
{/each}

<style lang="scss">
  .avatar {
    opacity: 0.4;
    cursor: pointer;

    &.selected {
      opacity: 1;
    }
  }
</style>
