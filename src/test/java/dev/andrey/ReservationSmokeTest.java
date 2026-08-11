package dev.andrey;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservationSmokeTest {
    
    @Test
    void createInstaceDateNotNull() {
        var reservationBuilder = Reservation.builder()
        .WithReservedBy("Andrew")
        .withStart(Date.from(Instant.now().plus(1, ChronoUnit.SECONDS)))
        .withEnd(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
        .withRoom("room1");
        var reservationOpt = reservationBuilder.build();
        assertThat(reservationOpt.isPresent()).isTrue();;
    }
}
