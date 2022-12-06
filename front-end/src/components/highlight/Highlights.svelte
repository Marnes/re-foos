<script lang="ts">
    import { Highlight, MessageType } from '$src/models/highlight/highlight';
    import { Alert } from '@skeletonlabs/skeleton';
    import { buildHighlightMessage } from '$src/lib/util/highlightUtil.js';
    import { humanDateWithTime } from '$src/lib/util/dateUtil.js';

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
</script>

{#each highlights as highlight}
  <Alert
      class="mt-1"
      border="border-l-4 {alertBorder(highlight)}"
      background="{alertClass(highlight)}"
  >
    <span class="text-xs {alertText(highlight)}">{humanDateWithTime(highlight.createdDate)}</span><br/>
    <span>{@html buildHighlightMessage(highlight.message, highlight.players)}</span>
  </Alert>
{/each}
