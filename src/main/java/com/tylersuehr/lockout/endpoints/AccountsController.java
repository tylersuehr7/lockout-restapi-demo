package com.tylersuehr.lockout.endpoints;

import com.tylersuehr.lockout.users.User;
import com.tylersuehr.lockout.users.params.SignInParams;
import com.tylersuehr.lockout.users.params.SignUpParams;
import com.tylersuehr.lockout.users.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;

/**
 * Defines restful api endpoints for accounts.
 * @author Tyler Suehr
 */
@RestController
@RequestMapping("/v1/accounts")
class AccountsController {
    private UserManager mUserManager;

    @PostMapping("/sign-in")
    public User signIn(@RequestBody SignInParams params, ServletRequest request) {
        return mUserManager.signIn(params, request);
    }

    @PostMapping("/sign-up")
    public User signUp(@RequestBody SignUpParams params) {
        return mUserManager.signUp(params);
    }

    @Autowired
    void setUserManager(UserManager userManager) {
        mUserManager = userManager;
    }
}
