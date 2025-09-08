package homework.blog.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PostSearchResponseDto {
    List<PostDto> posts;
}
