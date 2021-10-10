package com.alten.rafael.bookingapi.booking.validator;

import com.alten.rafael.bookingapi.booking.exception.BookingException;
import com.alten.rafael.bookingapi.booking.model.Booking;
import com.alten.rafael.bookingapi.booking.model.BookingStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BookingValidatorTest {

    @Test
    void checkIfDateIsNullWithSuccess() {
        assertDoesNotThrow(() -> BookingValidator.checkIfDateIsNull(LocalDate.now()));
    }

    @Test
    void checkIfDateIsNullWithError() {
        assertThrows(BookingException.class, () ->{
            BookingValidator.checkIfDateIsNull(null);
        });
    }

    @Test
    void checkIfDatesAreValidWithSuccess() {
        assertDoesNotThrow(() ->{
            BookingValidator.checkIfDatesAreValid(LocalDate.now(), LocalDate.now().plusDays(1L));
        });
    }

    @Test
    void checkIfDatesAreValidWithError() {
        assertThrows(BookingException.class, () ->{
            BookingValidator.checkIfDatesAreValid(LocalDate.now().plusDays(1L), LocalDate.now());
        });
        assertThrows(BookingException.class, () ->{
            BookingValidator.checkIfDatesAreValid( null, LocalDate.now().plusDays(1L));
        });
    }

    @Test
    void checkIfStayIsTooLongWithSuccess() {
        assertDoesNotThrow(() ->{
            BookingValidator.checkIfStayIsTooLong(LocalDate.now(), LocalDate.now().plusDays(1L));
        });
    }

    @Test
    void checkIfStayIsTooLongWithError() {
        assertThrows(BookingException.class, () ->{
            BookingValidator.checkIfStayIsTooLong(LocalDate.now(), LocalDate.now().plusDays(5L));
        });
    }

    @Test
    void checkIfStayIsTooAdvancedWithSuccess() {
        assertDoesNotThrow(() ->{
            BookingValidator.checkIfStayIsTooAdvanced(LocalDate.now().plusDays(1L));
        });
    }

    @Test
    void checkIfStayIsTooAdvancedWithError() {
        assertThrows(BookingException.class, () ->{
            BookingValidator.checkIfStayIsTooAdvanced(LocalDate.now().plusMonths(2L));
        });
    }

    @Test
    void checkIfStayStartsAtLeastTomorrowWithSuccess() {
        assertDoesNotThrow(() ->{
            BookingValidator.checkIfStayStartsAtLeastTomorrow(LocalDate.now().plusDays(1L));
        });
    }

    @Test
    void checkIfStayStartsAtLeastTomorrowWithError() {
        assertThrows(BookingException.class, () ->{
            BookingValidator.checkIfStayStartsAtLeastTomorrow(LocalDate.now());
        });
    }

    @Test
    void checkIfExistBookingConflictWithSuccess() {
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.CANCELED);
        Set<Booking> bookings = Collections.singleton(booking);
        assertDoesNotThrow(() ->{
            BookingValidator.checkIfExistBookingConflict(bookings);
        });
    }

    @Test
    void checkIfExistBookingConflictWithError() {
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.APPROVED);
        Set<Booking> bookings = Collections.singleton(booking);
        assertThrows(BookingException.class, () ->{
            BookingValidator.checkIfExistBookingConflict(bookings);
        });
    }
}