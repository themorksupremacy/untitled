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
  const [isPostModalOpen, setIsPostModalOpen] = useState(false);
  const [newPostContent, setNewPostContent] = useState("");

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
                      username,
                    },
                    ...post.comments,
                  ].sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp)),
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

  const openAddPostModal = () => {
    setIsPostModalOpen(true);
  };

  const closeAddPostModal = () => {
    setIsPostModalOpen(false);
    setNewPostContent("");
  };

  const handleCreatePost = async () => {
    if (newPostContent.trim()) {
      const content = newPostContent;
      const timestamp = new Date().toISOString();

      try {
        const response = await axios.post(
            "http://localhost:8080/dashboard/posts",
            {
              content,
              userId: parseInt(userId),
              timestamp,
              location: "Esslingen",
            },
            {
              headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`,
                "Content-Type": "application/json",
              },
            }
        );

        const savedPost = response.data;

        alert("Post created successfully!");

        setData((prevData) => [
          {
            ...savedPost,
            username,
            comments: [],
          },
          ...prevData,
        ]);

        closeAddPostModal();
      } catch (err) {
        console.error(err);
        setError("Failed to create post.");
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
                <p>Your Location: Esslingen</p>
              </div>
          )}
        </div>

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

                    {/* Show/Hide Comments Button */}
                    <button
                        className="toggle-comments-btn"
                        onClick={() => toggleComments(post.id)}
                    >
                      {openComments[post.id] ? "Hide Comments" : "Show Comments"}
                    </button>

                    {/* Add Comment Button */}
                    <button
                        className="add-comment-link"
                        onClick={() => openAddCommentModal(post.id)}
                    >
                      Add Comment
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

        {isPostModalOpen && (
            <div className="modal">
              <div className="modal-content">
                <h3>Create New Post</h3>
                <textarea
                    value={newPostContent}
                    onChange={(e) => setNewPostContent(e.target.value)}
                    placeholder="Enter your post content here"
                ></textarea>
                <div className="modal-actions">
                  <button onClick={handleCreatePost}>Create</button>
                  <button onClick={closeAddPostModal}>Cancel</button>
                </div>
              </div>
            </div>
        )}

        <button className="create-post-btn" onClick={openAddPostModal}>
          Create Post
        </button>
      </div>
  );
}

export default Dashboard;
