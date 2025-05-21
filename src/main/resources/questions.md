# questions.md

## Functional Questions

1. **How are cities uniquely identified in requests?**
   Should we use a `cityId` parameter or infer the city context from the authenticated user or subdomain?

2. **How is configuration versioned?**
   If tax rules change year over year, should we version rules by date (e.g., effectiveFrom / effectiveTo)?

3. **How are tax rules managed by content editors?**
   Will editors use an admin UI, JSON file, or an external CMS to manage rules?

4. **Should we validate overlapping time intervals in rules?**
   Is there a mechanism to prevent invalid or overlapping time windows?

5. **What is the source of holidays and tax-free dates?**
   Should we rely on a static list, public API (e.g., Swedish public holidays), or external calendar feed?

6. **Should we support holiday overrides per city?**
   Can some cities define additional or fewer local holidays?

7. **How are tax-exempt vehicles validated?**
   Are they identified by type alone or should license plate patterns or registry IDs be validated?

8. **What is the behavior for timestamps in the future or duplicated entries?**
   Should we reject them, log warnings, or sanitize input silently?

---

## Architectural & Technical Questions

9. **How is configuration stored at runtime?**
   Should we load from a file system, database, or external config service (e.g., Spring Cloud Config)?

10. **Should configuration be cacheable or always fetched live?**
    How often do rules change, and is caching in memory acceptable for performance?

11. **How should we support new cities with unique logic or exceptions?**
    Should we use Strategy pattern or inject city-specific rule engines?

12. **Do we foresee the need for dynamic rule evaluation (e.g., weather-based exemptions)?**
    Would a DSL (domain-specific language) or rules engine (like Drools) be beneficial?

13. **How should services be structured for maintainability and testability?**
    Are we enforcing strict separation between domain, application, and infrastructure layers?

14. **Should tax calculation be fully stateless?**
    Is this a requirement for running in distributed environments (e.g., cloud, Kubernetes)?

---

## Testing and Observability Questions

15. **What is the minimum test coverage expected (unit, integration, edge cases)?**
    Are we targeting 80%+, and how do we test rule correctness over time?

16. **How do we test edge cases (invalid dates, max daily limit, overlapping intervals)?**
    Should we include test cases for concurrency or large volume input?

17. **Should we implement metrics/logging for tax calculations?**
    What logs are needed for auditing incorrect tax or misconfiguration?

---

## Future Extension Questions

18. **Will we need multilingual support for error messages or UI labels?**
    If this will be exposed to external users, do we support i18n/l10n?

19. **Should this logic be reusable as a library by other services or exposed via API gateway?**
    Are there other systems (e.g., mobile apps, legacy services) that might need tax calculation logic?

20. **Should changes to tax rules be audited or version-controlled?**
    Is there a compliance requirement for change history or approvals before publishing?
