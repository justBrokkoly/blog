--liquibase formatted sql

--changeset kkustov:8-create_post_audit_log
--preconditions onFail:MARK_RAN
create table if not exists post_audit_log
(
    id             uuid primary key      default uuidv7(),
    post_id        uuid         not null,
    operation_type varchar(255) not null,
    changed_at     timestamptz           DEFAULT CURRENT_TIMESTAMP,
    created_at     timestamptz  not null DEFAULT CURRENT_TIMESTAMP,
    changed_by     varchar(255) not null DEFAULT CURRENT_USER,
    old_title      varchar(255) not null,
    new_title      varchar(255)
);
CREATE INDEX if not exists post_audit_log_post_id_idx ON post_audit_log (post_id);


--changeset kkustov:9-create_audit_post_function splitStatements:false
--preconditions onFail:MARK_RAN
CREATE OR REPLACE FUNCTION audit_post_function()
    RETURNS TRIGGER AS
$$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO blog.post_audit_log (operation_type, post_id, old_title)
        VALUES ('DELETE', OLD.id, OLD.title);
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO blog.post_audit_log (operation_type, post_id, old_title, new_title)
        VALUES ('UPDATE', NEW.id, OLD.title, NEW.title);
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

--changeset kkustov:10-create_trigger_post_after_update_trigger splitStatements:false
--preconditions onFail:MARK_RAN
CREATE TRIGGER post_after_update_trigger
    AFTER UPDATE OF title
    ON posts
    FOR EACH ROW
    WHEN (OLD.title IS DISTINCT FROM NEW.title)
EXECUTE FUNCTION audit_post_function();

--changeset kkustov:11-create_trigger_post_after_delete_trigger splitStatements:false
--preconditions onFail:MARK_RAN
CREATE TRIGGER post_after_delete_trigger
    AFTER DELETE
    ON posts
    FOR EACH ROW
EXECUTE FUNCTION audit_post_function();