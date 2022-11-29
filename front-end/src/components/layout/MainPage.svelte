<div class="flex h-full w-full">
  <div class="left-content grow h-full place-content-center">
    <div class="card card-body h-full">
      <slot></slot>
    </div>
  </div>
  <div class="h-full right-content hidden xl:flex">
    <div class="card card-body h-full w-full">
      <slot name="right-content"/>
    </div>
  </div>
</div>

<style lang="scss">
  .right-content {
    min-width: 20rem;
    max-width: 40rem;
  }
</style>
