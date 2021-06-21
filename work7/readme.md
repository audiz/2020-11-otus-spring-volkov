Create an application that stores information about books in the library
Goal: use the capabilities of Spring JDBC and spring-boot-starter-jdbc to connect to relational databases
Result: an application with data storage in a relational database, which we will develop in the future
This homework assignment is NOT based on the previous one.

1. Use Spring JDBC and a relational database (H2 or a real relational database). We strongly recommend using NamedParametersJdbcTemplate
2. Provide tables of authors, books, and genres.
3. The many-to-one relationship is assumed (the book has the same author and genre). Optional complication - many-to-many relationships (a book can have many authors and / or genres).
4. The interface is executed on the Spring Shell (CRUD books are required, operations with authors and genres - as it will be convenient).
5. The script for creating tables and the script for filling in data should be automatically started using spring-boot-starter-jdbc.
6. Cover with tests as much as possible.

Recommendations for the implementation of the work:
1. DON'T do AbstractDAO.
2. DON'T do inheritance in tests