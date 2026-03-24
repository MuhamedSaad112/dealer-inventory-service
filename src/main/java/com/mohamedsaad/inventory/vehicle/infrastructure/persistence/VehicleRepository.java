package com.mohamedsaad.inventory.vehicle.infrastructure.persistence;

import com.mohamedsaad.inventory.vehicle.domain.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID>, JpaSpecificationExecutor<Vehicle> {

    @Query("""
                SELECT v FROM Vehicle v
                WHERE v.id = :id
                AND v.tenantId = :tenantId
            """)
    Optional<Vehicle> findByIdAndTenantId(
            @Param("id") UUID id,
            @Param("tenantId") String tenantId
    );
}