package ru.otus.work25.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.work25.domain.User;

public interface UserRepository extends MongoRepository<User, Long> {
    @Query(value="{ 'username' : :#{#userName} }")
    User findByUsername(@Param("userName") String userName);
}