import React, { useState, useEffect } from "react";
import axios from "axios";
import "./Dashboard.css";

function Dashboard({ token }) {
  const [data, setData] = useState(null);
  const [error, setError] = useState("");
  const [votes, setVotes] = useState({}); // Track votes for each comment
  const [openComments, setOpenComments] = useState({}); // Track open/close state of comments for each post

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
    // Toggle the vote state
    setVotes((prevVotes) => {
      const currentVote = prevVotes[commentId];
      return { ...prevVotes, [commentId]: currentVote === voteType ? null : voteType };
    });

    // Update the local UI immediately
    const updatedData = data.map((post) =>
        post.id === postId
            ? {
              ...post,
              comments: post.comments.map((comment) =>
                  comment.id === commentId
                      ? {
                        ...comment,
                        upvoteCount:
                            voteType === "up"
                                ? comment.upvoteCount + (votes[commentId] === "up" ? -1 : 1)
                                : comment.upvoteCount - (votes[commentId] === "up" ? 1 : 0),
                        downvoteCount:
                            voteType === "down"
                                ? comment.downvoteCount + (votes[commentId] === "down" ? -1 : 1)
                                : comment.downvoteCount - (votes[commentId] === "down" ? 1 : 0),
                      }
                      : comment
              ),
            }
            : post
    );

    setData(updatedData); // Update the state with new vote counts

    // Send the vote to the backend API
    try {
      await axios.post(`http://localhost:8080/comments/${commentId}/vote`, {
        userId: 1, // Replace with the actual user ID
        voteType,
      });
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

  // Function to handle adding a comment (placeholder for now)
  const handleAddComment = (postId) => {
    alert(`Add Comment button clicked for post ID: ${postId}`);
  };

  // Function to handle creating a post (placeholder for now)
  const handleCreatePost = () => {
    alert("Create Post button clicked!");
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
                                    <strong>Timestamp:</strong>{" "}
                                    {new Date(comment.timestamp).toLocaleString()}
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
                                    <span className="vote-count">{comment.upvoteCount || 0}</span>
                                    <button
                                        className={`vote-btn downvote ${
                                            votes[comment.id] === "down" ? "active-downvote" : ""
                                        }`}
                                        onClick={() => handleVote(comment.id, "down", post.id)}
                                    >
                                      ↓
                                    </button>
                                    <span className="vote-count">{comment.downvoteCount || 0}</span>
                                  </div>
                                </li>
                            ))}
                          </ul>
                          {/* Add Comment Button */}
                          <button
                              className="add-comment-btn"
                              onClick={() => handleAddComment(post.id)}
                          >
                            Add Comment
                          </button>
                        </div>
                    )}
                  </li>
              ))}
            </ul>
        ) : (
            <p>Loading...</p>
        )}
        {/* Create Post Button */}
        <button className="create-post-btn" onClick={handleCreatePost}>
          Create Post
        </button>
      </div>
  );
}

export default Dashboard;
