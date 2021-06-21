Rewrite the application for storing books on ORM
Goal: to fully work with JPA + Hibernate to connect to relational databases via the ORM framework
Result: A high-level application with JPA entity mapping
The homework is done by rewriting the previous one in JPA.

Requirements:
1. Use JPA, Hibernate only as a JPA provider.
2. To solve the N+1 problem, you can use the Hibernate-specific @Fetch and @batchSize annotations.
3. Add the "book comment" entity, implement CRUD for the new entity.
4. Cover the repositories with tests, using the H2 database and the corresponding H2 Hibernate dialect for the tests.
5. Don't forget to disable DDL via Hibernate
6. @Transactional is recommended to put only on the methods of the service.