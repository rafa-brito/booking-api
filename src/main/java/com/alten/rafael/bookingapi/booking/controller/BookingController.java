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
    public Booking getBooking(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return service.findBookingByDate(date);
    }

    @GetMapping
    public Set<Booking> getBookingByRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOut){
        return  service.findAllBookingsByDates(dateIn, dateOut);
    }

    @PostMapping("/booking")
    public Booking makeBooking(@Valid @RequestBody Booking booking){
        return service.saveBooking(booking);
    }

    @PutMapping("/booking")
    public Booking changeBooking(@Valid @RequestBody Booking booking){
        return service.modifyBooking(booking);
    }

    @DeleteMapping("/booking/{id}")
    public Booking cancelBooking(@PathVariable("date") Long id){
        return service.cancelBooking(id);
    }
}
