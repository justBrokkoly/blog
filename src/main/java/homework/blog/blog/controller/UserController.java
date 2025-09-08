package homework.blog.blog.controller;

import homework.blog.blog.model.container.ActiveUserStatContainer;
import homework.blog.blog.model.container.UserContainer;
import homework.blog.blog.model.dto.UpdateUserRequestDto;
import homework.blog.blog.model.dto.UserRegRequestDto;
import homework.blog.blog.service.UserService;
import homework.blog.blog.service.UserStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequestMapping("/public/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserStatService userStatService;

    @PostMapping
    public UserContainer registerUser(@RequestBody UserRegRequestDto userRegRequestDto) {
        return userService.registerUser(userRegRequestDto);
    }

    @GetMapping("/{userId}")
    public UserContainer findUserById(@PathVariable UUID userId) {
        return userService.findUserById(userId);
    }

    @PostMapping("/{userId}")
    public UserContainer updateUser(@RequestBody UpdateUserRequestDto userRequestDto,
                                    @PathVariable UUID userId) {
        return userService.updateUser(userRequestDto, userId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
    }

    @GetMapping("/active/top")
    public List<ActiveUserStatContainer> getTopActiveUsers() {
        return userStatService.getTopActiveUsers();
    }
}
