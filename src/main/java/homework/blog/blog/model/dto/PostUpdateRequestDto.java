package homework.blog.blog.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostUpdateRequestDto {
    private String title;
    private String content;
    private List<String> tags;
}
