package dev.andrey;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class ModernReservationTest {

    @Test
    public void reservation_sameFields_isEqual() {
        var room = "room 1";
        var reservedBy = "Andrey";
        var start = Instant.now().plus(1, ChronoUnit.MINUTES);
        var end = start.plus(1, ChronoUnit.HOURS);
        var comment = "";
        var price1 = new BigDecimal("100.00");

        var reservation1 = ModernReservation.make(room, reservedBy, start, end, price1, comment);
        var reservation2 = ModernReservation.make(room, reservedBy, start, end, price1, comment);

        assertThat(reservation1).isEqualTo(reservation2);
    }

    @Test
    public void twoReservations_sameRoomStartAndEnd_areEqual() {
        var room = "room 1";
        var reservedBy1 = "Andrey";
        var reservedBy2 = "Bob";
        var start = Instant.now().plus(1, ChronoUnit.MINUTES);
        var end = start.plus(1, ChronoUnit.HOURS);
        var comment1 = "Comment1";
        var comment2 = "Comment2";

        var price1 = new BigDecimal("100.00");

        var reservation1 = ModernReservation.make(room, reservedBy1, start, end, price1, comment1);
        var reservation2 = ModernReservation.make(room, reservedBy2, start, end, price1, comment2);

        assertThat(reservation1).isEqualTo(reservation2);
    }

    @Test
    public void twoReservations_onlyRoomsDifferent_areDifferent() {
        var room1 = "room 1";
        var room2 = "room 2";
        var reservedBy = "Andrey";
        var start = Instant.now().plus(1, ChronoUnit.MINUTES);
        var end = start.plus(1, ChronoUnit.HOURS);
        var comment = "";
        var price1 = new BigDecimal("100.00");

        var reservation1 = ModernReservation.make(room1, reservedBy, start, end, price1, comment);
        var reservation2 = ModernReservation.make(room2, reservedBy, start, end, price1, comment);

        assertThat(reservation1).isNotEqualTo(reservation2);
    }

    @Test
    public void reservation_compareWithNull_isNotEqual() {
        var room1 = "room 1";
        var reservedBy = "Andrey";
        var start = Instant.now().plus(1, ChronoUnit.MINUTES);
        var end = start.plus(1, ChronoUnit.HOURS);
        var comment = "";
        var price1 = new BigDecimal("100.00");

        var reservation1 = ModernReservation.make(room1, reservedBy, start, end, price1, comment);

        assertThat(reservation1.equals(null)).isFalse();
    }

    @Test
    public void hashCode_sameIdentityFields_isEqual() {
        var room = "room 1";
        var reservedBy = "Andrey";
        var start = Instant.now().plus(1, ChronoUnit.MINUTES);
        var end = start.plus(1, ChronoUnit.HOURS);
        var comment = "";
        var price1 = new BigDecimal("100.00");

        var reservation1 = ModernReservation.make(room, reservedBy, start, end, price1, comment);
        var reservation2 = ModernReservation.make(room, reservedBy, start, end, price1, comment);

        assertThat(reservation1.hashCode()).isEqualTo(reservation2.hashCode());
    }

    @ParameterizedTest()
    @CsvSource({
    "room1, room1, 10:00, 11:00,  11:00, 12:00, false"
    ,"room1, room1, 10:00, 11:00,  10:30, 11:30, true"
    ,"room1, room1, 10:00, 12:00,  10:30, 11:00, true"
    ,"room1, room1, 10:00, 11:00,  10:00, 11:00, true"
    ,"room1, room1, 10:00, 11:00,  09:00, 09:30, false"
    ,"room1, room2, 10:00, 11:00,  10:30, 11:30, false"
    ,"room1, room1, 10:00, 11:00,  10:30, 10:45, true"
    ,"room1, room1, 10:00, 11:00,  09:30, 11:45, true"})
    public void twoReservations_twoTimeSlots_intersects(String room1, String room2, String start1, String end1,
            String start2, String end2, boolean intersected) {

        LocalTime startime1 = LocalTime.parse(start1);
        LocalTime endTime1 = LocalTime.parse(end1);

        LocalTime startime2 = LocalTime.parse(start2);
        LocalTime endTime2 = LocalTime.parse(end2);

        Instant startDateTime1 = getNextDayDateTime(startime1);
        Instant endDateTime1 = getNextDayDateTime(endTime1);

        Instant startDateTime2 = getNextDayDateTime(startime2);
        Instant endDateTime2 = getNextDayDateTime(endTime2);

        String reservedBy = "Andrey";
        var price1 = new BigDecimal("100.00");
        var reservation1 = ModernReservation.make(room1, reservedBy, startDateTime1, 
            endDateTime1, price1, "");

       var reservation2 =  ModernReservation.make(room2, reservedBy, startDateTime2, 
            endDateTime2, price1, "");
    
       assertThat(reservation1.checkIintersectsWith(reservation2)).isEqualTo(intersected);
    }

    @ParameterizedTest()
    @CsvSource({
        ",  10:00, 11:00, Andrey, room cannot be null",
        "room1,  , 11:00, Andrey, start cannot be null",
        "room1, 10:00 , , Andrey, end cannot be null",
        "room1, 10:00 , 11:00, , reservedBy cannot be null"
    })
    public void reservation_paramIsNull_rejectByNull(String room,  String start, String end, String reservedBy, String errorMessage) {
        LocalTime startTime = start == null ? null : LocalTime.parse(start);
        Instant startDateTime = getNextDayDateTime(startTime);

        LocalTime endTime = end == null ? null : LocalTime.parse(end);
        Instant endDateTime = getNextDayDateTime(endTime);
        var price1 = new BigDecimal("100.00");

        var comment = "";

        assertThatExceptionOfType(NullPointerException.class).isThrownBy(()-> {

            ModernReservation.make(room, reservedBy, startDateTime, endDateTime, price1, comment);
        }).withMessageContaining(errorMessage);      
    }

    @Test
    public void reservation_comparedWithBaseObject_isNotEqual() {
        var room = "room 1";
        var reservedBy = "Andrey";
        var start = Instant.now().plus(1, ChronoUnit.MINUTES);
        var end = start.plus(1, ChronoUnit.HOURS);
        var comment = "";
        var price1 = new BigDecimal("100.00");

        var reservation1 = ModernReservation.make(room, reservedBy, start, end, price1, comment);

        var nonReservation = new Object();

        assertThat(reservation1.equals(nonReservation)).isFalse();
    }

    @Test
    public void reservation_roomIsEmpty_rejectByEmpty() {

        String room = "";
        var reservedBy = "Andrey";
        var start = Instant.now().plus(1, ChronoUnit.MINUTES);
        var end = start.plus(1, ChronoUnit.HOURS);
        var comment = "";
        var price1 = new BigDecimal("100.00");

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()-> {

            ModernReservation.make(room, reservedBy, start, end, price1, comment);
        }).withMessageContaining("Не указана комната");      
    }

    @Test
    public void reservation_priceWithScaleEquas1_sacaleEquals2() {
        var room = "room 1";
        var reservedBy = "Andrey";
        var start = Instant.now().plus(1, ChronoUnit.MINUTES);
        var end = start.plus(1, ChronoUnit.HOURS);
        var comment = "";

        var price1 = new BigDecimal("100.0");

        var reservation1 = ModernReservation.make(room, reservedBy, start, end, price1, comment);

        assertThat(reservation1.getPrice().scale()).isEqualTo(2);
    }

    private Instant getNextDayDateTime(LocalTime time) {
        if (time == null) {
            return null;
        }
        LocalDate date = LocalDate.now().plusDays(1);

        ZoneId zone = ZoneId.of("Europe/Moscow");

        ZonedDateTime zoned = ZonedDateTime.of(date, time, zone);

        return zoned.toInstant();
    }
}