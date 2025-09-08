package homework.blog.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class UserPostCountDto {
    private String username;
    private Long postCount;
}
