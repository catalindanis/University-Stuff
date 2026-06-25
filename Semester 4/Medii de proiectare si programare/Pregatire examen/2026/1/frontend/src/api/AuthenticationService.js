const apiUrl = import.meta.env.VITE_API_BASE_URL;

export const login = async (nickname) => {
    const data = {
        nickname: nickname,
    }

    const response = await fetch(`${apiUrl}/auth/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });

    if (!response.ok) {
        throw new Error(await response.text());
    }

    const responseData = await response.json();
    const token = responseData && responseData.token;
    if (!token) {
        throw new Error('No token returned from server');
    }

    localStorage.setItem('token', token);
    return token;
}

export const getToken = () => {
    return localStorage.getItem('token');
};

export const isAuthenticated = () => {
    return Boolean(getToken());
};

export const logout = () => {
    localStorage.removeItem('token');
};

export const buildAuthenticationHeader = () => {
    const headers = {};
    const token = getToken();
    if (token) headers['Authorization'] = `Bearer ${token}`;
    return headers;
};