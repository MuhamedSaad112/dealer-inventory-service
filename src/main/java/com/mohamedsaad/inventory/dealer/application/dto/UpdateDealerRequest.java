package com.mohamedsaad.inventory.dealer.application.dto;

import com.mohamedsaad.inventory.dealer.domain.enums.SubscriptionType;
import jakarta.validation.constraints.Email;

public record UpdateDealerRequest(
        String name,

        @Email(message = "Dealer email must be valid")
        String email,

        SubscriptionType subscriptionType
) {
}