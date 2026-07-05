package dev.vorstu.mappers;

import dev.vorstu.models.dto.admin.AdminRequest;
import dev.vorstu.models.dto.admin.AdminResponse;
import dev.vorstu.models.entities.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    AdminResponse toAdminResponse(Admin Admin);
    List<AdminResponse> toAdminResponseList(List<Admin> Admins);

    AdminRequest toAdminRequest(Admin Admin);
    Admin toAdmin(AdminRequest AdminRequest);

    @Mapping(target = "id", ignore = true)
    void updateEntity(AdminRequest request, @MappingTarget Admin Admin);

}
