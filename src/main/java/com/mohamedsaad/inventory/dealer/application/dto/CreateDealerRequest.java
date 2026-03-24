package com.mohamedsaad.inventory.dealer.application.dto;

import com.mohamedsaad.inventory.dealer.domain.enums.SubscriptionType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateDealerRequest(
        @NotBlank(message = "Dealer name is required")
        String name,

        @NotBlank(message = "Dealer email is required")
        @Email(message = "Dealer email must be valid")
        String email,

        @NotNull(message = "Subscription type is required")
        SubscriptionType subscriptionType
) {
}
