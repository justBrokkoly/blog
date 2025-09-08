package homework.blog.blog.service.db;

import homework.blog.blog.mapper.LikeEntityMapper;
import homework.blog.blog.model.container.LikeContainer;
import homework.blog.blog.model.entity.LikeId;
import homework.blog.blog.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DbLikeService {

    private final LikeRepository likeRepository;
    private final LikeEntityMapper likeEntityMapper;

    public LikeContainer save(LikeContainer likeContainer) {
        var like = likeRepository.save(likeEntityMapper.toEntity(likeContainer));
        return likeEntityMapper.toDto(like);
    }

    public void deleteById (LikeId likeId){
        likeRepository.deleteById(likeId);
    }
}
