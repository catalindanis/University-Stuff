import { getToken } from './AuthService';
const apiUrl = process.env.REACT_APP_API_BASE_URL;

const buildHeaders = (hasJsonBody = false) => {
    const headers = {};
    if (hasJsonBody) headers['Content-Type'] = 'application/json';
    const token = getToken();
    if (token) headers['Authorization'] = `Bearer ${token}`;
    return headers;
};

export const fetchShows = async (filters = {}) => {
    try {
        const queryParams = new URLSearchParams();

        if (filters.artistName) {
            queryParams.append('artistName', filters.artistName);
        }
        if (filters.date) {
            queryParams.append('date', filters.date);
        }
        if (filters.location) {
            queryParams.append('location', filters.location);
        }
        if (filters.numberOfSeats) {
            queryParams.append('numberOfSeats', filters.numberOfSeats);
        }

        const queryString = queryParams.toString();
        const url = queryString ? `${apiUrl}/shows?${queryString}` : `${apiUrl}/shows`;

        const response = await fetch(url, { headers: buildHeaders(false) });

        if (!response.ok) {
            throw new Error('Failed to fetch shows');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error fetching shows:', error);
        throw error;
    }
}

export const fetchShowById = async (id) => {
    try {
        const response = await fetch(`${apiUrl}/shows/${id}`, { headers: buildHeaders(false) });

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

export const createShow = async (showData) => {
    try {
        const response = await fetch(`${apiUrl}/shows`, {
            method: 'POST',
            headers: buildHeaders(true),
            body: JSON.stringify(showData),
        });

        if (!response.ok) {
            throw new Error('Failed to create show');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error creating show:', error);
        throw error;
    }
}

export const updateShow = async (id, showData) => {
    try {
        const response = await fetch(`${apiUrl}/shows/${id}`, {
            method: 'PUT',
            headers: buildHeaders(true),
            body: JSON.stringify(showData),
        });

        if (!response.ok) {
            throw new Error('Failed to update show');
        }
    } catch (error) {
        console.error('Error updating show:', error);
        throw error;
    }
}

export const deleteShow = async (id) => {
    try {
        const response = await fetch(`${apiUrl}/shows/${id}`, {
            method: 'DELETE',
            headers: buildHeaders(false),
        });

        if (!response.ok) {
            throw new Error('Failed to delete show');
        }

        return true;
    } catch (error) {
        console.error('Error deleting show:', error);
        throw error;
    }
}