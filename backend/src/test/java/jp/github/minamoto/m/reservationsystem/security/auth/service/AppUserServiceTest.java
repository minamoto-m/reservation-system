package jp.github.minamoto.m.reservationsystem.security.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import jp.github.minamoto.m.reservationsystem.entity.AppUser;
import jp.github.minamoto.m.reservationsystem.repository.AppUserRepository;
import jp.github.minamoto.m.reservationsystem.security.auth.dto.RegisterRequest;
import jp.github.minamoto.m.reservationsystem.service.exception.EmailAlreadyRegisteredException;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AppUserService appUserService;

    @Captor
    ArgumentCaptor<AppUser> userCaptor;

    @Test
    void create_success() {
        // Given: 入力（リクエストDTO）と、モックの振る舞いだけ用意する
        RegisterRequest request = new RegisterRequest();
        request.setEmail("user@example.com");
        request.setPassword("rawPassword");

        when(appUserRepository.findByUsername("user@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");

        // When: ユーザー登録を実行する
        appUserService.create(request);

        // Then: Repository の save が1回呼ばれたか確認しつつ、そのとき渡された引数をキャプチャして中身を assert する。
        verify(appUserRepository).save(userCaptor.capture());
        AppUser savedUser = userCaptor.getValue();

        assertThat(savedUser.getUsername()).isEqualTo("user@example.com");
        assertThat(savedUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(savedUser.getRole()).isEqualTo("ROLE_USER");
    }

    @Test
    void create_duplicateEmail_throwsAndSaveNotCalled() {
        // Given: 既に存在するメールアドレスで登録リクエストを送る
        RegisterRequest request = new RegisterRequest();
        request.setEmail("existing@example.com");
        request.setPassword("rawPassword");

        AppUser existingUser = new AppUser();
        existingUser.setId(1L);
        existingUser.setUsername("existing@example.com");
        existingUser.setPassword("encoded");
        existingUser.setRole("ROLE_USER");

        when(appUserRepository.findByUsername("existing@example.com")).thenReturn(Optional.of(existingUser));

        // When / Then: 登録を実行すると例外が投げられ、save は一度も呼ばれない
        assertThatThrownBy(() -> appUserService.create(request))
            .isInstanceOf(EmailAlreadyRegisteredException.class)
            .hasMessageContaining("このメールアドレスは既に登録されています");
        verify(appUserRepository, never()).save(any(AppUser.class));
    }
}
