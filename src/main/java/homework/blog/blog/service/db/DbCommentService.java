package homework.blog.blog.service.db;

import homework.blog.blog.mapper.CommentEntityMapper;
import homework.blog.blog.model.container.CommentContainer;
import homework.blog.blog.model.container.PostAuditContainer;
import homework.blog.blog.model.entity.CommentEntity;
import homework.blog.blog.repository.CommentRepository;
import homework.blog.blog.repository.LikeRepository;
import homework.blog.blog.repository.PostRepository;
import homework.blog.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DbCommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentEntityMapper commentEntityMapper;
    private final LikeRepository likeRepository;


    public CommentContainer save(CommentContainer container) {
        var user = userRepository.getReferenceById(container.userId());
        var post = postRepository.getReferenceById(container.postId());
        var reply = Optional.ofNullable(container.parentId())
                .map(commentRepository::getReferenceById)
                .orElse(null);
        var comment = commentRepository.save(commentEntityMapper.toEntity(container, reply, user, post));
        return commentEntityMapper.toDto(comment);
    }

    public CommentContainer findById(UUID commentId) {
        return commentRepository.findById(commentId)
                .map(commentEntityMapper::toDto)
                .orElseThrow();
    }

    @Transactional
    public void deleteById(UUID commentId) {
        likeRepository.deleteAllByCommentId(commentId);
        commentRepository.deleteById(commentId);
    }

    @Transactional
    public CommentContainer update(CommentContainer updatedComment) {
        var comment = commentRepository.findById(updatedComment.id())
                .orElseThrow();
        var source = commentEntityMapper.toEntity(updatedComment);
        commentEntityMapper.merge(source, comment);
        return commentEntityMapper.toDto(comment);
    }

    public Page<CommentContainer> finaAllByPostId(UUID postId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        return commentRepository.findByPostId(postId, pageable).map(commentEntityMapper::toDto);
    }

    public Page<CommentContainer> finaAllByUserId(UUID userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        return commentRepository.findByUserId(userId, pageable).map(commentEntityMapper::toDto);
    }

}
