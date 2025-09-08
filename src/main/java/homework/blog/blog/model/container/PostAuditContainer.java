package homework.blog.blog.model.container;

import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
public record PostAuditContainer(
        UUID id,
        UUID postId,
        String operationType,
        String changedBy,
        ZonedDateTime changedAt,
        String newTitle,
        String oldTitle
) {
}
