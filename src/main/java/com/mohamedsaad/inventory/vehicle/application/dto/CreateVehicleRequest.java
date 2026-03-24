package com.mohamedsaad.inventory.vehicle.application.dto;

import com.mohamedsaad.inventory.vehicle.domain.enums.VehicleStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateVehicleRequest(
        @NotNull(message = "Dealer id is required")
        UUID dealerId,

        @NotBlank(message = "Vehicle model is required")
        String model,

        @NotNull(message = "Vehicle price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Vehicle price must be greater than zero")
        BigDecimal price,

        @NotNull(message = "Vehicle status is required")
        VehicleStatus status
) {
}