package dev.andrey;

import java.util.ArrayList;
import java.util.List;
import java.time.*;

/**
 * Reservation
 */
public class ModernReservation {

    private final String room;
    public String getRoom() {
        return room;
    }

    private final String reservedBy;
    public String getReservedBy() {
        return reservedBy;
    }

    private final Instant start;
    public Instant getStart() {
        return start;
    }

    private final Instant end;
    public Instant getEnd() {
        return end;
    }

    private final String comment;
    public String getComment() {
        return comment;
    }

    private List<String> baseValidation(String room, String reservedBy, Instant start, Instant end, String comment) {
        var errors = new ArrayList<String>();
        if (start.isAfter(end)) {
            errors.add("Дата начала должна быть больше даты конца");
        } else {
            long diffMillis = Duration.between(start, end).toMillis();
            long maxReservMillis = 8 * 60 * 60 * 1000;
            if (diffMillis > maxReservMillis) {
                errors.add("Нельзя резервировать больше чем на 8 часов");
            }
        }

        if (room.isBlank()) {
            errors.add("Не указана комната");
        }

        if (reservedBy.isBlank()) {
            errors.add("Не указано имя бронирующего");
        }

        return errors;
    }

    private ModernReservation(String room, String reservedBy, Instant start, Instant end, String comment) {
        var errors = baseValidation(room, reservedBy, start, end, comment); 

    
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(errors.toString());
        }

        this.room = room;
        this.reservedBy = reservedBy;
        this.start = start;
        this.end = end;
        this.comment = comment;
    }

    public static ModernReservation make(String room, String reservedBy, Instant start, Instant end, String comment) {
        if (start.isBefore(Instant.now())) {
            throw new IllegalArgumentException("Нельзя забронировать на время в прошлом");
        }
        return new ModernReservation(room, reservedBy, start, end, comment); 
    }

    public ModernReservation extend(Instant newEndTime) {

        return new ModernReservation(room, reservedBy, start, newEndTime, comment);
    }

    public ModernReservation passToAnotherRoom(String anotherRoom) {
        return new ModernReservation(anotherRoom, reservedBy, start, end, comment);
    }
}