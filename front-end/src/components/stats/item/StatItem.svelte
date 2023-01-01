<script lang="ts">
    import EloChange from '$src/components/stats/EloChange.svelte';

    export let title: string;
    export let value: number | string = undefined;
    export let eloChange: number = undefined;

    $: isEmptyValue = (value === 0 || value === '0' || !value);
</script>

<div>
  <span class="text-sm text-surface-500/70">{title}</span>
  <br/>

  {#if eloChange}
    <EloChange elo={eloChange}/>
  {:else }
    <span class="text-base {$$props.class}">{ isEmptyValue ? '-' : value}</span>
  {/if}

</div>
