package com.mousecall.mousecall.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserJoinRequest {
    private String username;
    private String password;
}
