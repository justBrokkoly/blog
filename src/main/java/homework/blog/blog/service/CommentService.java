package homework.blog.blog.service;

import homework.blog.blog.model.container.CommentContainer;
import homework.blog.blog.model.dto.CommentRequestDto;
import homework.blog.blog.model.dto.CommentUpdateRequestDto;
import homework.blog.blog.service.db.DbCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final DbCommentService dbCommentService;

    public CommentContainer addComment(CommentRequestDto commentRequest) {
        return dbCommentService.save(CommentContainer
                .builder()
                .content(commentRequest.getContent())
                .postId(commentRequest.getPostId())
                .userId(commentRequest.getUserId())
                .parentId(commentRequest.getParentId())
                .build());

    }

    public CommentContainer updateComment(UUID commentId, CommentUpdateRequestDto commentUpdateRequest) {
        return dbCommentService.update(CommentContainer.builder()
                .content(commentUpdateRequest.getContent())
                .id(commentId)
                .build());
    }

    public void deleteComment(UUID commentId) {
        dbCommentService.deleteById(commentId);
    }

    public CommentContainer getCommentById(UUID commentId) {
        return dbCommentService.findById(commentId);
    }

    public Page<CommentContainer> getCommentsByPostId(UUID postId, int size, int page) {
        return dbCommentService.finaAllByPostId(postId, page, size);
    }

    public Page<CommentContainer> getCommentsByUserId(UUID userId, int size, int page) {
        return dbCommentService.finaAllByUserId(userId, page, size);
    }
}
