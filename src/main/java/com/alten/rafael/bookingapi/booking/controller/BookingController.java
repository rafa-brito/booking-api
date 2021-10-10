package com.alten.rafael.bookingapi.booking.controller;

import com.alten.rafael.bookingapi.booking.model.Booking;
import com.alten.rafael.bookingapi.booking.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {

    private BookingService service;

    @GetMapping("/booking/{date}")
    public Booking getReservation(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return service.findReservationByDate(date);
    }

    @GetMapping
    public Set<Booking> getReservationByRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOut){
        return  service.findAllReservationsByDates(dateIn, dateOut);
    }

    @PostMapping("/booking")
    public Booking makeReservation(@Valid @RequestBody Booking booking){
        return service.saveReservation(booking);
    }

    @DeleteMapping("/booking/{id}")
    public Booking cancelReservation(@PathVariable("date") Long id){
        return service.cancelReservation(id);
    }
}
