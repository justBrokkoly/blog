package homework.blog.blog.controller;

import homework.blog.blog.model.container.PostContainer;
import homework.blog.blog.model.dto.PostCreateRequestDto;
import homework.blog.blog.model.dto.PostSearchResponseDto;
import homework.blog.blog.model.dto.PostUpdateRequestDto;
import homework.blog.blog.model.dto.UserPostCountDto;
import homework.blog.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequestMapping("/public/post")
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public PostContainer createPost(@RequestBody PostCreateRequestDto request) {
        return postService.createPost(request);
    }

    @PostMapping("/{postId}")
    public PostContainer updatePost(@RequestBody PostUpdateRequestDto request,
                                    @PathVariable UUID postId) {
        return postService.updatePost(request, postId);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable UUID postId) {
        postService.deletePost(postId);
    }

    @GetMapping("/tags")
    public Page<PostContainer> findPostsByTags(@RequestBody Set<String> tags,
                                               @RequestParam(value = "size") int size,
                                               @RequestParam(value = "page") int page) {
        return postService.findPostsByTags(tags, page, size);
    }

    @PostMapping("/{postId}/tags")
    public PostContainer updatePostTags(@RequestBody List<String> tags,
                                        @PathVariable UUID postId) {
        return postService.updatePostTags(tags, postId);
    }

    @GetMapping("/search")
    public PostSearchResponseDto searchPosts(@RequestBody String value) {
        return postService.searchPosts(value);
    }

    @GetMapping("/count/username")
    public List<UserPostCountDto> countPostsByUsername() {
        return postService.countPostsByUsername();
    }

    @GetMapping("/{postId}")
    public PostContainer findById(@PathVariable UUID postId) {
        return postService.findById(postId);
    }

}
