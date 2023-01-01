<script lang="ts">
    import TopBar from '$src/components/layout/TopBar.svelte';
    import MainPage from '$src/components/layout/MainPage.svelte';

    import { AppShell } from '@skeletonlabs/skeleton';
    import { session } from '$src/stores/sessionStore';

    const createLeague = () => {

    };

    $: canCreate = $session?.user?.admin

</script>

<AppShell>
  <svelte:fragment slot="header">
    <TopBar hasDrawer={false}>
      <svelte:fragment slot="actions">
        {#if canCreate}
          <a href="/leagues/new" class="hidden md:inline-flex btn bg-primary-500" on:click={createLeague}>
            Create
          </a>
        {/if}
      </svelte:fragment>
    </TopBar>
  </svelte:fragment>

  <MainPage>
    <slot></slot>
  </MainPage>

  {#if canCreate}
    <div class="flex md:hidden h-14">
      <a href="/leagues/new" class="btn bg-primary-500 absolute bottom-4 right-6" on:click={createLeague}>
        Create
      </a>
    </div>
  {/if}
</AppShell>



