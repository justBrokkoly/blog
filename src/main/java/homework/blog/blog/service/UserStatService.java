package homework.blog.blog.service;

import homework.blog.blog.model.container.ActiveUserStatContainer;
import homework.blog.blog.service.db.DbUserStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserStatService {

    private final DbUserStatService dbUserStatService;

    public List<ActiveUserStatContainer> getTopActiveUsers() {
        return dbUserStatService.getTopActiveUsers();
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void refreshStats() {
        dbUserStatService.refreshStats();
    }
}
