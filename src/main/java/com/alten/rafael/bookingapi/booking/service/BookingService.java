package com.alten.rafael.bookingapi.booking.service;

import com.alten.rafael.bookingapi.booking.exception.BookingException;
import com.alten.rafael.bookingapi.booking.model.Booking;
import com.alten.rafael.bookingapi.booking.model.BookingRepository;
import com.alten.rafael.bookingapi.booking.model.BookingStatus;
import com.alten.rafael.bookingapi.booking.validator.BookingValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class BookingService {

    public static final int LAST_DAY = 1;

    private final BookingRepository repository;

    public Booking findReservationByDate(LocalDate date) {
        BookingValidator.checkIfDateIsNull(date);
        return repository.findByDateInEquals(date);
    }

    public Set<Booking> findAllReservationsByDates(LocalDate dateIn, LocalDate dateOut) {
        BookingValidator.checkIfDatesAreValid(dateIn, dateOut);
        return findReservationsWithSameDates(dateIn, dateOut);
    }

    public Booking cancelReservation(Long reservationId) {
        Booking booking = repository.findById(reservationId).orElseThrow(() -> new BookingException("reservation not found!"));
        booking.setStatus(BookingStatus.CANCELED);
        return repository.save(booking);
    }

    public Booking saveReservation(Booking booking) {
        BookingValidator.checkIfDatesAreValid(booking.getDateIn(), booking.getDateOut());
        BookingValidator.checkIfStayIsTooLong(booking.getDateIn(), booking.getDateOut());
        BookingValidator.checkIfStayIsTooAdvanced(booking.getDateIn());
        BookingValidator.checkIfStayStartsAtLeastTomorrow(booking.getDateIn());

        Set<Booking> reservations = findReservationsWithSameDates(booking.getDateIn(), booking.getDateOut());
        BookingValidator.checkIfExistReservationConflict(reservations);

        booking.setStatus(BookingStatus.APPROVED);
        return repository.save(booking);
    }

    private Set<Booking> findReservationsWithSameDates(LocalDate dateIn, LocalDate dateOut) {
        List<LocalDate> datesBetween = getDatesBetween(dateIn, dateOut);
        Set<Booking> reservations = new HashSet<>();
        datesBetween.forEach(date -> {
            reservations.addAll(repository.findAllByDateInLessThanEqualAndDateOutGreaterThanEqual(
                    date, date));
        });
        return reservations;
    }

    private List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        return IntStream.iterate(0, i -> i + 1)
                .limit(numOfDaysBetween + LAST_DAY)
                .mapToObj(startDate::plusDays)
                .collect(Collectors.toList());
    }
}
