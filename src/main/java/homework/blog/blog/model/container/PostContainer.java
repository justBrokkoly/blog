package homework.blog.blog.model.container;

import lombok.Builder;
import lombok.With;

import java.util.List;
import java.util.UUID;

@Builder
public record PostContainer(
        UUID id,
        String title,
        String content,
        Long likesCount,
        @With List<String> tags,
        UUID userId
) {
}
