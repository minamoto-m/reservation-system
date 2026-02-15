package jp.github.minamoto.m.reservationsystem.security.auth.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
