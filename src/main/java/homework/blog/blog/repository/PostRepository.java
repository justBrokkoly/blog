package homework.blog.blog.repository;

import homework.blog.blog.model.dto.UserPostCountDto;
import homework.blog.blog.model.entity.PostAuditEntity;
import homework.blog.blog.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface PostRepository extends JpaRepository<PostEntity, UUID> {


    @Query("""
            delete from PostEntity
            where user.id = :userId
            """)
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void deleteAllByUserId(UUID userId);

    @Query(value = """
               SELECT *  FROM posts
                           WHERE to_tsquery(:value) @@ to_tsvector('english', posts.title || posts.content)
            """,
            nativeQuery = true)
    List<PostEntity> fullTextSearch(String value);

    @Query(value = "SELECT * FROM posts WHERE tags && ARRAY[:tags]",
            countQuery = "SELECT COUNT(*) FROM posts WHERE tags && ARRAY[:tags]",
            nativeQuery = true)
    Page<PostEntity> findByTags(@Param("tags") Set<String> tags, Pageable pageable);

    @Query("SELECT NEW homework.blog.blog.model.dto.UserPostCountDto(u.username, COUNT(p)) " +
            "FROM PostEntity p JOIN p.user u " +
            "GROUP BY u.id, u.username " +
            "ORDER BY COUNT(p) DESC")
    List<UserPostCountDto> countPostsByUsername();
}
