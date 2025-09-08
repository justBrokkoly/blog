package homework.blog.blog.mapper;

import homework.blog.blog.model.container.PostAuditContainer;
import homework.blog.blog.model.container.PostContainer;
import homework.blog.blog.model.entity.PostAuditEntity;
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
public interface PostAuditEntityMapper {


    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    PostAuditEntity toEntity(PostAuditContainer source);

    PostAuditContainer toDto(PostAuditEntity source);

}
