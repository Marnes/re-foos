<script lang="ts">
    export let title: string;
    export let value: number | string;

    $: isEmptyValue = (value === 0 || value === '0' || !value);
</script>

<div>
  <span class="text-sm text-accent-500">{title}</span>
  <br/>
  <span class="text-base {$$props.class}" >{ isEmptyValue ? '-' : value}</span>
</div>
