package homework.blog.blog.model.entity;

import homework.blog.blog.model.enums.TargetTypeLikeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class LikeId {

    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "target_id")
    private UUID targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "targetType")
    private TargetTypeLikeEnum targetType;
}
