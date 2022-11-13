<script lang="ts">
    import { dialogStore } from "$src/stores/dialog-store";
    import { Dialog } from "$src/models/dialog";
    import _ from 'lodash';
    import Icon from "@iconify/svelte";

    let dialog: Dialog;

    function closeModal() {
        dialog = null;
        $dialogStore = null;
    }

    $: show = !_.isNil(dialog);

    dialogStore.subscribe(value => {
        dialog = value;
    });
</script>

<div
    class="overlay fixed inset-0 bg-gray-800 bg-opacity-80 overflow-y-auto h-full w-full"
    class:hidden={!show}
>

</div>
<div
    class="transition-all duration-200 dialog fixed p-4 w-full max-w-2xl"
    class:hidden={!show}
>
  <div class="relative bg-white shadow dark:bg-gray-700">
    <div class="flex justify-between items-center p-4 rounded-t border-b dark:border-gray-600">
      <h3 class="text-xl pl-2 font-semibold">
        { @html dialog?.title }
      </h3>
      <button
          type="button"
          class="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm p-1.5 ml-auto inline-flex items-center dark:hover:bg-gray-600 dark:hover:text-white"
          data-modal-toggle="defaultModal"
          on:click={closeModal}
      >

        <Icon icon="ep:close-bold" class="text-2xl"/>
      </button>
    </div>
    <div class="p-6">
      {#if show}
        <svelte:component this={dialog.component} {...dialog.props}/>
      {/if}
    </div>
  </div>
</div>

<style lang="scss">
  .dialog {
    z-index: 101;
    left: 50%;
    top: 50%;
    -ms-transform: translateX(-50%) translateY(-50%);
    -webkit-transform: translate(-50%, -50%);
    transform: translate(-50%, -50%);

    -webkit-animation: fadein 0.5s; /* Safari, Chrome and Opera > 12.1 */
    -moz-animation: fadein 0.5s; /* Firefox < 16 */
    -ms-animation: fadein 0.5s; /* Internet Explorer */
    -o-animation: fadein 0.5s; /* Opera < 12.1 */
    animation: fadein 0.5s;
  }

  .overlay {
    z-index: 100;
  }

  @keyframes fadein {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }

  /* Firefox < 16 */
  @-moz-keyframes fadein {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }

  /* Safari, Chrome and Opera > 12.1 */
  @-webkit-keyframes fadein {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }

  /* Internet Explorer */
  @-ms-keyframes fadein {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }
</style>
