package homework.blog.blog.repository;

import homework.blog.blog.model.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<CommentEntity, UUID> {

    @Query("""
            delete from CommentEntity
            where post.id = :postId
            """)
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void deleteAllByPostId(UUID postId);

    @Query("""
            delete from CommentEntity
            where user.id = :userId
            """)
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void deleteAllByUserId(UUID userId);

    Page<CommentEntity> findByPostId(UUID postId, Pageable pageable);

    Page<CommentEntity> findByUserId(UUID userId, Pageable pageable);
}
