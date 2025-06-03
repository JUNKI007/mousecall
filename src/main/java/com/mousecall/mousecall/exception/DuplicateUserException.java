package com.mousecall.mousecall.exception;

public class DuplicateUserException extends RuntimeException {

    public DuplicateUserException(String username) {
        super("이미 사용 중인 아이디입니다: " + username);
    }
}
