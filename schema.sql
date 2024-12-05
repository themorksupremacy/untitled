-- Create User Table
DROP TABLE IF EXISTS Vote;
DROP TABLE IF EXISTS Comment;
DROP TABLE IF EXISTS Post;
DROP TABLE IF EXISTS EndUser;

CREATE TABLE EndUser(
    id bigint PRIMARY KEY,
    username varchar(255),
    passwordHash varchar(255),
    email varchar(255)
);
INSERT INTO EndUser(id, username, passwordHash, email) VALUES(1, 'Mork', '1234', 'mork@mail.com');
INSERT INTO EndUser(id, username, passwordHash, email) VALUES(2, 'Kieran', '1234', 'kieran@mail.com');

-- Create Post Table
CREATE TABLE Post(
    id bigint PRIMARY KEY,
    content varchar(255),
    timestamp timestamp,       -- Use 'timestamp' instead of 'DateTime'
    location varchar(255),
    userid bigint,
    CONSTRAINT fk_user FOREIGN KEY (userid) REFERENCES EndUser(id)
);
INSERT INTO Post(id, content, timestamp, location, userid) VALUES(1, 'Went to class!', '2024-11-09 15:45:21', 'Esslingen', 1);
INSERT INTO Post(id, content, timestamp, location, userid) VALUES(2, 'Did not go to class!', '2024-11-09 15:45:21', 'Esslingen', 2);

-- Create Comment Table
CREATE TABLE Comment(
    id bigint PRIMARY KEY,
    content varchar(255),
    timestamp timestamp,       -- Use 'timestamp' instead of 'DateTime'
    postId bigint,
    CONSTRAINT fk_post FOREIGN KEY (postId) REFERENCES Post(id)
);
INSERT INTO Comment(id, content, timestamp, postId) VALUES(1, 'Good job!', '2024-11-09 15:45:21', 2);
INSERT INTO Comment(id, content, timestamp, postId) VALUES(2, 'Well done!', '2024-11-09 15:45:21', 1);

-- Create Vote Table
CREATE TABLE Vote(
    id bigint PRIMARY KEY,
    type Boolean,
    userId bigint,
    commentId bigint,
    CONSTRAINT fk_user_vote FOREIGN KEY (userId) REFERENCES EndUser(id),
    CONSTRAINT fk_comment_vote FOREIGN KEY (commentId) REFERENCES Comment(id)
);
-- Assuming Vote table has initial votes
INSERT INTO Vote(id, type, userId, commentId) VALUES(1, TRUE, 1, 1);
INSERT INTO Vote(id, type, userId, commentId) VALUES(2, FALSE, 2, 2);
