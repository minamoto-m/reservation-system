package jp.github.minamoto.m.reservationsystem.security.auth.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jp.github.minamoto.m.reservationsystem.entity.AppUser;
import jp.github.minamoto.m.reservationsystem.repository.AppUserRepository;
import jp.github.minamoto.m.reservationsystem.service.exception.EmailAlreadyRegisteredException;
import jp.github.minamoto.m.reservationsystem.security.auth.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Spring Security の認証時に、ユーザー名（メールアドレス）からユーザー情報を取得する。
     * ログイン処理で {@link UserDetailsService} として呼ばれる。
     *
     * @param username ユーザー名（本システムではメールアドレスと同一）
     * @return 認証に必要なユーザー情報。存在しない場合は例外を投げる
     * @throws UsernameNotFoundException 指定したユーザー名のユーザーが存在しない場合
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRole().replace("ROLE_", ""))
            .build();
    }

    /**
     * ユーザー登録を行う。メールアドレス（username）の重複時は例外を投げる。
     * 
     * @param ユーザー登録リクエストDTO
     */
    public void create(RegisterRequest request) {
        if (appUserRepository.findByUsername(request.getEmail()).isPresent()) {
            throw new EmailAlreadyRegisteredException("このメールアドレスは既に登録されています");
        }
        AppUser user = new AppUser();
        user.setUsername(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_USER");
        appUserRepository.save(user);
    }
}
