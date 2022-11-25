<script lang="ts">
    export let title: string;
    export let position: string = 'left';

    export let color: string = 'text-surface-500'
    export let weight: string = 'font-bold'
    export let size: string = 'text-2xl'

</script>

<div class="w-full relative">
  <span class="{color} {weight} {size} {position} absolute top-0 section-title">{title}</span>
  <div class="card w-full {$$restProps.class} pt-3">

    <slot></slot>
  </div>
</div>

<style lang="scss">
  .section-title {
    top: 0;
    display: inline-block;
    background-color: rgb(var(--color-surface-800) / var(--tw-bg-opacity));

    -ms-transform: translateX(0) translateY(-47%);
    -webkit-transform: translate(0, -47%);
    transform: translate(0, -47%);

    &.left {
      left: 0.5rem;
      padding-left: 0.5rem;
      padding-right: 1rem;
    }

    &.center {
      left: 50%;
      -ms-transform: translateX(-50%) translateY(-47%);
      -webkit-transform: translate(-50%, -47%);
      transform: translate(-50%, -47%);
      padding-left: 2.5rem;
      padding-right: 2.5rem;
    }

    &.right {
      right: 0.5rem;
      padding-left: 1rem;
      padding-right: 0.5rem;
    }
  }
</style>
