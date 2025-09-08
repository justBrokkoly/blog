package homework.blog.blog.model.dto;

import homework.blog.blog.model.enums.LikeActionType;
import homework.blog.blog.model.enums.TargetTypeLikeEnum;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class LikeRequestDto {
    private UUID userId;
    private UUID targetId;
    private TargetTypeLikeEnum typeLike;
    private LikeActionType likeActionType;
}
