package homework.blog.blog.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PostCreateRequestDto {
    private String title;
    private String content;
    private List<String> tags;
    private UUID userId;
}
