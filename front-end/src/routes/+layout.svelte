<script lang="ts">
    import '@skeletonlabs/skeleton/themes/theme-skeleton.css';
    import '@skeletonlabs/skeleton/styles/all.css';
    import '$src/app.postcss';

    import { Toast } from '@skeletonlabs/skeleton';
    import Dialog from '$src/components/layout/Dialog.svelte';
</script>

<Dialog/>

<Toast/>

<slot/>
