<script lang="ts">
    import LoginForm from '$src/components/session/LoginForm.svelte';
    import JoinDialog from '$src/components/league/JoinDialog.svelte';


    import { goto, invalidateAll } from '$app/navigation';
    import { modalStore } from '@skeletonlabs/skeleton';
    import { page } from '$app/stores';
    import { session } from '$src/stores/sessionStore.js';
    import { league } from '$src/stores/leagueStore.js';
    import { onMount } from 'svelte';
    import { Modal } from '$src/models/modal';

    onMount(() => {
        const code = $page.url.searchParams.get('join');

        if (!code) {
            return;
        }

        if (!$session?.user) {
            modalStore.trigger(Modal.component('Login to <strong>ELO-Musk</strong>', LoginForm, {}, (response) => {
                if (response) {
                    showJoinDialog(code);
                } else {
                    goto(`/leagues/${ $league.id }/`);
                }
            }));
        } else {
            showJoinDialog(code);
        }
    })

    const showJoinDialog = (code: string) => {
        modalStore.trigger(Modal.component('', JoinDialog, {
                league: $league,
                code,
            },
            _ => {
                goto(`/leagues/${ $league.id }/`);
                invalidateAll();
            }));
    }
</script>
