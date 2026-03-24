package com.mohamedsaad.inventory.vehicle.application.service;

import com.mohamedsaad.inventory.common.exception.BadRequestException;
import com.mohamedsaad.inventory.common.exception.ForbiddenException;
import com.mohamedsaad.inventory.common.exception.ResourceNotFoundException;
import com.mohamedsaad.inventory.common.tenant.TenantContext;
import com.mohamedsaad.inventory.dealer.domain.enums.SubscriptionType;
import com.mohamedsaad.inventory.dealer.infrastructure.persistence.DealerRepository;
import com.mohamedsaad.inventory.vehicle.application.dto.CreateVehicleRequest;
import com.mohamedsaad.inventory.vehicle.application.dto.UpdateVehicleRequest;
import com.mohamedsaad.inventory.vehicle.application.dto.VehicleResponse;
import com.mohamedsaad.inventory.vehicle.application.mapper.VehicleMapper;
import com.mohamedsaad.inventory.vehicle.application.specification.VehicleSpecification;
import com.mohamedsaad.inventory.vehicle.domain.entity.Vehicle;
import com.mohamedsaad.inventory.vehicle.domain.enums.VehicleStatus;
import com.mohamedsaad.inventory.vehicle.infrastructure.persistence.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private static final String VEHICLE_NOT_FOUND = "Vehicle not found";
    private static final String DEALER_NOT_FOUND = "Dealer not found";
    private static final String VEHICLE_MODEL_MUST_NOT_BE_BLANK = "Vehicle model must not be blank";

    private final VehicleRepository vehicleRepository;
    private final DealerRepository dealerRepository;
    private final VehicleMapper vehicleMapper;

    public VehicleResponse createVehicle(CreateVehicleRequest request) {
        String tenantId = getCurrentTenantId();
        ensureDealerExistsInCurrentTenant(request.dealerId(), tenantId);
        validateCreateRequest(request);

        Vehicle vehicle = vehicleMapper.toEntity(request);
        vehicle.setTenantId(tenantId);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toResponse(savedVehicle);
    }

    public VehicleResponse getVehicleById(UUID id) {
        Vehicle vehicle = findVehicleByIdAndTenant(id);
        return vehicleMapper.toResponse(vehicle);
    }

    public List<VehicleResponse> getAllVehicles(String model, VehicleStatus status, BigDecimal priceMin, BigDecimal priceMax, SubscriptionType subscriptionType, int page, int size, String sortBy, String sortDir) {
        String tenantId = getCurrentTenantId();

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return vehicleRepository.findAll(VehicleSpecification.withFilters(tenantId, model, status, priceMin, priceMax, subscriptionType), pageable).stream().map(vehicleMapper::toResponse).toList();
    }

    public VehicleResponse updateVehicle(UUID id, UpdateVehicleRequest request) {
        String tenantId = getCurrentTenantId();
        Vehicle vehicle = findVehicleByIdAndTenant(id);

        validateUpdateRequest(request);

        if (request.dealerId() != null && !request.dealerId().equals(vehicle.getDealerId())) {
            ensureDealerExistsInCurrentTenant(request.dealerId(), tenantId);
        }

        vehicleMapper.updateVehicleFromRequest(request, vehicle);

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toResponse(updatedVehicle);
    }

    public void deleteVehicle(UUID id) {
        Vehicle vehicle = findVehicleByIdAndTenant(id);
        vehicleRepository.delete(vehicle);
    }

    private Vehicle findVehicleByIdAndTenant(UUID id) {
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(VEHICLE_NOT_FOUND));

        if (!vehicle.getTenantId().equals(getCurrentTenantId())) {
            throw new ForbiddenException("Cross-tenant access is forbidden");
        }

        return vehicle;
    }

    private void ensureDealerExistsInCurrentTenant(UUID dealerId, String tenantId) {
        dealerRepository.findByIdAndTenantId(dealerId, tenantId).orElseThrow(() -> new ResourceNotFoundException(DEALER_NOT_FOUND));
    }

    private void validateCreateRequest(CreateVehicleRequest request) {
        if (request.model() != null && request.model().isBlank()) {
            throw new BadRequestException(VEHICLE_MODEL_MUST_NOT_BE_BLANK);
        }
    }

    private void validateUpdateRequest(UpdateVehicleRequest request) {
        if (request.model() != null && request.model().isBlank()) {
            throw new BadRequestException(VEHICLE_MODEL_MUST_NOT_BE_BLANK);
        }
    }

    private String getCurrentTenantId() {
        String tenantId = TenantContext.getTenantId();
        if (tenantId == null || tenantId.isBlank()) {
            throw new BadRequestException("Missing tenant context");
        }
        return tenantId;
    }

}