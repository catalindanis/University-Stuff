import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Shows from './features/shows';
import Login from './features/auth/Login';
import { getToken } from './api/AuthService';
import { useState, useEffect } from 'react';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(Boolean(getToken()));

  useEffect(() => {
    const onStorage = (e) => {
      if (e.key === 'token') setIsAuthenticated(Boolean(e.newValue));
    };
    window.addEventListener('storage', onStorage);
    return () => window.removeEventListener('storage', onStorage);
  }, []);

  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/login"
          element={isAuthenticated ? <Shows /> : <Login />}
        />
        <Route
          path="/shows"
          element={<Shows />}
        />
        <Route
          path="*"
          element={<Navigate to={isAuthenticated ? "/shows" : "/login"} replace />}
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;