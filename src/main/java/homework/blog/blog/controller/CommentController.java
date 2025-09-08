package homework.blog.blog.controller;

import homework.blog.blog.model.container.CommentContainer;
import homework.blog.blog.model.dto.CommentRequestDto;
import homework.blog.blog.model.dto.CommentUpdateRequestDto;
import homework.blog.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping("/public/comment")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public CommentContainer addComment(@RequestBody CommentRequestDto commentRequest) {
        return commentService.addComment(commentRequest);

    }

    @PostMapping("/{commentId}")
    public CommentContainer updateComment(@PathVariable UUID commentId,
                                          @RequestBody CommentUpdateRequestDto commentUpdateRequest) {
        return commentService.updateComment(commentId, commentUpdateRequest);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable UUID commentId) {
        commentService.deleteComment(commentId);
    }

    @GetMapping("/{commentId}")
    public CommentContainer getCommentById(@PathVariable UUID commentId) {
        return commentService.getCommentById(commentId);
    }

    @GetMapping("/post/{postId}")
    public Page<CommentContainer> getCommentsByPostId(@PathVariable UUID postId,
                                                      @RequestParam(value = "size") int size,
                                                      @RequestParam(value = "page") int page) {
        return commentService.getCommentsByPostId(postId, page, size);
    }

    @GetMapping("/user/{userId}")
    public Page<CommentContainer> getCommentsByUserId(@PathVariable UUID userId,
                                                      @RequestParam(value = "size") int size,
                                                      @RequestParam(value = "page") int page) {
        return commentService.getCommentsByUserId(userId, page, size);
    }


}
