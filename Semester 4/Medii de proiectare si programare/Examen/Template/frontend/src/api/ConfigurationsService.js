import {buildAuthenticationHeader} from "./AuthenticationService.js";

const apiUrl = import.meta.env.VITE_API_BASE_URL;

export const fetchConfigurations = async () => {
    const response = await fetch(`${apiUrl}/configurations`, {
        headers: buildAuthenticationHeader()
    });

    if (!response.ok) {
        throw new Error('Failed to fetch configurations');
    }

    return await response.json();
};

export const fetchShowById = async (id) => {
    try {
        const response = await fetch(`${apiUrl}/shows/${id}`);

        if (!response.ok) {
            throw new Error('Failed to fetch show');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error fetching show:', error);
        throw error;
    }
}

export const saveConfiguration = async (category, answer, points) => {
    const data = {
        category: category,
        answer: answer,
        numberOfPoints: points
    }

    const response = await fetch(`${apiUrl}/answers`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });

    if (!response.ok) {
        throw new Error(await response.text());
    }

    return await response.json();
}