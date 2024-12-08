import React, { useState, useEffect } from "react";
import axios from "axios";
import "./Dashboard.css";

function Dashboard() {
  const [data, setData] = useState(null);
  const [error, setError] = useState("");
  const [votes, setVotes] = useState({});
  const [openComments, setOpenComments] = useState({});
  const [userId, setUserId] = useState(null);
  const [username, setUsername] = useState("");
  const [location, setLocation] = useState(null);
  const [locationError, setLocationError] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [newComment, setNewComment] = useState("");
  const [commentingPostId, setCommentingPostId] = useState(null);

  useEffect(() => {
    const storedUserId = localStorage.getItem("userId");
    if (storedUserId) {
      setUserId(storedUserId);
    }
  }, []);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await axios.get("http://localhost:8080/users", {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
            "Content-Type": "application/json",
          },
        });
        const users = response.data;
        const loggedInUser = users.find((user) => user.id === parseInt(userId));
        if (loggedInUser) {
          setUsername(loggedInUser.username);
        }
      } catch (err) {
        console.error(err);
        setError("Failed to fetch users.");
      }
    };

    if (userId) {
      fetchUsers();
    }
  }, [userId]);

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

        // Initialize votes state based on userVoteType
        const initialVotes = {};
        response.data.forEach((post) => {
          post.comments.forEach((comment) => {
            initialVotes[comment.id] = comment.userVoteType; // Fetch userVoteType from the server
          });
        });
        setVotes(initialVotes);
      } catch (err) {
        console.error(err);
        setError("Failed to fetch dashboard data.");
      }
    };

    if (userId) {
      fetchData();
    }
  }, [userId]);

  useEffect(() => {
    if ("geolocation" in navigator) {
      navigator.geolocation.getCurrentPosition(
          (position) => {
            const { latitude, longitude } = position.coords;
            setLocation({ latitude, longitude });
          },
          (err) => {
            setLocationError("Failed to retrieve location: " + err.message);
          }
      );
    } else {
      setLocationError("Geolocation is not supported by this browser.");
    }
  }, []);

  const handleVote = async (commentId, voteType, postId) => {
    const previousVote = votes[commentId];
    let updatedVoteType = voteType;

    if (previousVote === voteType) {
      updatedVoteType = null; // Nullify the vote
    }

    setVotes((prevVotes) => ({
      ...prevVotes,
      [commentId]: updatedVoteType,
    }));

    setData((prevData) =>
        prevData.map((post) => {
          if (post.id === postId) {
            return {
              ...post,
              comments: post.comments.map((comment) => {
                if (comment.id === commentId) {
                  let upvoteCount = comment.upvoteCount || 0;
                  let downvoteCount = comment.downvoteCount || 0;

                  if (previousVote === "up") upvoteCount--;
                  if (previousVote === "down") downvoteCount--;

                  if (updatedVoteType === "up") upvoteCount++;
                  if (updatedVoteType === "down") downvoteCount++;

                  return {
                    ...comment,
                    upvoteCount,
                    downvoteCount,
                  };
                }
                return comment;
              }),
            };
          }
          return post;
        })
    );

    try {
      await axios.post(
          `http://localhost:8080/dashboard/comments/${commentId}/vote`,
          {
            userId,
            type: updatedVoteType === "up" ? true : updatedVoteType === "down" ? false : null,
          },
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
              "Content-Type": "application/json",
            },
          }
      );
    } catch (err) {
      console.error(err);
      setError("Failed to register vote.");
    }
  };

  const toggleComments = (postId) => {
    setOpenComments((prevOpenComments) => ({
      ...prevOpenComments,
      [postId]: !prevOpenComments[postId],
    }));
  };

  const openAddCommentModal = (postId) => {
    setCommentingPostId(postId);
    setIsModalOpen(true);
  };

  const closeAddCommentModal = () => {
    setIsModalOpen(false);
    setNewComment("");
    setCommentingPostId(null);
  };

  const handleSendComment = async () => {
    if (newComment.trim()) {
      const content = newComment;
      const timestamp = new Date().toISOString();

      try {
        const response = await axios.post(
            "http://localhost:8080/dashboard/comments",
            {
              content,
              postId: commentingPostId,
              userId,
              timestamp,
            },
            {
              headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`,
                "Content-Type": "application/json",
              },
            }
        );

        const savedComment = response.data;

        alert("Comment added successfully!");

        setData((prevData) =>
            prevData.map((post) => {
              if (post.id === commentingPostId) {
                return {
                  ...post,
                  comments: [
                    {
                      ...savedComment,
                      username, // Add the current user's username
                    },
                    ...post.comments, // Keep existing comments below
                  ].sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp)), // Sort by timestamp
                };
              }
              return post;
            })
        );

        setNewComment("");
        closeAddCommentModal();
      } catch (err) {
        console.error(err);
        setError("Failed to add comment.");
      }
    }
  };

  return (
      <div className="dashboard-container">
        <div className="dashboard-header">
          <h1>Jodel</h1>
          {username && (
              <div className="user-info">
                Logged in as: <strong>{username}</strong>
              </div>
          )}
        </div>

        {locationError && <p style={{ color: "red" }}>{locationError}</p>}

        {location ? (
            <div>
              <p>Your current location:</p>
              <p>Latitude: {location.latitude}</p>
              <p>Longitude: {location.longitude}</p>
            </div>
        ) : (
            <p>Loading your location...</p>
        )}

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
                    {openComments[post.id] &&
                        post.comments &&
                        post.comments.length > 0 && (
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
                                                votes[comment.id] === "up"
                                                    ? "active-upvote"
                                                    : ""
                                            }`}
                                            onClick={() =>
                                                handleVote(comment.id, "up", post.id)
                                            }
                                        >
                                          ↑
                                        </button>
                                        <span className="vote-count">
                              {comment.upvoteCount || 0}
                            </span>
                                        <button
                                            className={`vote-btn downvote ${
                                                votes[comment.id] === "down"
                                                    ? "active-downvote"
                                                    : ""
                                            }`}
                                            onClick={() =>
                                                handleVote(comment.id, "down", post.id)
                                            }
                                        >
                                          ↓
                                        </button>
                                        <span className="vote-count">
                              {comment.downvoteCount || 0}
                            </span>
                                      </div>
                                    </li>
                                ))}
                              </ul>
                              <button
                                  className="add-comment-btn"
                                  onClick={() => openAddCommentModal(post.id)}
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

        {isModalOpen && (
            <div className="modal">
              <div className="modal-content">
                <h3>Add Comment</h3>
                <textarea
                    value={newComment}
                    onChange={(e) => setNewComment(e.target.value)}
                    placeholder="Enter your comment here"
                ></textarea>
                <div className="modal-actions">
                  <button onClick={handleSendComment}>Send</button>
                  <button onClick={closeAddCommentModal}>Cancel</button>
                </div>
              </div>
            </div>
        )}
        <button
            className="create-post-btn"
            onClick={() => alert("Create Post button clicked!")}
        >
          Create Post
        </button>
      </div>
  );
}

export default Dashboard;