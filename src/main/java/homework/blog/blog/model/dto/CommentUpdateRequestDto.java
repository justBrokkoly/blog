package homework.blog.blog.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentUpdateRequestDto {
    private String commentId;
    private String content;
}
