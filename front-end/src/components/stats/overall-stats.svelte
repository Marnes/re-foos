<script lang='ts'>
    import _ from 'lodash';
    import type { Player } from '$src/models/player';
    import { eloChangeString, formatElo } from '$src/lib/util/elo-util';
    import { DataTable } from "@brainandbones/skeleton";

    export let players: Player[];

    const tableHeadings = ['Rank', 'Player', 'Played', 'Wins', 'Losses', 'Elo', 'Change'];
    $: tableSource = _.sortBy(players, 'rank')
                            .map(player => ({
                                rank: player.rank,
                                username: player.username,
                                played: player.played,
                                wins: player.wins,
                                losses: player.losses,
                                elo: formatElo(player.elo),
                                eloChange: eloChangeString(player.eloChange)
                            }))
</script>

<DataTable
    headings={tableHeadings}
    source={tableSource}
>
</DataTable>

<style lang='scss'>
  .table th, td {
    border-radius: 0;
  }
</style>
