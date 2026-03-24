package com.mohamedsaad.inventory.dealer.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mohamedsaad.inventory.dealer.domain.enums.SubscriptionType;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DealerResponse(
        UUID id,
        String name,
        String email,
        SubscriptionType subscriptionType
) {
}