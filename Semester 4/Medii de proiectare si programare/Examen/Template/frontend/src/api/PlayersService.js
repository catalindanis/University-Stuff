import {buildAuthenticationHeader} from "./AuthenticationService.js";

const apiUrl = import.meta.env.VITE_API_BASE_URL;

export const joinGame = async () => {
    const response = await fetch(`${apiUrl}/games/join-game`, {
        headers: buildAuthenticationHeader(),
        method: "POST",
    });

    if (!response.ok) {
        throw new Error(await response.text());
    }

    if (response.status === 204 || response.headers.get("content-length") === "0") {
        return { success: true };
    }

    return await response.json();
};

export const selectInitialConfiguration = async (id) => {
    const response = await fetch(`${apiUrl}/players/new-game`, {
        headers: {
            ...buildAuthenticationHeader(),
            'Content-Type': 'application/json'
        },
        method: 'POST',
        body: JSON.stringify({ configurationId: id })
    });

    if (!response.ok) {
        throw new Error(await response.text());
    }

    if (response.status === 204 || response.headers.get("content-length") === "0") {
        return { success: true };
    }

    return await response.json();
}

export const submitMove = async(gameId, generatedNumber, round) => {
    const response = await fetch(`${apiUrl}/players/move`, {
        headers: {
            ...buildAuthenticationHeader(),
            'Content-Type': 'application/json'
        },
        method: 'POST',
        body: JSON.stringify({
            gameId: gameId,
            generatedNumber: generatedNumber,
            round: round
        })
    });

    if (!response.ok) {
        throw new Error(await response.text());
    }

    if (response.status === 204 || response.headers.get("content-length") === "0") {
        return { success: true };
    }

    return await response.json();
}