package homework.blog.blog.mapper;

import homework.blog.blog.model.container.LikeContainer;
import homework.blog.blog.model.entity.LikeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface LikeEntityMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", source = "source")
    LikeEntity toEntity(LikeContainer source);

    @Mapping(target = "userId", source = "id.userId")
    @Mapping(target = "targetType", source = "id.targetType")
    @Mapping(target = "targetId", source = "id.targetId")
    LikeContainer toDto(LikeEntity source);
}
