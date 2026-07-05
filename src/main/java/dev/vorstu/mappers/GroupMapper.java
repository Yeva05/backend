package dev.vorstu.mappers;

import dev.vorstu.models.dto.group.GroupRequest;
import dev.vorstu.models.dto.group.GroupResponse;
import dev.vorstu.models.entities.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    GroupResponse toGroupResponse(Group group);
    List<GroupResponse> toGroupResponseList(List<Group> groups);

    GroupRequest toGroupRequest(Group group);
    Group toGroup(GroupRequest groupRequest);

    @Mapping(target = "id", ignore = true)
    void updateEntity(GroupRequest request, @MappingTarget Group group);

}
