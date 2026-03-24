package com.mohamedsaad.inventory.vehicle.application.dto;

import com.mohamedsaad.inventory.vehicle.domain.enums.VehicleStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record VehicleResponse(
        UUID id,
        UUID dealerId,
        String model,
        BigDecimal price,
        VehicleStatus status
) {
}