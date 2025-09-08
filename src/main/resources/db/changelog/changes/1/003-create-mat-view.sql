--liquibase formatted sql

--changeset kkustov:11-create_mat_view_active_users_stats
--preconditions onFail:MARK_RAN
CREATE MATERIALIZED VIEW active_users_stats AS
SELECT u.id                                                                           AS user_id,
       u.username,
       COUNT(p.id)                                                                    AS post_count,
       COUNT(c.id)                                                                    AS comment_count,
       COUNT(l.created_at)                                                            AS like_count,
       (COUNT(p.id) + COUNT(c.id) + COUNT(l.created_at))                              AS total_activity,
       GREATEST(NOW()::date, MAX(p.created_at), MAX(c.created_at), MAX(l.created_at)) AS last_activity_date
FROM users u
         LEFT JOIN posts p ON u.id = p.author_id AND p.created_at >= NOW() - INTERVAL '10 days'
         LEFT JOIN comments c ON u.id = c.author_id AND c.created_at >= NOW() - INTERVAL '10 days'
         LEFT JOIN likes l ON u.id = l.user_id AND l.created_at >= NOW() - INTERVAL '10 days'
GROUP BY u.id, u.username
ORDER BY total_activity DESC;
CREATE UNIQUE INDEX idx_active_users_stats_user_id ON active_users_stats (user_id);
CREATE INDEX idx_active_users_stats_total_activity ON active_users_stats (total_activity DESC);