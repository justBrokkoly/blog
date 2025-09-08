package homework.blog.blog.service;

import homework.blog.blog.model.container.PostContainer;
import homework.blog.blog.model.dto.PostCreateRequestDto;
import homework.blog.blog.model.dto.PostDto;
import homework.blog.blog.model.dto.PostSearchResponseDto;
import homework.blog.blog.model.dto.PostUpdateRequestDto;
import homework.blog.blog.model.dto.UserPostCountDto;
import homework.blog.blog.service.db.DbPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final DbPostService dbPostService;

    public PostContainer createPost(PostCreateRequestDto request) {
        return dbPostService.save(PostContainer.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .tags(request.getTags())
                .build());
    }

    public PostContainer updatePost(PostUpdateRequestDto request, UUID postId) {
        return dbPostService.update(PostContainer.builder()
                .tags(request.getTags())
                .id(postId)
                .content(request.getContent())
                .title(request.getTitle())
                .build());
    }

    public void deletePost(UUID postId) {
        dbPostService.deleteById(postId);
    }

    public Page<PostContainer> findPostsByTags(Set<String> tags, int size, int page) {
        return dbPostService.findPostsByTags(tags, page, size);
    }

    public PostContainer findById (UUID postId){
        return dbPostService.findById(postId);
    }

    public PostContainer updatePostTags(List<String> tags, UUID postId) {
        var currentPost = dbPostService.findById(postId);
        return dbPostService.update(currentPost.withTags(tags));
    }

    public PostSearchResponseDto searchPosts(String value) {
        var posts = dbPostService.searchByText(value).stream()
                .map(d -> PostDto.builder()
                        .title(d.title())
                        .content(d.content())
                        .postId(d.id())
                        .tags(d.tags())
                        .build())
                .toList();
        return new PostSearchResponseDto(posts);
    }

    public List<UserPostCountDto> countPostsByUsername() {
        return dbPostService.countPostsByUsername();
    }
}
