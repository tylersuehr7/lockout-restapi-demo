package com.tylersuehr.lockout.lockouts;

import com.tylersuehr.lockout.users.EnabledStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Defines a repository for the {@link Lockout}.
 * @author Tyler Suehr
 */
@Repository
public interface LockoutRepository extends MongoRepository<Lockout,String> {
    boolean existsByUsernameAndStatusEquals(String username, EnabledStatus status);
}
