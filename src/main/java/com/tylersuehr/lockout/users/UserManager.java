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

import com.tylersuehr.lockout.users.params.SignInParams;
import com.tylersuehr.lockout.users.params.SignUpParams;

import javax.servlet.ServletRequest;

/**
 * Defines a domain-layer abstraction for users.
 * @author Tyler Suehr
 */
public interface UserManager {
    User signIn(SignInParams params, ServletRequest request);
    User signUp(SignUpParams params);
}
