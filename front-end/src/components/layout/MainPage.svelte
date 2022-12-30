<div class="flex h-full w-full p-2 xl:p-6">
  <div class="left-content grow place-content-center">
    <slot></slot>
  </div>
  {#if $$slots['right-content'] }
    <div class="card !bg-tertiary-500/5 ml-5 p-3 h-fit hidden xl:flex">
      <div class="right-content">
        <slot name="right-content"/>
      </div>
    </div>
  {/if}
</div>

<style lang="scss">
  .right-content {
    min-width: 20rem;
    max-width: 40rem;
  }
</style>
