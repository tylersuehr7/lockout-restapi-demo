/*
 * Copyright 2021 Tyler R. Suehr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tylersuehr.lockout.users;

import com.tylersuehr.lockout.lockouts.Lockout;
import com.tylersuehr.lockout.lockouts.LockoutRepository;
import com.tylersuehr.lockout.security.PasswordEncoder;
import com.tylersuehr.lockout.users.params.SignInParams;
import com.tylersuehr.lockout.users.params.SignUpParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletRequest;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

/**
 * Default implementation of {@link UserManager}.
 * @author Tyler Suehr
 */
@Service
class UserManagerImpl implements UserManager {
    private UserRepository mUserRepository;
    private LockoutRepository mLockoutRepository;
    private PasswordEncoder mPasswordEncoder;

    @Transactional
    @Override
    public User signIn(SignInParams params, ServletRequest request) {
        // Check if the username has been blacklisted
        if (mLockoutRepository.existsByUsernameAndStatusEquals(params.getUsername(), EnabledStatus.DISABLED))
            throw new SecurityException("Username is locked. Please contact support!");

        // Authenticate the username and password
        final User found = mUserRepository.findByAccountUsername(params.getUsername()).orElse(null);
        if (found == null || !mPasswordEncoder.matches(params.getPassword(), found.getAccount().getPassword())) {
            incrementLockout(params, request);
            throw new SecurityException("Invalid login attempt!");
        }

        resetLockout(params);
        return found;
    }

    @Transactional
    @Override
    public User signUp(SignUpParams params) {
        // Check if the username is already taken
        if (mUserRepository.existsByAccountUsername(params.getUsername()))
            throw new IllegalArgumentException("Username is unavailable!");

        // Create the account object
        final Account account = new Account();
        account.setUsername(params.getUsername());
        account.setPassword(mPasswordEncoder.encode(params.getPassword()));
        account.setCreatedOn(new Date());
        account.setLastUpdatedOn(account.getCreatedOn());
        account.setStatus(EnabledStatus.ENABLED);

        // Create the user object
        final User user = new User();
        user.setUid(UUID.randomUUID().toString());
        user.setNickname(params.getNickname());
        user.setAccount(account);

        return mUserRepository.save(user);
    }

    private void incrementLockout(SignInParams params, ServletRequest request) {
        final Lockout lockout = mLockoutRepository.findById(params.getUsername()).orElseGet(() -> {
            final Lockout newLockout = new Lockout();
            newLockout.setUsername(params.getUsername());
            newLockout.setLastAttemptedOn(new Date());
            newLockout.setAttempts(new LinkedList<>());
            newLockout.setStatus(EnabledStatus.ENABLED);
            return newLockout;
        });

        // Describe the attempted password
        final Lockout.Attempt attempt = new Lockout.Attempt(params.getPassword());
        attempt.setRemoteHost(request.getRemoteHost());
        attempt.setRemoteAddr(request.getRemoteAddr());
        attempt.setRemotePort(request.getRemotePort());

        lockout.getAttempts().add(attempt);
        if (lockout.getAttempts().size() >= Lockout.MAX) {
            lockout.setStatus(EnabledStatus.DISABLED);
        }

        mLockoutRepository.save(lockout);
    }

    private void resetLockout(SignInParams params) {
        mLockoutRepository.deleteById(params.getUsername());
    }

    @Autowired
    void setUserRepository(UserRepository userRepository) {
        mUserRepository = userRepository;
    }

    @Autowired
    void setLockoutRepository(LockoutRepository lockoutRepository) {
        mLockoutRepository = lockoutRepository;
    }

    @Autowired
    void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        mPasswordEncoder = passwordEncoder;
    }
}
