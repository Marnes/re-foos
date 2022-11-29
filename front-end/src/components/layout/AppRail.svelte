<script lang="ts">
    import Icon from '@iconify/svelte';
    import { AppRail, AppRailTile } from '@brainandbones/skeleton';
    import { menuDrawerStore } from '$src/stores/menu-store.js';

    export let rails: [{ title: string, link: string, icon: string, selected: boolean }] = [];

    const closeDrawer = () => {
        $menuDrawerStore = false;
    }

    const isSelectedClass = (rail) => {
        if (rail.selected) {
            return '!bg-primary-500';
        }

        return '';
    }

</script>

<AppRail width="w-24 lg:w-20">
  {#each rails as rail, index}
    <AppRailTile
        tag="a"
        class="wrapper-link {isSelectedClass(rail)}"
        label={rail.title}
        title={rail.title}
        href="{rail.link}"
        value={rail.title}
        on:click={closeDrawer}
    >
      <Icon icon="{rail.icon}" class="text-2xl"/>
    </AppRailTile>
  {/each}
</AppRail>


