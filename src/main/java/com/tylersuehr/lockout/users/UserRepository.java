package com.tylersuehr.lockout.users;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Defines a repository for the {@link User}.
 * @author Tyler Suehr
 */
@Repository
public interface UserRepository extends MongoRepository<User,String> {
    Optional<User> findByAccountUsername(String username);
    boolean existsByAccountUsername(String username);
}
