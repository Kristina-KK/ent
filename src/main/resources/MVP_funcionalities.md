# Congestion Tax Calculator – MVP Functional Scope

This document outlines the **Minimum Viable Product (MVP)** functionality for the Congestion Tax Calculator, implemented
using **Java 17**, **Spring Boot**, and designed following **Clean Architecture**, **SOLID principles**, and **Best
Practices**.

---

## MVP Functional Requirements

| #   | Functionality                   | Description                                                                                                | Rationale                                                                               |
|-----|---------------------------------|------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------|
| 1.  | **HTTP Endpoint**               | Expose a `POST /calculate-tax` endpoint to receive vehicle type and timestamps as input.                   | Enables integration via REST, which is industry standard and suitable for MVP delivery. |
| 2.  | **Vehicle Type Support**        | Respect tax-exempt vehicle types (Motorcycle, Emergency, Bus, Diplomat, Military, Foreign).                | Business rule compliance. Ensures no tax is calculated for exempt vehicles.             |
| 3.  | **Hourly Tax Rules**            | Apply time-based tax fees per city configuration (e.g. 06:00–06:29 = 8 SEK).                               | Core logic required for correct fee calculation.                                        |
| 4.  | **Single-Charge Rule**          | Within a 60-minute window, only charge the highest fee once.                                               | Specific to Gothenburg. Ensures fairness and regulatory compliance.                     |
| 5.  | **Daily Cap (60 SEK)**          | Do not charge more than 60 SEK per vehicle per day.                                                        | Implements rule for max daily tax and prevents overcharging.                            |
| 6.  | **Weekend & Holiday Exclusion** | Exclude weekends and predefined public holidays from taxation.                                             | Aligns with legal and business rules.                                                   |
| 7.  | **July Exclusion**              | No tax should be charged during the entire month of July.                                                  | Required by Gothenburg regulations.                                                     |
| 8.  | **YAML-Based Configuration**    | Load all tax rules and config from `application.yml`.                                                      | Keeps configuration externalized and environment-friendly for MVP.                      |
| 9.  | **Basic Unit Testing**          | Unit tests for core logic (e.g. vehicle exemption, time rules, single-charge, max cap).                    | Verifies correctness and stability of the business logic.                               |
| 10. | **Service Layer Abstraction**   | Use interfaces (`CongestionTaxCalculatorService`, `TaxFreeDateService`) for testability and extensibility. | Supports dependency injection, mocking, and Clean Code principles.                      |
| 11. | **Logging**                     | Add structured logs for tax requests and calculated results.                                               | Enables observability, useful for debugging and audits.                                 |

---

## Not in MVP (Out of Scope)

| Feature                       | Reason for Exclusion                                                      |
|-------------------------------|---------------------------------------------------------------------------|
| Multi-city support            | Can be added in the next iteration. MVP focuses on Gothenburg only.       |
| Dynamic configuration from DB | YAML config suffices for MVP. DB-backed config adds persistence overhead. |
| Admin UI for configuration    | Not needed for backend MVP. Would be part of future CMS integration.      |
| Authentication/Authorization  | MVP assumes internal or protected access. Security can be added later.    |
| Caching                       | Not required for MVP volume. Consider for production scale.               |
| CI/CD pipeline                | Valuable but not required for functional MVP submission.                  |

---

## Clean Code Principles Applied

- **SRP (Single Responsibility Principle)**: Each class and method has a well-defined role.
- **OCP (Open/Closed Principle)**: Easy to extend with new tax rules or cities.
- **Dependency Injection**: Services are injected to promote loose coupling and easier testing.
- **Code Readability**: Descriptive method names, no magic numbers, and meaningful domain models.
- **Testability**: Business logic is easily unit-testable via service layer abstraction.

---

## Conclusion

This MVP lays a robust and clean foundation for calculating congestion taxes in Gothenburg, while remaining open to
extension and integration with dynamic rule sources, multiple cities, and external configuration management systems.

