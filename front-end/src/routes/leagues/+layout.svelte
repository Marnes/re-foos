<script lang="ts">
    import TopBar from '$src/components/layout/TopBar.svelte';
    import MainPage from '$src/components/layout/MainPage.svelte';
    import LeagueForm from '$src/components/league/LeagueForm.svelte';

    import type { ModalSettings } from '@skeletonlabs/skeleton';
    import { AppShell, modalStore } from '@skeletonlabs/skeleton';
    import { session } from '$src/stores/sessionStore';
    import { buildModalComponent, ModalType } from '$src/lib/util/skeletonUtil';

    const createLeague = () => {
        const modal: ModalSettings = {
            type: ModalType.COMPONENT,
            title: 'Create new League',
            component: buildModalComponent(LeagueForm),
        }

        modalStore.trigger(modal);
    };

    $: canCreate = $session?.user?.admin

</script>

<AppShell>
  <svelte:fragment slot="header">
    <TopBar hasDrawer={false}>
      <svelte:fragment slot="actions">
        {#if canCreate}
          <button class="hidden md:inline-flex btn bg-primary-500" on:click={createLeague}>
            Create
          </button>
        {/if}
      </svelte:fragment>
    </TopBar>
  </svelte:fragment>

  <MainPage>
    <slot></slot>
  </MainPage>

  {#if canCreate}
    <div class="flex md:hidden h-14">
      <button class="btn bg-primary-500 absolute bottom-4 right-4" on:click={createLeague}>
        Create
      </button>
    </div>
  {/if}
</AppShell>



