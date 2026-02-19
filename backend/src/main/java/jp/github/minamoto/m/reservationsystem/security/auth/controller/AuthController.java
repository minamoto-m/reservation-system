package jp.github.minamoto.m.reservationsystem.security.auth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;

import jp.github.minamoto.m.reservationsystem.security.auth.dto.LoginRequest;
import jp.github.minamoto.m.reservationsystem.security.auth.dto.RegisterRequest;
import jp.github.minamoto.m.reservationsystem.security.auth.jwt.JwtService;
import jp.github.minamoto.m.reservationsystem.security.auth.service.AppUserService;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AppUserService appUserService;
    private final String cookieName;
    private final int cookieMaxAge;

    public AuthController(JwtService jwtService, UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder, AppUserService appUserService,
            @Value("${app.jwt.cookie-name:token}") String cookieName,
            @Value("${app.jwt.max-age-seconds:86400}") int cookieMaxAge) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.appUserService = appUserService;
        this.cookieName = cookieName;
        this.cookieMaxAge = cookieMaxAge;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        if (request == null || request.getUsername() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("username and password are required");
        }

        UserDetails user;
        try {
            user = userDetailsService.loadUserByUsername(request.getUsername());
        } catch (UsernameNotFoundException e) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String role = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");

        String token = jwtService.generateToken(user.getUsername(), role);

        ResponseCookie cookie = ResponseCookie.from(cookieName, token)
                .httpOnly(true)
                .secure(false)  // 本番でHTTPSの場合は true に変更
                .path("/")
                .maxAge(cookieMaxAge)
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.noContent().build();
    }

    /**
     * 認証状態を確認する。認証済みの場合のみ 200 を返す。
     * 未認証の場合は Spring Security が 401 を返す。
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> me(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(Map.of("username", user.getUsername()));
    }

    /**
     * ユーザー登録を行う。
     * @param request ユーザー登録リクエストDTO
     * @return ユーザー登録レスポンスDTO
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        appUserService.create(request);
        return ResponseEntity.noContent().build();
    }
}