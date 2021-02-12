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

package com.tylersuehr.lockout.endpoints;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Custom error handler.
 * @author Tyler Suehr
 */
@RestController
@ControllerAdvice
class ErrorController {
    @ExceptionHandler(Exception.class)
    ResponseEntity<Err> handle(Exception cause, HttpServletRequest request) {
        final HttpStatus status = getStatusOrDefault(cause);
        final String errMsg = cause.getLocalizedMessage();

        final Err error = new Err();
        error.setTimestamp(new Date());
        error.setPath(request.getRequestURI());
        error.setMessage(errMsg == null || errMsg.length() <= 0 ? "Something went wrong" : errMsg);
        error.setError(String.format("(%d) %s", status.value(), status.getReasonPhrase()));

        return new ResponseEntity<>(error, status);
    }

    private HttpStatus getStatusOrDefault(Exception cause) {
        final ResponseStatus status = cause.getClass().getAnnotation(ResponseStatus.class);
        if (status == null)
            return HttpStatus.INTERNAL_SERVER_ERROR;
        return status.value();
    }

    @Data
    static class Err {
        private Date timestamp;
        private String path;
        private String error;
        private String message;
    }
}
