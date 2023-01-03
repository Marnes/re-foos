<div class="flex flex-col bg-black w-full overflow-x-hidden h-full">
  <div class="flex flex-row h-full match-screen">
    <div class="flex flex-col w-full card-glass-surface">
      <slot />
    </div>
  </div>
  <div>
    <slot name="action"/>
  </div>
</div>


<style lang="scss">
  .match-screen {
    background-image: url('$lib/assets/match-background.jpg');
    background-repeat: no-repeat;
    background-position: center center;
    background-size: 100%;
  }
</style>
