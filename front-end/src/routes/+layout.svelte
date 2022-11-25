<script lang="ts">
    import '@brainandbones/skeleton/themes/theme-skeleton.css';
    import '@brainandbones/skeleton/styles/all.css';
    import '$src/app.postcss';

    import { Toast } from '@brainandbones/skeleton';
    import Dialog from '$src/components/layout/Dialog.svelte';
</script>

<Dialog/>

<Toast/>

<slot/>
