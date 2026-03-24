package com.mohamedsaad.inventory.vehicle.application.specification;

import com.mohamedsaad.inventory.dealer.domain.entity.Dealer;
import com.mohamedsaad.inventory.dealer.domain.enums.SubscriptionType;
import com.mohamedsaad.inventory.vehicle.domain.entity.Vehicle;
import com.mohamedsaad.inventory.vehicle.domain.enums.VehicleStatus;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class VehicleSpecification {

    private VehicleSpecification() {
    }

    public static Specification<Vehicle> withFilters(
            String tenantId,
            String model,
            VehicleStatus status,
            BigDecimal priceMin,
            BigDecimal priceMax,
            SubscriptionType subscriptionType
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("tenantId"), tenantId));

            if (model != null && !model.isBlank()) {
                predicates.add(
                        cb.like(cb.lower(root.get("model")), "%" + model.toLowerCase() + "%")
                );
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (priceMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), priceMin));
            }

            if (priceMax != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), priceMax));
            }

            if (subscriptionType != null) {
                Join<Vehicle, Dealer> dealerJoin = root.join("dealer");
                predicates.add(cb.equal(dealerJoin.get("subscriptionType"), subscriptionType));
                predicates.add(cb.equal(dealerJoin.get("tenantId"), tenantId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}