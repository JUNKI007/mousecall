package com.mousecall.mousecall.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    public String code;
    public String message;
}
