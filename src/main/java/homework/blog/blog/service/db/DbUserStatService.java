package homework.blog.blog.service.db;

import homework.blog.blog.mapper.ActiveUserStatEntityMapper;
import homework.blog.blog.model.container.ActiveUserStatContainer;
import homework.blog.blog.repository.ActiveUserStatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DbUserStatService {

    private final ActiveUserStatRepository repository;
    private final ActiveUserStatEntityMapper activeUserStatEntityMapper;

    public List<ActiveUserStatContainer> getTopActiveUsers() {
        return repository.findTop10ByOrderByTotalActivityDesc()
                .stream().map(activeUserStatEntityMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void refreshStats() {
        repository.refreshActiveUsersStats();
    }
}
