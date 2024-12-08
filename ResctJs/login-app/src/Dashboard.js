import React, { useState, useEffect } from "react";
import axios from "axios";
import "./Dashboard.css"; // Import Dashboard-specific CSS

function Dashboard({ token }) {
  const [data, setData] = useState(null);
  const [error, setError] = useState("");
  const [votes, setVotes] = useState({}); // Track votes for each comment
  const [openComments, setOpenComments] = useState({}); // Track open/close state of comments for each post
  const [clickCounts, setClickCounts] = useState({}); // Track the number of clicks for upvotes and downvotes

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get("http://localhost:8080/dashboard/posts", {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        });
        setData(response.data); // Set the response data (posts)
      } catch (err) {
        console.error(err);
        setError("Failed to fetch dashboard data");
      }
    };

    if (token) {
      fetchData();
    }
  }, [token]);

  // Function to handle voting for comments (upvote or downvote)
  const handleVote = async (commentId, voteType, postId) => {
    setVotes((prevVotes) => {
      const currentVote = prevVotes[commentId];
      return { ...prevVotes, [commentId]: currentVote === voteType ? null : voteType };
    });

    setClickCounts((prevCounts) => {
      const currentCount = prevCounts[commentId] || { up: 0, down: 0 };
      let updatedCount;

      if (voteType === "up") {
        updatedCount = {
          up: currentCount.up + (votes[commentId] === "up" ? -1 : 1),
          down: currentCount.down - (votes[commentId] === "down" ? 1 : 0),
        };
      } else if (voteType === "down") {
        updatedCount = {
          up: currentCount.up - (votes[commentId] === "up" ? 1 : 0),
          down: currentCount.down + (votes[commentId] === "down" ? -1 : 1),
        };
      }

      return { ...prevCounts, [commentId]: updatedCount };
    });

    try {
      // Send the vote to the backend API
      await axios.post(`http://localhost:8080/comments/${commentId}/vote`, {
        userId: 1, // Replace with actual user ID
        voteType,
      });

      // Update the UI with the new vote count
      const updatedData = data.map((post) =>
          post.id === postId
              ? {
                ...post,
                comments: post.comments.map((comment) =>
                    comment.id === commentId
                        ? {
                          ...comment,
                          vote_count:
                              voteType === "up"
                                  ? comment.vote_count + (votes[commentId] === "up" ? -1 : 1)
                                  : comment.vote_count + (votes[commentId] === "down" ? -1 : 1),
                        }
                        : comment
                ),
              }
              : post
      );
      setData(updatedData); // Update the local state with the new vote count
    } catch (error) {
      console.error("Error voting:", error);
    }
  };

  // Function to toggle the visibility of comments
  const toggleComments = (postId) => {
    setOpenComments((prevOpenComments) => ({
      ...prevOpenComments,
      [postId]: !prevOpenComments[postId], // Toggle visibility
    }));
  };

  return (
      <div className="dashboard-container">
        <h1>Dashboard</h1>
        {error && <p style={{ color: "red" }}>{error}</p>}
        {data ? (
            <ul>
              {data.map((post) => (
                  <li key={post.id} className="post">
                    {/* Render the post's content */}
                    <p>
                      <strong>Post by:</strong> {post.username}
                    </p>
                    <p>
                      <strong>Content:</strong> {post.content}
                    </p>
                    <p>
                      <strong>Location:</strong> {post.location}
                    </p>
                    <p>
                      <strong>Timestamp:</strong> {new Date(post.timestamp).toLocaleString()}
                    </p>

                    {/* Toggle Comments button */}
                    <button
                        className="toggle-comments-btn"
                        onClick={() => toggleComments(post.id)}
                    >
                      {openComments[post.id] ? "Hide Comments" : "Show Comments"}
                    </button>

                    {/* Render comments if visible */}
                    {openComments[post.id] && post.comments && post.comments.length > 0 && (
                        <div className="comments-section">
                          <ul>
                            {post.comments.map((comment) => (
                                <li key={comment.id} className="comment">
                                  {/* Comment Content */}
                                  <p>
                                    <strong>{comment.username}</strong>: {comment.content}
                                  </p>
                                  <p>
                                    <strong>Timestamp:</strong> {new Date(comment.timestamp).toLocaleString()}
                                  </p>

                                  {/* Vote Section with Vote Counts */}
                                  <div className="votes-section">
                                    <button
                                        className={`vote-btn upvote ${
                                            votes[comment.id] === "up" ? "active-upvote" : ""
                                        }`}
                                        onClick={() => handleVote(comment.id, "up", post.id)}
                                    >
                                      ↑
                                    </button>
                                    <span className="vote-count">
                            {clickCounts[comment.id]?.up || 0} {/* Show upvote click count */}
                          </span>

                                    <button
                                        className={`vote-btn downvote ${
                                            votes[comment.id] === "down" ? "active-downvote" : ""
                                        }`}
                                        onClick={() => handleVote(comment.id, "down", post.id)}
                                    >
                                      ↓
                                    </button>
                                    <span className="vote-count">
                            {clickCounts[comment.id]?.down || 0} {/* Show downvote click count */}
                          </span>
                                  </div>
                                </li>
                            ))}
                          </ul>
                        </div>
                    )}
                  </li>
              ))}
            </ul>
        ) : (
            <p>Loading...</p>
        )}
      </div>
  );
}

export default Dashboard;
