// src/Dashboard.js
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './Dashboard.css';  // Import Dashboard-specific CSS

function Dashboard({ token }) {
  const [data, setData] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get('http://localhost:8080/dashboard/posts', {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        });
        setData(response.data);  // Set the response data (posts along with comments)
      } catch (err) {
        setError('Failed to fetch dashboard data');
      }
    };

    if (token) {
      fetchData();
    }
  }, [token]);

  return (
      <div className="dashboard-container">
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

                      {/* Render Comments */}
                      {post.comments && post.comments.length > 0 && (
                        <div>
                          <h4>Comments:</h4>
                          <ul>
                            {post.comments.map((comment) => (
                              <li key={comment.id}>
                                <p><strong>Comment:</strong> {comment.content}</p>
                                <p><strong>By User:</strong> {comment.endUser}</p>
                                <p><strong>Timestamp:</strong> {new Date(comment.timestamp).toLocaleString()}</p>
                              </li>
                            ))}
                          </ul>
                        </div>
                      )}
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

export default Dashboard;
