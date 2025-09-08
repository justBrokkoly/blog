-- Insert test users
INSERT INTO blog.users (email, username, created_at, updated_at) VALUES
                                                                ('alice@example.com', 'alice', NOW(), NOW()),
                                                                ('bob@example.com', 'bob', NOW(), NOW()),
                                                                ('charlie@example.com', 'charlie', NOW(), NOW());



-- Create posts
INSERT INTO blog.posts (title, content, author_id, created_at, updated_at, tags) VALUES
                                                                                ('First Post1', 'Content of the first post',
                                                                                 (SELECT id FROM blog.users WHERE email = 'alice@example.com'),
                                                                                 NOW(), NOW(), ARRAY['tech', 'programming']),
                                                                                ('Second Post1', 'Content of the second post',
                                                                                 (SELECT id FROM blog.users WHERE email = 'bob@example.com'),
                                                                                 NOW(), NOW(), ARRAY['design', 'art']),
                                                                                ('Third Post1', 'Content of the third post about programming',
                                                                                 (SELECT id FROM blog.users WHERE email = 'alice@example.com'),
                                                                                 NOW(), NOW(), ARRAY['tech', 'tutorial']);

select  * from blog.posts;

-- Test tag search
SELECT * FROM blog.posts WHERE tags @> ARRAY['tech'];
SELECT * FROM blog.posts WHERE tags && ARRAY['tech', 'design'];


-- Test full-text search
SELECT *  FROM blog.posts WHERE to_tsquery('programming') @@ to_tsvector('english', blog.posts.title || blog.posts.content);


-- Add comments
INSERT INTO blog.comments (content, author_id, post_id, created_at, updated_at) VALUES
                                                                               ('Great post!',
                                                                                (SELECT id FROM blog.users WHERE email = 'bob@example.com'),
                                                                                (SELECT id FROM blog.posts WHERE title = 'First Post1'),
                                                                                NOW(), NOW()),
                                                                               ('Thanks for sharing',
                                                                                (SELECT id FROM blog.users WHERE email = 'charlie@example.com'),
                                                                                (SELECT id FROM blog.posts WHERE title = 'First Post1'),
                                                                                NOW(), NOW());

SELECT * FROM blog.comments WHERE post_id = (SELECT id FROM blog.posts WHERE title = 'First Post1');


-- Like posts and comments
INSERT INTO blog.likes (user_id, target_id,target_type, created_at) VALUES
    ((SELECT id FROM blog.users WHERE email = 'bob@example.com'),
     (SELECT id FROM blog.posts WHERE title = 'First Post1'),'POST',
     NOW());

INSERT INTO blog.likes (user_id, target_id,target_type, created_at) VALUES
    ((SELECT id FROM blog.users WHERE email = 'charlie@example.com'),
     (SELECT id FROM blog.posts WHERE title = 'First Post1'),'POST',
     NOW());

INSERT INTO blog.likes (user_id, target_id,target_type, created_at) VALUES
    ((SELECT id FROM blog.users WHERE email = 'alice@example.com'),
     (SELECT id FROM blog.comments WHERE content = 'Great post!'),'COMMENT',
     NOW());

-- Verify like counts
SELECT title, likes_count FROM blog.posts;
SELECT content, likes_count FROM blog.comments;

-- Unlike a post
DELETE FROM blog.likes
WHERE user_id = (SELECT id FROM blog.users WHERE email = 'charlie@example.com')
  AND target_type = 'POST'
  AND target_id = (SELECT id FROM blog.posts WHERE title = 'First Post1');

-- Verify like count decreased
SELECT title, likes_count FROM blog.posts WHERE title = 'First Post1';


-- Update post title (should trigger audit log)
UPDATE blog.posts
SET title = 'Updated First Post', updated_at = NOW()
WHERE title = 'First Post1';

INSERT INTO blog.posts (title, content, author_id, created_at, updated_at, tags) VALUES
                                                                                     ('Audit Delete', 'Content of the delete post',
                                                                                      (SELECT id FROM blog.users WHERE email = 'alice@example.com'),
                                                                                      NOW(), NOW(), ARRAY['tech', 'programming']);
-- Delete a post (should trigger audit log)
DELETE FROM blog.posts WHERE title = 'Audit Delete';

-- Check audit log
SELECT * FROM blog.post_audit_log;


-- Refresh materialized view to capture recent activity
REFRESH MATERIALIZED VIEW CONCURRENTLY blog.active_users_stats;

-- Query the materialized view
SELECT * FROM blog.active_users_stats ORDER BY total_activity DESC;

