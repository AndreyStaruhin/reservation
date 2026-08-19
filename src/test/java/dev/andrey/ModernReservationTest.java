package dev.andrey;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class ModernReservationTest {

    @Test
    public void TwoDifferentReservationsWithSameFields__EqualsEachOther() {
        var room = "room 1";
        var reservedBy = "Andrey";
        var start = Instant.now().plus(1, ChronoUnit.MINUTES);
        var end = start.plus(1, ChronoUnit.HOURS);
        var comment = "";

        var reservation1 = ModernReservation.make(room, reservedBy, start, end, comment);
        var reservation2 = ModernReservation.make(room, reservedBy, start, end, comment);

        assertThat(reservation1).isEqualTo(reservation2);
    }

    @Test
    public void TwoDifferentReservationsWithSameRoomStartAndEnd_differentReservedByAndComment__EqualsEachOther() {
        var room = "room 1";
        var reservedBy1 = "Andrey";
        var reservedBy2 = "Bob";
        var start = Instant.now().plus(1, ChronoUnit.MINUTES);
        var end = start.plus(1, ChronoUnit.HOURS);
        var comment1 = "Comment1";
        var comment2 = "Comment2";

        var reservation1 = ModernReservation.make(room, reservedBy1, start, end, comment1);
        var reservation2 = ModernReservation.make(room, reservedBy2, start, end, comment2);

        assertThat(reservation1).isEqualTo(reservation2);
    }

    @Test
    public void TwoDifferentReservationsWithDifferntRooms_AnotherFieldsSame__NOTEqualsEachOther() {
        var room1 = "room 1";
        var room2 = "room 2";
        var reservedBy = "Andrey";
        var start = Instant.now().plus(1, ChronoUnit.MINUTES);
        var end = start.plus(1, ChronoUnit.HOURS);
        var comment = "";

        var reservation1 = ModernReservation.make(room1, reservedBy, start, end, comment);
        var reservation2 = ModernReservation.make(room2, reservedBy, start, end, comment);

        assertThat(reservation1).isNotEqualTo(reservation2);
    }

    @Test
    public void CompareWithNull_ReturnFalse() {
        var room1 = "room 1";
        var reservedBy = "Andrey";
        var start = Instant.now().plus(1, ChronoUnit.MINUTES);
        var end = start.plus(1, ChronoUnit.HOURS);
        var comment = "";

        var reservation1 = ModernReservation.make(room1, reservedBy, start, end, comment);

        assertThat(reservation1.equals(null)).isFalse();
    }

    @Test
    public void TwoDifferentReservationsWithSameFields_haveSameHashCode() {
        var room = "room 1";
        var reservedBy = "Andrey";
        var start = Instant.now().plus(1, ChronoUnit.MINUTES);
        var end = start.plus(1, ChronoUnit.HOURS);
        var comment = "";

        var reservation1 = ModernReservation.make(room, reservedBy, start, end, comment);
        var reservation2 = ModernReservation.make(room, reservedBy, start, end, comment);

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
    public void makeTwoReservationsForNextDay_intersectionIsOrNot(String room1, String room2, String start1, String end1,
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
        var reservation1 = ModernReservation.make(room1, reservedBy, startDateTime1, 
            endDateTime1, "");

       var reservation2 =  ModernReservation.make(room2, reservedBy, startDateTime2, 
            endDateTime2, "");
    
       assertThat(reservation1.checkIintersectsWith(reservation2)).isEqualTo(intersected);
    }

    @Test
    public void setRoomAsNull_makeRejectNullRoom() {

        String room = null;
        var reservedBy = "Andrey";
        var start = Instant.now().plus(1, ChronoUnit.MINUTES);
        var end = start.plus(1, ChronoUnit.HOURS);
        var comment = "";

        assertThatExceptionOfType(NullPointerException.class).isThrownBy(()-> {

            ModernReservation.make(room, reservedBy, start, end, comment);
        }).withMessageContaining("room cannot be null");      
    }

    @Test
    public void setRoomAsEmpty_makeRejectEmptyRoom() {

        String room = "";
        var reservedBy = "Andrey";
        var start = Instant.now().plus(1, ChronoUnit.MINUTES);
        var end = start.plus(1, ChronoUnit.HOURS);
        var comment = "";

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()-> {

            ModernReservation.make(room, reservedBy, start, end, comment);
        }).withMessageContaining("Не указана комната");      
    }

    private Instant getNextDayDateTime(LocalTime time) {
        LocalDate date = LocalDate.now().plusDays(1);

        ZoneId zone = ZoneId.of("Europe/Moscow");

        ZonedDateTime zoned = ZonedDateTime.of(date, time, zone);

        return zoned.toInstant();
    }
}