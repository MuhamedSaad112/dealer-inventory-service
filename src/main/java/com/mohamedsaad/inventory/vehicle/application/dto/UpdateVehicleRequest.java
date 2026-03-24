package com.mohamedsaad.inventory.vehicle.application.dto;

import com.mohamedsaad.inventory.vehicle.domain.enums.VehicleStatus;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateVehicleRequest(
        UUID dealerId,
        String model,

        @DecimalMin(value = "0.0", inclusive = false, message = "Vehicle price must be greater than zero")
        BigDecimal price,

        VehicleStatus status
) {
}