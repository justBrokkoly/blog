package homework.blog.blog.service;

import homework.blog.blog.model.container.LikeContainer;
import homework.blog.blog.model.dto.LikeRequestDto;
import homework.blog.blog.model.entity.LikeId;
import homework.blog.blog.service.db.DbLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final DbLikeService dbLikeService;

    public void updateLike(LikeRequestDto likeRequestDto) {
        switch (likeRequestDto.getLikeActionType()) {
            case LIKE -> dbLikeService.save(LikeContainer.builder()
                    .userId(likeRequestDto.getUserId())
                    .targetType(likeRequestDto.getTypeLike())
                    .targetId(likeRequestDto.getTargetId())
                    .build());
            case DISLIKE -> dbLikeService.deleteById(LikeId.builder()
                    .userId(likeRequestDto.getUserId())
                    .targetType(likeRequestDto.getTypeLike())
                    .targetId(likeRequestDto.getTargetId())
                    .build());
        }
    }
}
