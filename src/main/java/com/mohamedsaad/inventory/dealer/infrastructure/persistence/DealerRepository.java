package com.mohamedsaad.inventory.dealer.infrastructure.persistence;

import com.mohamedsaad.inventory.dealer.domain.entity.Dealer;
import com.mohamedsaad.inventory.dealer.domain.enums.SubscriptionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface DealerRepository extends JpaRepository<Dealer, UUID> {

    @Query("""
            SELECT d
            FROM Dealer d
            WHERE d.id = :id
            AND d.tenantId = :tenantId
            """)
    Optional<Dealer> findByIdAndTenantId(@Param("id") UUID id,
                                         @Param("tenantId") String tenantId);


    @Query("""
            SELECT d
            FROM Dealer d
            WHERE d.tenantId = :tenantId
            """)
    Page<Dealer> findAllByTenantId(@Param("tenantId") String tenantId,
                                   Pageable pageable);


    @Query("""
            SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END
            FROM Dealer d
            WHERE d.email = :email
            AND d.tenantId = :tenantId
            """)
    boolean existsByEmailAndTenantId(@Param("email") String email,
                                     @Param("tenantId") String tenantId);


    @Query("""
            SELECT COUNT(d)
            FROM Dealer d
            WHERE d.subscriptionType = :subscriptionType
            """)
    long countBySubscriptionType(@Param("subscriptionType") SubscriptionType subscriptionType);
}