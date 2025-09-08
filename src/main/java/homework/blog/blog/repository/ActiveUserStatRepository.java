package homework.blog.blog.repository;

import homework.blog.blog.model.entity.ActiveUserStatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ActiveUserStatRepository extends JpaRepository<ActiveUserStatEntity, UUID> {

    List<ActiveUserStatEntity> findTop10ByOrderByTotalActivityDesc();

    @Modifying
    @Query(value = "REFRESH MATERIALIZED VIEW CONCURRENTLY active_users_stats", nativeQuery = true)
    void refreshActiveUsersStats();
}
