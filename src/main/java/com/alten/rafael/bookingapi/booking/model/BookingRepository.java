package com.alten.rafael.bookingapi.booking.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByDateinGreaterThanEqualAndDateoutLessThanEqual(LocalDate dateIn, LocalDate dateOut);
}
