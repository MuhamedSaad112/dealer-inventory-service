package com.mohamedsaad.inventory.dealer.application.mapper;

import com.mohamedsaad.inventory.dealer.application.dto.CreateDealerRequest;
import com.mohamedsaad.inventory.dealer.application.dto.DealerResponse;
import com.mohamedsaad.inventory.dealer.application.dto.UpdateDealerRequest;
import com.mohamedsaad.inventory.dealer.domain.entity.Dealer;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface DealerMapper {

    DealerResponse toResponse(Dealer dealer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    Dealer toEntity(CreateDealerRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    void updateDealerFromRequest(UpdateDealerRequest request, @MappingTarget Dealer dealer);
}