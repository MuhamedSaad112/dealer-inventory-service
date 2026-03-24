package com.mohamedsaad.inventory.dealer.infrastructure.web;

import com.mohamedsaad.inventory.dealer.application.dto.CreateDealerRequest;
import com.mohamedsaad.inventory.dealer.application.dto.DealerResponse;
import com.mohamedsaad.inventory.dealer.application.dto.UpdateDealerRequest;
import com.mohamedsaad.inventory.dealer.application.service.DealerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dealers")
@RequiredArgsConstructor
@Tag(name = "Dealer", description = "Dealer management APIs")
public class DealerController {

    private final DealerService dealerService;

    @Operation(summary = "Create a new dealer")
    @PostMapping
    public DealerResponse createDealer(
            @Valid @RequestBody CreateDealerRequest request
    ) {
        return dealerService.createDealer(request);
    }

    @Operation(summary = "Get dealer by ID")
    @GetMapping("/{id}")
    public DealerResponse getDealerById(
            @Parameter(description = "Dealer ID") @PathVariable UUID id
    ) {
        return dealerService.getDealerById(id);
    }

    @Operation(summary = "Get all dealers with pagination")
    @GetMapping
    public List<DealerResponse> getAllDealers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return dealerService.getAllDealers(page, size, sortBy, sortDir);
    }

    @Operation(summary = "Update dealer")
    @PatchMapping("/{id}")
    public DealerResponse updateDealer(
            @Parameter(description = "Dealer ID") @PathVariable UUID id,
            @Valid @RequestBody UpdateDealerRequest request
    ) {
        return dealerService.updateDealer(id, request);
    }

    @Operation(summary = "Delete dealer")
    @DeleteMapping("/{id}")
    public void deleteDealer(
            @Parameter(description = "Dealer ID") @PathVariable UUID id
    ) {
        dealerService.deleteDealer(id);
    }
}