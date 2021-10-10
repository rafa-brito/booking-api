package com.alten.rafael.bookingapi.booking.validator;


import com.alten.rafael.bookingapi.booking.exception.BookingException;
import com.alten.rafael.bookingapi.booking.model.Booking;
import com.alten.rafael.bookingapi.booking.model.BookingStatus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Set;

public class BookingValidator {

    private static final long MAX_STAY_PERIOD = 3;
    private static final long MAX_ADVANCE_PERIOD = 30;

    public static void checkIfDateIsNull(LocalDate date) {
        if(Objects.isNull(date))
            throw new BookingException("the date passed must be valid!");
    }

    public static void checkIfDatesAreValid(LocalDate dateIn, LocalDate dateOut) {
        if(Objects.isNull(dateIn) || Objects.isNull(dateOut))
            throw new BookingException("the date passed must be valid!");
        if(dateIn.isAfter(dateOut))
            throw new BookingException("the date in cannot be after date out!");
    }

    public static void checkIfStayIsTooLong(LocalDate dateIn, LocalDate dateOut) {
        if(ChronoUnit.DAYS.between(dateIn, dateOut.plusDays(1L)) > MAX_STAY_PERIOD){
            throw new BookingException(String.format("the stay cannot be more then %d days!", MAX_STAY_PERIOD));
        }
    }

    public static void checkIfStayIsTooAdvanced(LocalDate dateIn) {
        if(ChronoUnit.DAYS.between(LocalDate.now(), dateIn) > MAX_ADVANCE_PERIOD){
            throw new BookingException(String.format("the booking cannot be %d days after today!", MAX_ADVANCE_PERIOD));
        }
    }

    public static void checkIfStayStartsAtLeastTomorrow(LocalDate dateIn) {
        if(!dateIn.isAfter(LocalDate.now())){
            throw new BookingException("the booking cannot start before tomorrow!");
        }
    }

    public static void checkIfExistBookingConflict(Set<Booking> possibleConflicts) {
        possibleConflicts.forEach(r ->{
            if(BookingStatus.APPROVED.equals(r.getStatus()))
                throw new BookingException("there are active bookings that conflict with the booking!");
        });
    }
}
