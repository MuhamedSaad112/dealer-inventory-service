# Dealer & Vehicle Inventory Module

A multi-tenant Inventory module built using **Spring Boot** and **Java 17** inside a **Modular Monolith** structure.

This project manages **dealers** and their **vehicles** while enforcing strict **tenant isolation** and **role-based access control**.  
It follows clean architecture principles by separating responsibilities across controller, service, domain/entity, repository, validation, and security layers.

---

# Project Overview

The goal of this module is to provide a backend inventory system for managing:

- Dealers
- Vehicles

The system is designed as a **multi-tenant application**, meaning each request must belong to a specific tenant, and data from one tenant cannot be accessed by another tenant.

The application also includes an **admin endpoint** that is accessible only by users with the `GLOBAL_ADMIN` role.

---

# Main Features

## Dealer Management
Supports full CRUD operations for dealers:

- Create dealer
- Get dealer by ID
- Get all dealers with pagination and sorting
- Update dealer
- Delete dealer

## Vehicle Management
Supports full CRUD operations for vehicles:

- Create vehicle
- Get vehicle by ID
- Get all vehicles with filtering, pagination, and sorting
- Update vehicle
- Delete vehicle

## Vehicle Filtering
Vehicles can be filtered by:

- `model`
- `status`
- `priceMin`
- `priceMax`
- `subscription`

## Admin Reporting
Provides an admin-only endpoint to count dealers by subscription type.

## Multi-Tenant Enforcement
Every request must include:

`X-Tenant-Id`

The system uses this header to determine the current tenant and prevent cross-tenant access.

---

# Tech Stack

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Security
- PostgreSQL
- Hibernate
- Bean Validation
- MapStruct
- Lombok
- Springdoc OpenAPI / Swagger UI
- Maven

---

# Architecture

The project is structured using clean separation of concerns.

## Controller Layer
Responsible for exposing REST APIs and handling HTTP requests and responses.

Examples:
- `DealerController`
- `VehicleController`
- `AdminController`

## Service Layer
Contains the business logic of the application.

Examples:
- `DealerService`
- `VehicleService`
- `AdminService`

## Domain / Entity Layer
Represents the core business entities and enums.

Examples:
- `Dealer`
- `Vehicle`
- `SubscriptionType`
- `VehicleStatus`

## Repository Layer
Responsible for database access using Spring Data JPA.

Examples:
- `DealerRepository`
- `VehicleRepository`

## Validation Layer
Handles request validation using:
- Bean Validation annotations in DTOs
- Additional custom validation inside services

## Security Layer
Responsible for:
- Basic Authentication
- Role-based access control
- Tenant header enforcement

## Tenant Enforcement
A custom filter reads the `X-Tenant-Id` request header and stores it in `TenantContext` using `ThreadLocal`.

---

# Multi-Tenancy Design

## Header-Based Tenant Resolution
Each request must include:

`X-Tenant-Id: tenant1`

This value is extracted by `TenantFilter` and stored in `TenantContext`.

## TenantContext
`TenantContext` uses `ThreadLocal<String>` to store the current tenant ID for the duration of the request.

## Tenant Isolation
Tenant isolation is enforced in:

- Dealer queries
- Vehicle queries
- Service-level validations
- Cross-tenant access checks

## Missing Tenant Header
If `X-Tenant-Id` is missing or blank, the request is rejected with:

- HTTP `400 Bad Request`

---

# Security

The application uses **HTTP Basic Authentication**.

## In-Memory Users

### Regular User
- Username: `user`
- Password: `user`
- Role: `USER`

### Admin User
- Username: `admin`
- Password: `admin`
- Role: `GLOBAL_ADMIN`

## Authorization Rules

- Swagger and OpenAPI endpoints are public
- `/admin/**` endpoints require `GLOBAL_ADMIN`
- All other endpoints require authentication

---

# Data Model

## Dealer

Fields:

- `id` → UUID
- `tenantId` → String
- `name` → String
- `email` → String
- `subscriptionType` → Enum (`BASIC`, `PREMIUM`)

### Notes
- Dealer email must be unique **within the same tenant**
- This is enforced both in service validation and via database unique constraint:
    - `(tenant_id, email)`

## Vehicle

Fields:

- `id` → UUID
- `tenantId` → String
- `dealerId` → UUID
- `model` → String
- `price` → BigDecimal
- `status` → Enum (`AVAILABLE`, `SOLD`)

### Notes
- A vehicle belongs to a dealer
- Vehicle creation and update validate that the referenced dealer exists in the same tenant

---

# API Endpoints

# Dealers

## 1) Create Dealer
**POST** `/dealers`

Creates a new dealer in the current tenant.

### Request Body
```json
{
  "name": "Dealer One",
  "email": "dealer1@example.com",
  "subscriptionType": "PREMIUM"
}