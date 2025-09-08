package homework.blog.blog.model.container;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CommentContainer(
        UUID id,
        String content,
        Long likesCount,
        UUID postId,
        UUID userId,
        UUID parentId
) {
}
