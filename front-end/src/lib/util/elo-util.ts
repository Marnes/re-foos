export const formatElo = (elo: number): number => {
    return Math.round(elo);
}

export const eloChangeString = (eloChange: number): string => {
    if (eloChange > 0) {
        return `+ ${ formatElo(eloChange) }`;
    }

    return `${ formatElo(eloChange) }`;
}
