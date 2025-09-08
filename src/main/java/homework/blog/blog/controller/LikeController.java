package homework.blog.blog.controller;

import homework.blog.blog.model.dto.LikeRequestDto;
import homework.blog.blog.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/public/like")
@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLike(@RequestBody LikeRequestDto likeRequestDto){
        likeService.updateLike(likeRequestDto);
    }
}
