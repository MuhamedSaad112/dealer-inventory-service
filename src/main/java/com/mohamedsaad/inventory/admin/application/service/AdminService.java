package com.mohamedsaad.inventory.admin.application.service;

import com.mohamedsaad.inventory.admin.application.dto.SubscriptionCountResponse;
import com.mohamedsaad.inventory.dealer.domain.enums.SubscriptionType;
import com.mohamedsaad.inventory.dealer.infrastructure.persistence.DealerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final DealerRepository dealerRepository;

    public SubscriptionCountResponse countDealersBySubscription() {
        long basicCount = dealerRepository.countBySubscriptionType(SubscriptionType.BASIC);
        long premiumCount = dealerRepository.countBySubscriptionType(SubscriptionType.PREMIUM);

        return new SubscriptionCountResponse(basicCount, premiumCount);
    }
}