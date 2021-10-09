package com.alten.rafael.bookingapi.booking.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Entity
public class Booking {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "date in is invalid!")
    private LocalDate dateIn;

    @NotNull(message = "date out is invalid!")
    private LocalDate dateOut;

    private BookingStatus status;

    @NotNull(message = "client id is invalid!")
    private Long clientId;
}
