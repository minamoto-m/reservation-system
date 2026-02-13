package jp.github.minamoto.m.reservationsystem.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jp.github.minamoto.m.reservationsystem.entity.AppUser;
import jp.github.minamoto.m.reservationsystem.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {
    private final AppUserRepository appUserRepository;

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
}
