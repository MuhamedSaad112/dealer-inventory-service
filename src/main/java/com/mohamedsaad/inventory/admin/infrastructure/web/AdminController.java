package com.mohamedsaad.inventory.admin.infrastructure.web;

import com.mohamedsaad.inventory.admin.application.dto.SubscriptionCountResponse;
import com.mohamedsaad.inventory.admin.application.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin APIs", description = "Endpoints for admin operations")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dealers/countBySubscription")
    @Operation(
            summary = "Count dealers by subscription type",
            description = "Returns the number of dealers grouped by their subscription plans"
    )
    public SubscriptionCountResponse countDealersBySubscription() {
        return adminService.countDealersBySubscription();
    }
}