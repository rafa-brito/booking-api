package com.alten.rafael.bookingapi.booking.service;

import com.alten.rafael.bookingapi.booking.model.Booking;
import com.alten.rafael.bookingapi.booking.model.BookingRepository;
import com.alten.rafael.bookingapi.booking.model.BookingStatus;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository repository;

    @InjectMocks
    private BookingService service;

    @Test
    void findBookingByDate() {
        //GIVEN
        LocalDate date = LocalDate.now();
        Booking expectedBooking = new Booking();
        //WHEN
        when(repository.findByDateInEquals(date)).thenReturn(expectedBooking);
        Booking actualBooking = service.findBookingByDate(date);
        //THEN
        assertEquals(expectedBooking, actualBooking, "the returned booking do not match with the expected");
    }

    @Test
    void findAllBookingsByDates() {
        //GIVEN
        LocalDate dateIn = LocalDate.now();
        LocalDate dateOut = dateIn.plusDays(1L);
        Booking firstBooking = new Booking();
        firstBooking.setId(1L);
        Booking secondBooking = new Booking();
        secondBooking.setId(2L);
        List<Booking> expectedBookings = Lists.list(firstBooking, secondBooking);
        //WHEN
        when(repository.findAllByDateInLessThanEqualAndDateOutGreaterThanEqual(dateIn, dateIn))
                .thenReturn(Collections.singletonList(firstBooking));
        when(repository.findAllByDateInLessThanEqualAndDateOutGreaterThanEqual(dateOut, dateOut))
                .thenReturn(Collections.singletonList(secondBooking));
        Set<Booking> actualReservations = service.findAllBookingsByDates(dateIn, dateOut);
        //THEN
        assertTrue(CollectionUtils.isEqualCollection(expectedBookings, actualReservations), "the returned booking list do not match with the expected");
    }

    @Test
    void cancelBooking() {
        //GIVEN
        Booking expectedBooking = new Booking();
        //WHEN
        when(repository.findById(1L)).thenReturn(Optional.of(expectedBooking));
        service.cancelBooking(1L);
        //THEN
        verify(repository, times(1)).save(expectedBooking);
        assertEquals(expectedBooking.getStatus(), BookingStatus.CANCELED, "the booking should have status changed to CANCELED");
    }

    @Test
    void saveBooking() {
        //GIVEN
        Booking expectedBooking = new Booking();
        expectedBooking.setId(1L);
        expectedBooking.setDateIn(LocalDate.now().plusDays(1L));
        expectedBooking.setDateOut(LocalDate.now().plusDays(2L));
        //WHEN
        when(repository.findAllByDateInLessThanEqualAndDateOutGreaterThanEqual(any(), any())).thenReturn(new ArrayList<>());
        service.saveBooking(expectedBooking);
        //THEN
        verify(repository, times(1)).save(expectedBooking);
        assertEquals(expectedBooking.getStatus(), BookingStatus.APPROVED, "the booking should have status changed to APPROVED");

    }

    @Test
    void modifyBooking() {
        //GIVEN
        Booking previousBooking = new Booking();
        previousBooking.setId(1L);
        previousBooking.setDateIn(LocalDate.now().plusDays(1L));
        previousBooking.setDateOut(LocalDate.now().plusDays(2L));
        Booking expectedBooking = new Booking();
        expectedBooking.setId(1L);
        expectedBooking.setDateIn(LocalDate.now().plusDays(5L));
        expectedBooking.setDateOut(LocalDate.now().plusDays(6L));
        //WHEN
        when(repository.findAllByDateInLessThanEqualAndDateOutGreaterThanEqual(any(), any())).thenReturn(new ArrayList<>());
        when(repository.findById(1L)).thenReturn(Optional.of(previousBooking));
        service.modifyBooking(expectedBooking);
        //THEN
        verify(repository, times(1)).save(previousBooking);
        assertEquals(previousBooking.getDateIn(), expectedBooking.getDateIn(), "the date in should match the expected");
        assertEquals(previousBooking.getDateOut(), expectedBooking.getDateOut(), "the date in should match the expected");

    }
}