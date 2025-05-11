package com.project.platform.dto;

import lombok.Data;

@Data
public class LoginDTO {
    private String username;
    private String password;
    private String type;
}
