package dev.andrey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Reservation
 */
public class Reservation {

    private String room;
    public String getRoom() {
        return room;
    }

    private String reservedBy;
    public String getReservedBy() {
        return reservedBy;
    }

    private Date start;
    public Date getStart() {
        return start;
    }

    private Date end;
    public Date getEnd() {
        return end;
    }

    private String comment;
    public String getComment() {
        return comment;
    }

    private Reservation() {   }

    public Optional<Reservation> extend(Date newTime) {
        return new ReservationBuilder(this)
                .withEnd(newTime).build();
    }

    public static ReservationBuilder builder() {
        return new ReservationBuilder();
    }

    /**
     * ReservationBuilder
     */
    public static class ReservationBuilder {
         
        private final Reservation reservation;
        private List<String> errors = new ArrayList<>();

        public List<String> getErrors() {
            return errors;
        }

        public ReservationBuilder() {
            reservation = new Reservation();
        }

        public ReservationBuilder(Reservation reservation) {
            this.reservation = reservation;
        }
        
        public ReservationBuilder withRoom(String room) {
            reservation.room = room;
            return this;
        }

        public ReservationBuilder WithReservedBy(String reservedBy) {
            reservation.reservedBy = reservedBy;
            return this;
        }

        public ReservationBuilder withStart(Date start) {
            reservation.start = start;
            return this;
        }

        public ReservationBuilder withEnd(Date end) {
            reservation.end = end;
            return this;
        }

        public ReservationBuilder withComment(String comment) {
            reservation.comment = comment;
            return this;
        }

        private void validate() {
            if(reservation.start.compareTo(reservation.end) > 0) {
                errors.add("Дата начала должна быть больше даты конца");
            }
            else {
                long diffMillis = reservation.end.getTime() - reservation.start.getTime();
                long maxReservMillis = 8 * 60 * 60 * 1000;
                if(diffMillis > maxReservMillis) {
                    errors.add("Нельзя резервировать больше чем на 8 часов");
                }
            }

            if(reservation.start.compareTo(Date.from(Instant.now())) < 0) {
                errors.add("Нельзя забронировать на время в прошлом");
            }
            
            if(reservation.room.isBlank()) {
                errors.add("Не указана комната");
            }

            if(reservation.reservedBy.isBlank()){
                errors.add("Не указано имя бронирующего");
            }
        }

        public Optional<Reservation> build() {
            validate();

            if(errors.isEmpty()) {
                return Optional.of(reservation);
            }
            else {
                return Optional.empty();
            }

        }
    }
}