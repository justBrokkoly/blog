package homework.blog.blog.model.container;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserContainer(
        UUID id,
        String email,
        String username
) {
}
