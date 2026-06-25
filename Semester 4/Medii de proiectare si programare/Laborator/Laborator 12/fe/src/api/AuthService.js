const apiUrl = process.env.REACT_APP_API_BASE_URL;

export const login = async (email, password) => {
    try {
        const response = await fetch(`${apiUrl}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email, password }),
        });

        if (!response.ok) {
            const errText = await response.text().catch(() => null);
            throw new Error(errText || 'Login failed');
        }

        const data = await response.json();
        const token = data && data.token;
        if (!token) {
            throw new Error('No token returned from server');
        }

        localStorage.setItem('token', token);
        return token;
    } catch (error) {
        console.error('AuthService.login error:', error);
        throw error;
    }
};

export const getToken = () => {
    return localStorage.getItem('token');
};

export const isAuthenticated = () => {
    return Boolean(getToken());
};

export const logout = () => {
    localStorage.removeItem('token');
};

export default {
    login,
    getToken,
    isAuthenticated,
    logout,
};

