package homework.blog.blog.service.db;

import homework.blog.blog.mapper.PostAuditEntityMapper;
import homework.blog.blog.model.container.PostAuditContainer;
import homework.blog.blog.repository.PostAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DbPostAuditService {

    private final PostAuditRepository postAuditRepository;
    private final PostAuditEntityMapper postAuditEntityMapper;

    public Page<PostAuditContainer> finaAllByPostId(UUID postId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        return postAuditRepository.findByPostId(postId, pageable).map(postAuditEntityMapper::toDto);
    }
}
