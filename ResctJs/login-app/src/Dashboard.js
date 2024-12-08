import React, { useState, useEffect } from "react";
import axios from "axios";
import "./Dashboard.css";

function Dashboard() {
  const [data, setData] = useState(null);
  const [error, setError] = useState("");
  const [votes, setVotes] = useState({});
  const [openComments, setOpenComments] = useState({});
  const [userId, setUserId] = useState(null);
  const [username, setUsername] = useState(""); // State to store the username
  const [location, setLocation] = useState(null); // State to store location
  const [locationError, setLocationError] = useState(""); // State to store location errors
  const [isModalOpen, setIsModalOpen] = useState(false); // State for modal
  const [newComment, setNewComment] = useState(""); // State for new comment content
  const [activePostId, setActivePostId] = useState(null); // State for active post ID

  // Get userId from localStorage
  useEffect(() => {
    const storedUserId = localStorage.getItem("userId");
    if (storedUserId) {
      setUserId(storedUserId); // Set userId to state
    }
  }, []);

  // Fetch all users and find the username of the logged-in user
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

  // Fetch data and handle voting
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

        const initialVotes = {};
        response.data.forEach((post) => {
          post.comments.forEach((comment) => {
            initialVotes[comment.id] = comment.userVoteType;
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

  // Get the user's location
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

  const toggleComments = (postId) => {
    setOpenComments((prevOpenComments) => ({
      ...prevOpenComments,
      [postId]: !prevOpenComments[postId],
    }));
  };

  const openAddCommentModal = (postId) => {
    setActivePostId(postId); // Set the active post ID
    setIsModalOpen(true); // Open the modal
  };

  const closeAddCommentModal = () => {
    setIsModalOpen(false);
    setNewComment("");
  };

  const handleSendComment = async () => {
    try {
      const response = await axios.post(
          `http://localhost:8080/dashboard/posts/${activePostId}/comments`,
          {
            content: newComment,
            userId,
          },
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
              "Content-Type": "application/json",
            },
          }
      );
      // Update the data with the new comment
      const updatedData = data.map((post) =>
          post.id === activePostId
              ? { ...post, comments: [...post.comments, response.data] }
              : post
      );
      setData(updatedData);
      closeAddCommentModal();
    } catch (err) {
      console.error("Failed to add comment:", err);
      setError("Failed to add comment.");
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
      </div>
  );
}

export default Dashboard;
