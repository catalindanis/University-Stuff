import { useState } from 'react';
import TextInput from '../../components/TextInput';
import Button from '../../components/Button';
import './style.css';
import { login as authLogin } from '../../api/AuthService';

function Login({ onLogin }) {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        if (!email.trim() || !password) {
            setError('Please enter email and password.');
            return;
        }

        setLoading(true);
        try {
            await authLogin(email.trim(), password);
            if (onLogin) onLogin();
            else window.location.reload();
        } catch (err) {
            console.error('Login failed', err);
            setError(err.message || 'Login failed');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-container">
            <form className="auth-form" onSubmit={handleSubmit}>
                <h2 className="auth-title">Sign in</h2>
                {error && <div className="auth-error">{error}</div>}

                <TextInput
                    label="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="you@example.com"
                    type="email"
                />

                <TextInput
                    label="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="Your password"
                    type="password"
                />

                <div className="auth-actions">
                    <Button type="submit" disabled={loading}>
                        {loading ? 'Signing in...' : 'Sign in'}
                    </Button>
                </div>
            </form>
        </div>
    );
}

export default Login;

