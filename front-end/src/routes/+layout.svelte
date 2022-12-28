<script lang="ts">
    import '@skeletonlabs/skeleton/themes/theme-skeleton.css';
    import '@skeletonlabs/skeleton/styles/all.css';
    import '$src/app.postcss';

    import { Modal, Toast } from '@skeletonlabs/skeleton';
    import Dialog from '$src/components/layout/Dialog.svelte';
</script>

<Dialog/>
<Toast />
<Modal/>

<slot/>

