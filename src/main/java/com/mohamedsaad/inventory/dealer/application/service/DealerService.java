package com.mohamedsaad.inventory.dealer.application.service;

import com.mohamedsaad.inventory.common.exception.BadRequestException;
import com.mohamedsaad.inventory.common.exception.ForbiddenException;
import com.mohamedsaad.inventory.common.exception.ResourceNotFoundException;
import com.mohamedsaad.inventory.common.tenant.TenantContext;
import com.mohamedsaad.inventory.dealer.application.dto.CreateDealerRequest;
import com.mohamedsaad.inventory.dealer.application.dto.DealerResponse;
import com.mohamedsaad.inventory.dealer.application.dto.UpdateDealerRequest;
import com.mohamedsaad.inventory.dealer.application.mapper.DealerMapper;
import com.mohamedsaad.inventory.dealer.domain.entity.Dealer;
import com.mohamedsaad.inventory.dealer.infrastructure.persistence.DealerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DealerService {

    private static final String DEALER_NOT_FOUND = "Dealer not found";
    private static final String DUPLICATE_EMAIL_MESSAGE = "Dealer email already exists in the current tenant";

    private final DealerRepository dealerRepository;
    private final DealerMapper dealerMapper;

    public DealerResponse createDealer(CreateDealerRequest request) {
        String tenantId = getCurrentTenantId();

        validateDealerEmailUniqueness(request.email(), tenantId);

        Dealer dealer = dealerMapper.toEntity(request);
        dealer.setTenantId(tenantId);

        Dealer savedDealer = dealerRepository.save(dealer);
        return dealerMapper.toResponse(savedDealer);
    }

    public DealerResponse getDealerById(UUID id) {
        Dealer dealer = findDealerByIdAndTenant(id);
        return dealerMapper.toResponse(dealer);
    }

    public List<DealerResponse> getAllDealers(int page, int size, String sortBy, String sortDir) {
        String tenantId = getCurrentTenantId();

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return dealerRepository.findAllByTenantId(tenantId, pageable)
                .stream()
                .map(dealerMapper::toResponse)
                .toList();
    }

    public DealerResponse updateDealer(UUID id, UpdateDealerRequest request) {
        String tenantId = getCurrentTenantId();
        Dealer dealer = findDealerByIdAndTenant(id);

        validateUpdateRequest(request);
        validateEmailChangeUniqueness(request, dealer, tenantId);

        dealerMapper.updateDealerFromRequest(request, dealer);

        Dealer updatedDealer = dealerRepository.save(dealer);
        return dealerMapper.toResponse(updatedDealer);
    }

    public void deleteDealer(UUID id) {
        Dealer dealer = findDealerByIdAndTenant(id);
        dealerRepository.delete(dealer);
    }

    private Dealer findDealerByIdAndTenant(UUID id) {
        Dealer dealer = dealerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(DEALER_NOT_FOUND));

        if (!dealer.getTenantId().equals(getCurrentTenantId())) {
            throw new ForbiddenException("Cross-tenant access is forbidden");
        }

        return dealer;
    }

    private void validateDealerEmailUniqueness(String email, String tenantId) {
        if (dealerRepository.existsByEmailAndTenantId(email, tenantId)) {
            throw new BadRequestException(DUPLICATE_EMAIL_MESSAGE);
        }
    }

    private void validateEmailChangeUniqueness(UpdateDealerRequest request, Dealer dealer, String tenantId) {
        if (request.email() != null
                && !request.email().equals(dealer.getEmail())) {
            validateDealerEmailUniqueness(request.email(), tenantId);
        }
    }

    private void validateUpdateRequest(UpdateDealerRequest request) {
        if (request.name() != null && request.name().isBlank()) {
            throw new BadRequestException("Dealer name must not be blank");
        }

        if (request.email() != null && request.email().isBlank()) {
            throw new BadRequestException("Dealer email must not be blank");
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