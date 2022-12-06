<script lang="ts">
    import { Highlight, HighlightPlayer, HighlightPlayerResult, MessageType } from '$src/models/highlight/highlight';
    import { Alert } from '@skeletonlabs/skeleton';
    import { buildHighlightMessage } from '$src/lib/util/highlightUtil.js';
    import { humanDateWithTime } from '$src/lib/util/dateUtil.js';
    import PlayerAvatar from '$src/components/player/PlayerAvatar.svelte';

    export let highlights: Highlight[];

    const colors = {
        [MessageType.WIN]: {
            bg: 'bg-tertiary-500/40',
            border: 'border-tertiary-500',
            text: 'text-tertiary-500'
        },
        [MessageType.LOSE]: {
            bg: 'bg-warning-500/40',
            border: 'border-warning-500',
            text: 'text-warning-500'
        },
        [MessageType.WIN_LOSE]: {
            bg: 'bg-neutral-500/40',
            border: 'border-neutral-500',
            text: 'text-neutral-500'
        },
        [MessageType.DRAW]: {
            bg: 'bg-neutral-500/40',
            border: 'border-neutral-500',
            text: 'text-neutral-500'
        },
        [MessageType.WHITEWASH]: {
            bg: 'bg-surface-500/40',
            border: 'border-surface-500',
            text: 'text-surface-500'
        }
    }

    const alertClass = (highlight: Highlight): string => {
        return colors[highlight.messageType].bg
    }

    const alertBorder = (highlight: Highlight): string => {
        return colors[highlight.messageType].border
    }

    const alertText = (highlight: Highlight): string => {
        return colors[highlight.messageType].text
    }

    const getAvatarClass = (highlightPlayer: HighlightPlayer): string => {
        if (highlightPlayer.result === HighlightPlayerResult.WIN) {
            return `border-2 ${ colors[MessageType.WIN].border }`
        }

        if (highlightPlayer.result === HighlightPlayerResult.LOSE) {
            return `border-2 ${ colors[MessageType.LOSE].border }`
        }

        return '';
    }
</script>

{#each highlights as highlight}
  <Alert
      class="mt-1 relative"
      border="border-l-4 {alertBorder(highlight)}"
      background="{alertClass(highlight)}"
  >
    <span class="text-xs {alertText(highlight)}">{humanDateWithTime(highlight.createdDate)}</span><br/>
    <span>{@html buildHighlightMessage(highlight.message, highlight.players)}</span>

    <div class="flex gap-1 absolute top-2 right-2">
      {#each highlight.players as highlightPlayer}
        <PlayerAvatar
            class="{getAvatarClass(highlightPlayer)}"
            player={highlightPlayer.player}
            showTooltip={true}
            width="w-8"
        />
      {/each}
    </div>
  </Alert>
{/each}
