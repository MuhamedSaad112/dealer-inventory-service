package com.mohamedsaad.inventory.vehicle.infrastructure.web;

import com.mohamedsaad.inventory.dealer.domain.enums.SubscriptionType;
import com.mohamedsaad.inventory.vehicle.application.dto.CreateVehicleRequest;
import com.mohamedsaad.inventory.vehicle.application.dto.UpdateVehicleRequest;
import com.mohamedsaad.inventory.vehicle.application.dto.VehicleResponse;
import com.mohamedsaad.inventory.vehicle.application.service.VehicleService;
import com.mohamedsaad.inventory.vehicle.domain.enums.VehicleStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicle", description = "Vehicle management APIs")
public class VehicleController {

    private final VehicleService vehicleService;

    @Operation(summary = "Create a new vehicle")
    @PostMapping
    public VehicleResponse createVehicle(
            @Valid @RequestBody CreateVehicleRequest request
    ) {
        return vehicleService.createVehicle(request);
    }

    @Operation(summary = "Get vehicle by ID")
    @GetMapping("/{id}")
    public VehicleResponse getVehicleById(
            @Parameter(description = "Vehicle ID") @PathVariable UUID id
    ) {
        return vehicleService.getVehicleById(id);
    }

    @Operation(summary = "Get all vehicles with filters, pagination, and sorting")
    @GetMapping
    public List<VehicleResponse> getAllVehicles(

            @Parameter(description = "Filter by model")
            @RequestParam(required = false) String model,

            @Parameter(description = "Filter by status")
            @RequestParam(required = false) VehicleStatus status,

            @Parameter(description = "Minimum price")
            @RequestParam(required = false) BigDecimal priceMin,

            @Parameter(description = "Maximum price")
            @RequestParam(required = false) BigDecimal priceMax,

            @Parameter(description = "Subscription type")
            @RequestParam(required = false) SubscriptionType subscription,

            @Parameter(description = "Page number (default 0)")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size (default 10)")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Sort field (default model)")
            @RequestParam(defaultValue = "model") String sortBy,

            @Parameter(description = "Sort direction: asc or desc (default asc)")
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return vehicleService.getAllVehicles(
                model,
                status,
                priceMin,
                priceMax,
                subscription,
                page,
                size,
                sortBy,
                sortDir
        );
    }

    @Operation(summary = "Update vehicle")
    @PatchMapping("/{id}")
    public VehicleResponse updateVehicle(
            @Parameter(description = "Vehicle ID") @PathVariable UUID id,
            @Valid @RequestBody UpdateVehicleRequest request
    ) {
        return vehicleService.updateVehicle(id, request);
    }

    @Operation(summary = "Delete vehicle")
    @DeleteMapping("/{id}")
    public void deleteVehicle(
            @Parameter(description = "Vehicle ID") @PathVariable UUID id
    ) {
        vehicleService.deleteVehicle(id);
    }
}