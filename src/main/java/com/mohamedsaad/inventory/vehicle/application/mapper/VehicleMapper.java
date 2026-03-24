package com.mohamedsaad.inventory.vehicle.application.mapper;

import com.mohamedsaad.inventory.vehicle.application.dto.CreateVehicleRequest;
import com.mohamedsaad.inventory.vehicle.application.dto.UpdateVehicleRequest;
import com.mohamedsaad.inventory.vehicle.application.dto.VehicleResponse;
import com.mohamedsaad.inventory.vehicle.domain.entity.Vehicle;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    VehicleResponse toResponse(Vehicle vehicle);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "dealer", ignore = true)
    Vehicle toEntity(CreateVehicleRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "dealer", ignore = true)
    void updateVehicleFromRequest(UpdateVehicleRequest request, @MappingTarget Vehicle vehicle);
}