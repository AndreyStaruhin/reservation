package dev.andrey;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
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
}
