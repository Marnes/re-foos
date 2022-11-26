<script lang="ts">
    import Icon from '@iconify/svelte';
    import { AppRail, AppRailTile } from '@brainandbones/skeleton';
    import { writable, type Writable } from 'svelte/store';
    import _ from 'lodash';

    export let rails: [{ title: string, link: string, icon: string, selected: boolean }] = [];

    const getInitialValue = (): number => {
        const initialValue = _.findIndex(rails, rail => rail.selected);

        return initialValue === -1 ? 1 : initialValue + 1;
    }

    const railStore: Writable<number> = writable(getInitialValue());
</script>

<AppRail selected={railStore} width="w-24 lg:w-20">
  {#each rails as rail, index}
    <AppRailTile
        tag="a"
        class="wrapper-link"
        label={rail.title}
        title={rail.title}
        href="{rail.link}"
        value={index + 1}
    >
      <Icon icon="{rail.icon}" class="text-2xl"/>
    </AppRailTile>
  {/each}
</AppRail>


