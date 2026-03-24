package com.mohamedsaad.inventory.dealer.domain.entity;

import com.mohamedsaad.inventory.dealer.domain.enums.SubscriptionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(
        name = "dealers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"tenant_id", "email"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dealer {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_type", nullable = false)
    private SubscriptionType subscriptionType;
}