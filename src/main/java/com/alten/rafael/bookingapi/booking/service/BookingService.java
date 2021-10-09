package com.alten.rafael.bookingapi.booking.service;

import com.alten.rafael.bookingapi.booking.model.Booking;
import com.alten.rafael.bookingapi.booking.model.BookingRepository;
import com.alten.rafael.bookingapi.booking.model.BookingStatus;
import com.alten.rafael.bookingapi.booking.validator.BookingValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class BookingService {

    private BookingRepository repository;

    public Booking findReservationByDate(LocalDate date){
        BookingValidator.checkIfDateIsNull(date);
        return repository.findAllByDateinGreaterThanEqualAndDateoutLessThanEqual(date, date).get(0);
    }

    public List<Booking> findAllReservationsByDates(LocalDate dateIn, LocalDate dateOut) {
        BookingValidator.checkIfDateIsNull(dateIn);
        BookingValidator.checkIfDateIsNull(dateOut);
        return repository.findAllByDateinGreaterThanEqualAndDateoutLessThanEqual(dateIn, dateOut);
    }

    public Booking saveReservation(Booking booking){
        booking.setStatus(BookingStatus.APPROVED);
        return repository.save(booking);
    }

    public Booking cancelReservation(Booking booking){
        booking.setStatus(BookingStatus.CANCELED);
        return repository.save(booking);
    }
}
