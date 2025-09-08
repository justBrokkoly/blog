package homework.blog.blog.mapper;

import homework.blog.blog.model.container.CommentContainer;
import homework.blog.blog.model.container.PostContainer;
import homework.blog.blog.model.entity.CommentEntity;
import homework.blog.blog.model.entity.PostEntity;
import homework.blog.blog.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface CommentEntityMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", source = "source.id")
    @Mapping(target = "content", source = "source.content")
    @Mapping(target = "likesCount", source = "source.likesCount")
    @Mapping(target = "user", source = "user")
    CommentEntity toEntity(CommentContainer source, CommentEntity reply, UserEntity user, PostEntity post);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "parentId", source = "reply.id")
    CommentContainer toDto(CommentEntity source);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "reply", ignore = true)
    @Mapping(target = "post", ignore = true)
    CommentEntity toEntity(CommentContainer source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "reply", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "likesCount", ignore = true)
    void merge(
            CommentEntity source,
            @MappingTarget CommentEntity target
    );
}
