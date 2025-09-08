package homework.blog.blog.repository;

import homework.blog.blog.model.entity.LikeEntity;
import homework.blog.blog.model.entity.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface LikeRepository extends JpaRepository<LikeEntity, LikeId> {

    @Query("""
            delete from LikeEntity
            where id.targetType = 'POST' and id.targetId = :postId
            """)
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void deleteAllByPostId(UUID postId);

    @Query("""
            delete from LikeEntity
            where id.targetType = 'COMMENT' and id.targetId = :userId
            """)
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void deleteAllByUserId(UUID userId);

    @Query("""
            delete from LikeEntity
            where id.targetType = 'COMMENT' and id.targetId = :commentId
            """)
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void deleteAllByCommentId(UUID commentId);
}
