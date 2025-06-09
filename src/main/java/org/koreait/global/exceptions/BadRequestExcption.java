package org.koreait.global.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestExcption extends CommonException {
    public BadRequestExcption(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
