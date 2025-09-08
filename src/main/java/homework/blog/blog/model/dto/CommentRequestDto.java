package homework.blog.blog.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CommentRequestDto {
    private UUID userId;
    private UUID postId;
    private UUID parentId;
    private String content;
}
