package homework.blog.blog.model.container;

import homework.blog.blog.model.enums.TargetTypeLikeEnum;
import lombok.Builder;

import java.util.UUID;

@Builder
public record LikeContainer(
        UUID userId,
        TargetTypeLikeEnum targetType,
        UUID targetId
) {
}
