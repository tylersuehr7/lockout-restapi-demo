package com.tylersuehr.lockout.users;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * Represents a generic user on the system.
 * @author Tyler Suehr
 */
@Data
@Document
public class User {
    @MongoId
    private String uid;
    private String nickname;
    private Account account;
}
