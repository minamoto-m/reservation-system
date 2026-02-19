package jp.github.minamoto.m.reservationsystem.security.auth.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    /** メールアドレス（必須・重複チェックに使用） */
    private String email;
    private String password;
}
