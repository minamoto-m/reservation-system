package jp.github.minamoto.m.reservationsystem.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jp.github.minamoto.m.reservationsystem.dto.ReservationCreateRequestDto;
import jp.github.minamoto.m.reservationsystem.entity.Reservation;

@SpringBootTest
class ReservationServiceTest {

    @Autowired
    ReservationService reservationService;

    @Test
    void create_success() {
        // given（前提）
        ReservationCreateRequestDto dto = new ReservationCreateRequestDto();
        dto.setReservationDate(LocalDate.of(2026, 1, 24));
        dto.setStartTime(LocalTime.of(9, 0));
        dto.setEndTime(LocalTime.of(9, 30));
        dto.setName("テストユーザー");
        dto.setPhoneNumber("09012345678");

        // when（実行）
        Reservation result = reservationService.create(dto);

        // then（検証）
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("テストユーザー失敗");
        assertThat(result.getStartTime()).isEqualTo(LocalTime.of(9, 0));
    }
}