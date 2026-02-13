package jp.github.minamoto.m.reservationsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.github.minamoto.m.reservationsystem.entity.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}  
