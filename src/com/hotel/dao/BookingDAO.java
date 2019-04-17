package com.hotel.dao;

import com.hotel.domain.Booking;

import java.util.List;

public interface BookingDAO {
    Booking getBookingById(long id);

    List<Booking> getBookingByAvailable(boolean isAvailable);

    List<Booking> getAllBookings();

    boolean insertBooking(Booking booking);

    boolean updateBooking(Booking booking);

    boolean updateBookingRoomIsFree(Booking booking);

    boolean deleteBooking(long id);

    Booking getFirstBooking();
}
