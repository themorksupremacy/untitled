import React, { useState, useEffect } from "react";
import axios from "axios";
import "./Dashboard.css";

function Dashboard() {
  const [data, setData] = useState(null);
  const [error, setError] = useState("");
  const [votes, setVotes] = useState({});
  const [openComments, setOpenComments] = useState({});
  const [userId, setUserId] = useState(null);

  // Get userId from localStorage
  useEffect(() => {
    const storedUserId = localStorage.getItem('userId'); // Get the userId from localStorage
    if (storedUserId) {
      setUserId(storedUserId); // Set userId to state
    }
  }, []);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get("http://localhost:8080/dashboard/posts", {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
            "Content-Type": "application/json",
          },
        });
        setData(response.data);
      } catch (err) {
        console.error(err);
        setError("Failed to fetch dashboard data.");
      }
    };

    if (userId) {
      fetchData(); // Fetch data only if userId is available
    }
  }, [userId]);

  const handleVote = async (commentId, voteType, postId) => {
    setVotes((prevVotes) => ({
      ...prevVotes,
      [commentId]: prevVotes[commentId] === voteType ? null : voteType,
    }));

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
    setData(updatedData);

    try {
      await axios.post(`http://localhost:8080/dashboard/comments/${commentId}/vote`, {
        userId: userId, // Use the userId from localStorage
        type: voteType === "up",
      });
    } catch (error) {
      console.error("Error voting:", error);
    }
  };

  const toggleComments = (postId) => {
    setOpenComments((prevOpenComments) => ({
      ...prevOpenComments,
      [postId]: !prevOpenComments[postId],
    }));
  };

  const handleAddComment = (postId) => {
    alert(`Add Comment button clicked for post ID: ${postId}`);
  };

  const handleCreatePost = () => {
    alert("Create Post button clicked!");
  };

  return (
    <div className="dashboard-container">
      <h1>Jodel</h1>
      {error && <p style={{ color: "red" }}>{error}</p>}
      {data ? (
        <ul>
          {data.map((post) => (
            <li key={post.id} className="post">
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
                <strong>Timestamp:</strong>{" "}
                {new Date(post.timestamp).toLocaleString()}
              </p>
              <button
                className="toggle-comments-btn"
                onClick={() => toggleComments(post.id)}
              >
                {openComments[post.id] ? "Hide Comments" : "Show Comments"}
              </button>
              {openComments[post.id] && post.comments && post.comments.length > 0 && (
                <div className="comments-section">
                  <ul>
                    {post.comments.map((comment) => (
                      <li key={comment.id} className="comment">
                        <p>
                          <strong>{comment.username}</strong>: {comment.content}
                        </p>
                        <p>
                          <strong>Timestamp:</strong>{" "}
                          {new Date(comment.timestamp).toLocaleString()}
                        </p>
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
      <button className="create-post-btn" onClick={handleCreatePost}>
        Create Post
      </button>
    </div>
  );
}

export default Dashboard;
