package homework.blog.blog.service.db;

import homework.blog.blog.mapper.PostEntityMapper;
import homework.blog.blog.model.container.PostAuditContainer;
import homework.blog.blog.model.container.PostContainer;
import homework.blog.blog.model.dto.UserPostCountDto;
import homework.blog.blog.repository.CommentRepository;
import homework.blog.blog.repository.LikeRepository;
import homework.blog.blog.repository.PostAuditRepository;
import homework.blog.blog.repository.PostRepository;
import homework.blog.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DbPostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final PostEntityMapper postEntityMapper;
    private final CommentRepository commentRepository;
    private final PostAuditRepository postAuditRepository;

    public PostContainer save(PostContainer postContainer) {
        var userEntity = userRepository.getReferenceById(postContainer.userId());
        var post = postRepository.save(postEntityMapper.toEntity(postContainer, userEntity));
        return postEntityMapper.toDto(post);
    }

    public PostContainer findById(UUID postId) {
        return postRepository.findById(postId)
                .map(postEntityMapper::toDto)
                .orElseThrow();
    }

    @Transactional
    public void deleteById(UUID postId) {
        postAuditRepository.deleteAllByPostId(postId);
        likeRepository.deleteAllByPostId(postId);
        commentRepository.deleteAllByPostId(postId);
        postRepository.deleteById(postId);
    }

    @Transactional
    public PostContainer update(PostContainer updatedPost) {
        var post = postRepository.findById(updatedPost.id())
                .orElseThrow();
        var source = postEntityMapper.toEntity(updatedPost);
        postEntityMapper.merge(source, post);
        return postEntityMapper.toDto(post);
    }


    public List<PostContainer> searchByText(String value) {
        return postRepository.fullTextSearch(value)
                .stream().map(postEntityMapper::toDto)
                .collect(Collectors.toList());
    }


    public Page<PostContainer> findPostsByTags(Set<String> tags, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        return postRepository.findByTags(tags, pageable).map(postEntityMapper::toDto);
    }

    public List<UserPostCountDto> countPostsByUsername (){
        return postRepository.countPostsByUsername();
    }

}
