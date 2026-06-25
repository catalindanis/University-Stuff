import { BrowserRouter, Routes, Route } from 'react-router-dom'

import './App.css'
import Configurations from "./features/configurations/index.jsx";
import Login from "./features/authentication/index.jsx";
import Home from "./features/home/index.jsx";
import { useState, useEffect } from 'react';
import { isAuthenticated } from './api/AuthenticationService.js';

function App() {
    const [authenticated, setAuthenticated] = useState(isAuthenticated());

    useEffect(() => {
        const onStorage = (e) => {
            if (e.key === 'token') setAuthenticated(Boolean(e.newValue));
        };
        window.addEventListener('storage', onStorage);
        return () => window.removeEventListener('storage', onStorage);
    }, []);

  return (
    <BrowserRouter>
        <Routes>
            <Route path="/configurations" element={<Configurations />} />
            <Route path="/login" element={<Login />}></Route>
            <Route path="/home" element={<Home />}></Route>
            <Route path="*" element={authenticated ? <Home /> : <Login />} />
        </Routes>
    </BrowserRouter>
  )
}

export default App
