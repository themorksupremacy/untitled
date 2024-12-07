// src/App.js
import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './Login';  // Import Login component
import Dashboard from './Dashboard';  // Import Dashboard component

function App() {
    const [token, setToken] = useState(null);

    return (
        <Router>
            <Routes>
                <Route path="/" element={<Login setToken={setToken} />} />
                <Route path="/dashboard" element={<Dashboard token={token} />} />
            </Routes>
        </Router>
    );
}

export default App;
