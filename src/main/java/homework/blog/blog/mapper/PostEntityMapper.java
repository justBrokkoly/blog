package homework.blog.blog.mapper;

import homework.blog.blog.model.container.PostContainer;
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
public interface PostEntityMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", source = "source.id")
    PostEntity toEntity(PostContainer source, UserEntity user);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    PostEntity toEntity(PostContainer source);

    @Mapping(target = "userId", source = "user.id")
    PostContainer toDto(PostEntity source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
     void merge(
            PostEntity source,
            @MappingTarget PostEntity target
    );
}
