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

    public Booking findBookingByDate(LocalDate date) {
        BookingValidator.checkIfDateIsNull(date);
        return repository.findByDateInEquals(date);
    }

    public Set<Booking> findAllBookingsByDates(LocalDate dateIn, LocalDate dateOut) {
        BookingValidator.checkIfDatesAreValid(dateIn, dateOut);
        return findBookingsWithSameDates(dateIn, dateOut);
    }

    public Booking cancelBooking(Long bookingId) {
        Booking booking = repository.findById(bookingId).orElseThrow(() -> new BookingException("booking not found!"));
        booking.setStatus(BookingStatus.CANCELED);
        return repository.save(booking);
    }

    public Booking saveBooking(Booking booking) {
        BookingValidator.checkIfDatesAreValid(booking.getDateIn(), booking.getDateOut());
        BookingValidator.checkIfStayIsTooLong(booking.getDateIn(), booking.getDateOut());
        BookingValidator.checkIfStayIsTooAdvanced(booking.getDateIn());
        BookingValidator.checkIfStayStartsAtLeastTomorrow(booking.getDateIn());

        Set<Booking> bookings = findBookingsWithSameDates(booking.getDateIn(), booking.getDateOut());
        BookingValidator.checkIfExistBookingConflict(bookings);

        booking.setStatus(BookingStatus.APPROVED);
        return repository.save(booking);
    }

    public Booking modifyBooking(Booking booking) {
        BookingValidator.checkIfIdIsValid(booking.getId());
        BookingValidator.checkIfDatesAreValid(booking.getDateIn(), booking.getDateOut());
        BookingValidator.checkIfStayIsTooLong(booking.getDateIn(), booking.getDateOut());
        BookingValidator.checkIfStayIsTooAdvanced(booking.getDateIn());
        BookingValidator.checkIfStayStartsAtLeastTomorrow(booking.getDateIn());

        Set<Booking> bookings = findBookingsWithSameDates(booking.getDateIn(), booking.getDateOut());
        BookingValidator.checkIfExistBookingConflict(bookings);

        Booking previousBooking = repository.findById(booking.getId()).orElseThrow(() -> new BookingException("booking Id not found!"));

        BookingValidator.checkIfPreviousBookingIsValid(previousBooking);

        previousBooking.setDateIn(booking.getDateIn());
        previousBooking.setDateOut(booking.getDateOut());
        return repository.save(previousBooking);
    }

    private Set<Booking> findBookingsWithSameDates(LocalDate dateIn, LocalDate dateOut) {
        List<LocalDate> datesBetween = getDatesBetween(dateIn, dateOut);
        Set<Booking> bookings = new HashSet<>();
        datesBetween.forEach(date -> {
            bookings.addAll(repository.findAllByDateInLessThanEqualAndDateOutGreaterThanEqual(
                    date, date));
        });
        return bookings;
    }

    private List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        return IntStream.iterate(0, i -> i + 1)
                .limit(numOfDaysBetween + LAST_DAY)
                .mapToObj(startDate::plusDays)
                .collect(Collectors.toList());
    }
}
