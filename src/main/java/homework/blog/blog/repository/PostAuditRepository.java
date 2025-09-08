package homework.blog.blog.repository;

import homework.blog.blog.model.entity.PostAuditEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PostAuditRepository extends JpaRepository<PostAuditEntity, UUID> {

    Page<PostAuditEntity> findByPostId(UUID postId, Pageable pageable);

    @Query("""
            delete from PostAuditEntity
            where postId = :postId
            """)
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void deleteAllByPostId(UUID postId);
}
