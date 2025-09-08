package homework.blog.blog.service;

import homework.blog.blog.model.container.UserContainer;
import homework.blog.blog.model.dto.UpdateUserRequestDto;
import homework.blog.blog.model.dto.UserRegRequestDto;
import homework.blog.blog.service.db.DbUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final DbUserService dbUserService;

    public UserContainer registerUser(UserRegRequestDto userRegRequestDto) {
        return dbUserService.save(UserContainer.builder()
                .email(userRegRequestDto.getEmail())
                .username(userRegRequestDto.getUsername())
                .build());
    }

    public UserContainer findUserById(UUID userId) {
        return dbUserService.findById(userId);
    }

    public UserContainer updateUser(UpdateUserRequestDto userRequestDto, UUID userId) {
        return dbUserService.update(UserContainer.builder()
                .id(userId)
                .username(userRequestDto.getUsername())
                .email(userRequestDto.getEmail())
                .build());
    }

    public void deleteUser(UUID userId) {
        dbUserService.deleteById(userId);
    }

}
