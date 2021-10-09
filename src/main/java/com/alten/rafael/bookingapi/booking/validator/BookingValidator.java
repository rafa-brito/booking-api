package com.alten.rafael.bookingapi.booking.validator;


import com.alten.rafael.bookingapi.booking.exception.BookingException;

import java.time.LocalDate;
import java.util.Objects;

public class BookingValidator {

    public static void checkIfDateIsNull(LocalDate date) {
        if(Objects.isNull(date))
            throw new BookingException("the date passed must be valid!");
    }
}
