package homework.blog.blog.controller;

import homework.blog.blog.model.container.PostAuditContainer;
import homework.blog.blog.service.PostAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping("/private/audit-info")
@RestController
@RequiredArgsConstructor
public class AuditController {
    private final PostAuditService postAuditService;

    @GetMapping("/{postId}")
    public Page<PostAuditContainer> findPostAuditHistory(@PathVariable UUID postId,
                                                         @RequestParam(value = "page") int page,
                                                         @RequestParam(value = "size") int size) {
        return postAuditService.findPostAuditHistory(postId, page, size);
    }
}
