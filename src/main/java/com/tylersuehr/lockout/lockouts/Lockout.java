package com.tylersuehr.lockout.lockouts;

import com.tylersuehr.lockout.users.EnabledStatus;
import lombok.Data;
import org.springframework.data.mongodb.core.index.HashIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;
import java.util.List;

/**
 * Represents a lockout counter for trying usernames.
 * @author Tyler Suehr
 */
@Data
@Document
public class Lockout {
    public static final byte MAX = 6;

    @MongoId
    @HashIndexed
    private String username;
    private Date lastAttemptedOn;
    private List<Attempt> attempts;
    private EnabledStatus status;

    @Data
    public static class Attempt {
        private final String guess;
        private String remoteAddr;
        private String remoteHost;
        private int remotePort;
    }
}
