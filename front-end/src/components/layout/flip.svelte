<script lang="ts">
    export let vertical = false;
    export let flipped = false;
    export let duration = 500;

    function flip(node, { delay = 0 }) {
        return {
            delay,
            duration,
            css: (t, u) => `transform: rotate${ vertical ? 'X' : 'Y' }(${ 1 - u * 240 }deg); opacity: ${ 0.5 - u }` };
    }
</script>

<div class="flip-container {$$props.class}">
  <div class="flip">
    {#if !flipped}
      <div class="side" transition:flip>
        <slot name="front"/>
      </div>
    {:else}
      <div class="side back" transition:flip>
        <slot name="back"/>
      </div>
    {/if}
  </div>
</div>

<style lang="scss">
  .flip-container {
    position: relative;
    transform-origin: center right;
  }

  .flip {
    transform-origin: center right;
    width: 100%;
    height: 100%;
    perspective: 1000px;
    position: absolute;
    transform-style: preserve-3d;
  }

  .side {
    position: absolute;
    height: 100%;
    width: 100%;
  }
</style>
