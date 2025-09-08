package homework.blog.blog.service;

import homework.blog.blog.model.container.PostAuditContainer;
import homework.blog.blog.service.db.DbPostAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostAuditService {

    private final DbPostAuditService dbPostAuditService;

    public Page<PostAuditContainer> findPostAuditHistory(UUID postId, int page, int size) {
        return dbPostAuditService.finaAllByPostId(postId, page, size);
    }
}
