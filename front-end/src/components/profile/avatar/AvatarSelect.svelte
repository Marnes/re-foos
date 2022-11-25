<script lang="ts">
    import Section from '$src/components/layout/Section.svelte';

    import { Avatar } from '@brainandbones/skeleton';
    import { getAvatarUrl } from '$src/lib/api.js';
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
  <Section title={category} class="mb-5 mt-2">
    <div class="card-body">
      <div class="flex flex-row flex-wrap gap-4">
        {#each getAvatars(category) as avatar}
          {#if avatar === value}
            <div class="avatar selected">
              <Avatar
                  width="w-24"
                  border="border-primary-500 border-2 border-solid"
                  src="{getAvatarUrl(avatar)}"
              />
            </div>
          {:else }
            <div class="avatar">
              <Avatar
                  width="w-24"
                  src="{getAvatarUrl(avatar)}"
                  on:click={() => setAvatar(avatar)}
              />
            </div>
          {/if}
        {/each}
      </div>
    </div>
  </Section>
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
