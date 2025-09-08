package homework.blog.blog.mapper;

import homework.blog.blog.model.container.ActiveUserStatContainer;
import homework.blog.blog.model.entity.ActiveUserStatEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ActiveUserStatEntityMapper {

    ActiveUserStatContainer toDto(ActiveUserStatEntity source);
}
