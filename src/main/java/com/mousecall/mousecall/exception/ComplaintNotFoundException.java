package com.mousecall.mousecall.exception;

public class ComplaintNotFoundException extends RuntimeException {
    public ComplaintNotFoundException(Long id) {
        super("해당 ID의 민원을 찾을 수 없습니다: " + id);
    }
}
