--liquibase formatted sql

--changeset kkustov:1
--preconditions onFail:MARK_RAN
create table if not exists users
(
    id         uuid primary key default uuidv7(),
    email      varchar(255) UNIQUE not null,
    username   varchar(255) UNIQUE not null,
    created_at timestamptz         not null,
    updated_at timestamptz         not null
);
CREATE INDEX users_email_ixs
    ON users (email)
    INCLUDE (id, username);

--changeset kkustov:2
--preconditions onFail:MARK_RAN
--comment: для тэгов использую массив, потому что операции поиска, вставка при первичном создании будут быстрее,
--чем у реализации с many to many, обновление мб медленее, потому что в many to many нужно просто вставить запись,
--а в реализации с массивом ее нужно обновить. Но тэги редко когда обновляют вообще. Ориентировался на статьи из этой серии http://www.databasesoup.com/2015/01/tag-all-things-part-3.html?m=1
create table if not exists posts
(
    id          uuid primary key      default uuidv7(),
    title       varchar(255) not null,
    content     text         not null,
    likes_count bigint                default 0,
    tags        text[]       not null default '{}',
    author_id   uuid         not null,
    created_at  timestamptz  not null,
    updated_at  timestamptz  not null,
    foreign key (author_id) references users (id)
);
CREATE INDEX if not exists posts_tags_idx ON posts using gin (tags);
CREATE INDEX if not exists posts_search_idx ON posts USING GIN (to_tsvector('english', posts.title || posts.content));
CREATE INDEX if not exists posts_author_id_idx ON posts (author_id);
CREATE INDEX if not exists posts_created_at_idx ON posts (created_at);

--changeset kkustov:3
--preconditions onFail:MARK_RAN
create table if not exists comments
(
    id          uuid primary key default uuidv7(),
    content     text        not null,
    author_id   uuid        not null,
    post_id     uuid        not null,
    parent_id   uuid,
    likes_count bigint           default 0,
    created_at  timestamptz not null,
    updated_at  timestamptz not null,
    foreign key (author_id) references users (id),
    foreign key (post_id) references posts (id),
    foreign key (parent_id) references comments (id)
);
CREATE INDEX if not exists comments_post_id_idx ON comments (post_id, author_id);
CREATE INDEX if not exists comments_author_id_idx ON comments (author_id);
CREATE INDEX if not exists comments_parent_id_idx ON comments (parent_id);
CREATE INDEX if not exists comments_created_at_idx ON posts (created_at);

--changeset kkustov:4
--preconditions onFail:MARK_RAN
create table if not exists likes
(
    user_id    uuid        not null,
    target_id    uuid      not null,
    target_type  varchar(100)        not null ,
    created_at timestamptz not null,
    primary key (user_id, target_id, target_type),
    foreign key (user_id) references users (id)
);
CREATE INDEX if not exists likes_post_id_idx ON likes (target_type,target_id);

--changeset kkustov:5 splitStatements:false
--preconditions onFail:MARK_RAN
CREATE OR REPLACE FUNCTION update_likes_count()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF TG_OP = 'INSERT' THEN
        IF NEW.target_type = 'POST' THEN
            UPDATE blog.posts
            SET likes_count = likes_count + 1
            WHERE id = NEW.target_id;
        ELSIF NEW.target_type = 'COMMENT' THEN
            UPDATE blog.comments
            SET likes_count = likes_count + 1
            WHERE id = NEW.target_id;
        END IF;
        RETURN NEW;

    ELSIF TG_OP = 'DELETE' THEN
        IF OLD.target_type = 'POST' THEN
            UPDATE blog.posts
            SET likes_count = likes_count - 1
            WHERE id = OLD.target_id;
        ELSIF OLD.target_type = 'COMMENT' THEN
            UPDATE blog.comments
            SET likes_count = likes_count - 1
            WHERE id = OLD.target_id;
        END IF;
        RETURN OLD;
    END IF;

    RETURN NULL;
END;
$$;

--changeset kkustov:6 splitStatements:false
--preconditions onFail:MARK_RAN
CREATE TRIGGER after_like_insert
    AFTER INSERT
    ON likes
    FOR EACH ROW
EXECUTE FUNCTION update_likes_count();

--changeset kkustov:7 splitStatements:false
--preconditions onFail:MARK_RAN
CREATE TRIGGER after_like_delete
    AFTER DELETE
    ON likes
    FOR EACH ROW
EXECUTE FUNCTION update_likes_count();

