package com.tylersuehr.lockout.users;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

/**
 * Represents the account information for a user.
 * @author Tyler Suehr
 */
@Data
public class Account {
    @Indexed(unique = true)
    private String username;
    private String password;
    private Date createdOn;
    private Date lastUpdatedOn;
    private EnabledStatus status;
}
