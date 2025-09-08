package homework.blog.blog.model.container;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ActiveUserStatContainer(
        UUID userId,
        String username,
        Long postCount,
        Long commentCount,
        Long likeCount,
        Long totalActivity,
        LocalDateTime lastActivityDate
) {
}
