package dev.andrey;

import java.util.List;

public sealed interface ReservationResult permits ReservationResult.Booked, ReservationResult.Rejected {
    

    public record Booked(ModernReservation reservation) implements ReservationResult {} 
    public record Rejected(List<String> errors) implements ReservationResult {}
}
