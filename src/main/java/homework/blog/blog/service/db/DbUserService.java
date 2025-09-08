package homework.blog.blog.service.db;

import homework.blog.blog.mapper.UserEntityMapper;
import homework.blog.blog.model.container.UserContainer;
import homework.blog.blog.repository.CommentRepository;
import homework.blog.blog.repository.LikeRepository;
import homework.blog.blog.repository.PostRepository;
import homework.blog.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DbUserService {

    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public UserContainer save(UserContainer userContainer) {
        var user = userRepository.save(userEntityMapper.toEntity(userContainer));
        return userEntityMapper.toDto(user);
    }

    public UserContainer findById(UUID userId) {
        return userRepository.findById(userId)
                .map(userEntityMapper::toDto)
                .orElseThrow();
    }

    public void deleteById(UUID userId) {
        likeRepository.deleteAllByUserId(userId);
        commentRepository.deleteAllByUserId(userId);
        postRepository.deleteAllByUserId(userId);
        userRepository.deleteById(userId);
    }

    @Transactional
    public UserContainer update(UserContainer updatedUser){
        var post = userRepository.findById(updatedUser.id())
                .orElseThrow();
        var source =   userEntityMapper.toEntity(updatedUser);
        userEntityMapper.merge(source, post);
        return userEntityMapper.toDto(post);
    }

    public Optional<UserContainer> safelyFindByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userEntityMapper::toDto);
    }
}
