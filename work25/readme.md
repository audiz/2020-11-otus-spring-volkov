Enter authorization based on URL and / or domain entities
Goal: learn how to protect the application with full authorization and access rights differentiation
The result: a full-fledged application with security based on Spring Security
Attention! The task is based on a non-active Sping MVC application!

1. Minimum: configure URL-level authorization in the app.
2. Maximum: configure in-app authorization based on domain entities and service methods.

Recommendations for implementation:
1. It is not recommended to allocate users with different rights to different classes - that is, just one user class.
2. In the case of authorization based on domain entities and PostgreSQL, do not use the GUID for entities.

ACL with Mongo
https://github.com/RovoMe/spring-security-acl-mongodb