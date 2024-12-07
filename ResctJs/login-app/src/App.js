import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { BrowserRouter as Router, Routes, Route, useNavigate } from 'react-router-dom';

function Login({ setToken }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/api/login', {
        username,
        password,
      });
      setMessage(response.data.message); // Display success message
      setToken(response.data.token); // Store token
      navigate('/dashboard'); // Navigate to the dashboard
    } catch (error) {
      if (error.response && error.response.status === 401) {
        setMessage('Invalid username or password');
      } else {
        setMessage('An error occurred. Please try again.');
      }
    }
  };

  return (
    <div>
      <h1>Login</h1>
      <form onSubmit={handleLogin}>
        <div>
          <label>Username:</label>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Password:</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit">Login</button>
      </form>
      {message && <p>{message}</p>}
    </div>
  );
}

function Dashboard({ token }) {
  const [data, setData] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get('http://localhost:8080/posts', {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        });
        setData(response.data);  // Set the response data (posts)
      } catch (err) {
        setError('Failed to fetch dashboard data');
      }
    };

    if (token) {
      fetchData();
    }
  }, [token]);

  return (
    <div>
      <h1>Dashboard</h1>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {data ? (
        <>
          <ul>
            {data.map((post) => (
              <li key={post.id}>
                <p><strong>Content:</strong> {post.content}</p>
                <p><strong>Location:</strong> {post.location}</p>
                <p><strong>Timestamp:</strong> {new Date(post.timestamp).toLocaleString()}</p>
              </li>
            ))}
          </ul>
        </>
      ) : (
        <p>Loading...</p>
      )}
    </div>
  );
}

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
