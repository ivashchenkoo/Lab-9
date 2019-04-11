package com.hotel.dao;

import com.hotel.ConnectionFactory;
import com.hotel.domain.Booking;
import com.hotel.domain.Clients;
import com.hotel.domain.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {

    private Booking getBookingFromRS(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        RoomDAO roomDAO = new RoomDAOImpl();
        ClientsDAO clientsDAO = new ClientsDAOImpl();

        Room room = roomDAO.getRoomByID(rs.getLong("roomID"));
        Clients client = clientsDAO.getClientByID(rs.getLong("clientID"));

        booking.setId(rs.getLong("id"));
        booking.setRoom(room);
        booking.setClient(client);
        booking.setCheckInDate(rs.getDate("checkInDate"));
        booking.setCheckOutDate(rs.getDate("checkOutDate"));

        return booking;
    }


    @Override
    public Booking getBookingById(long id) {
        //try-with-resources - відкриває ресурси і закриває їх автоматично без використання finally
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM booking WHERE id=?")) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return getBookingFromRS(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Booking> getBookingByAvailable(boolean isAvailable) {
        String isAvailableStr = "not null";
        if (!isAvailable) {
            isAvailableStr = "null";
        }
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM booking WHERE checkOutDate is " + isAvailableStr)) {
            ResultSet rs = ps.executeQuery();
            List<Booking> bookings = new ArrayList<>();
            while (rs.next()) {
                bookings.add(getBookingFromRS(rs));
            }
            return bookings;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Booking> getAllBookings() {
        try (Connection connection = ConnectionFactory.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM booking"))
        // Метод executeQuery використовується для отримання даних з БД командою SELECT
        // Повертає об'єкт ResultSet, який зберігає в собі вибірку даних після запиту
        {
            List<Booking> booking = new ArrayList<>();
            while (rs.next()) {
                booking.add(getBookingFromRS(rs));
            }
            return booking;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insertBooking(Booking booking) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO booking(roomId, clientId, checkInDate, checkOutDate) VALUES (?, ?, ?, ?)")
        ) {
            ps.setLong(1, booking.getRoom().getId());
            ps.setLong(2, booking.getClient().getId());
            ps.setDate(3, (Date) booking.getCheckInDate());
            ps.setDate(4, (Date) booking.getCheckOutDate());
            if (ps.executeUpdate() == 1)
                return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateBooking(Booking booking) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("UPDATE booking set roomId = ?, clientId = ?, checkInDate = ?, checkOutDate = ? WHERE id=?")
        ) {
            ps.setLong(1, booking.getRoom().getId());
            ps.setLong(2, booking.getClient().getId());
            ps.setDate(3, (Date) booking.getCheckInDate());
            ps.setDate(4, (Date) booking.getCheckOutDate());
            ps.setLong(5, booking.getId());
            if (ps.executeUpdate() == 1)
                return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateBookingRoomIsFree(Booking booking) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("UPDATE booking set checkOutDate = ? WHERE id=?")
        ) {
            ps.setDate(1, (Date) booking.getCheckOutDate());
            ps.setLong(2, booking.getId());
            if (ps.executeUpdate() == 1)
                return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteBooking(long id) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM booking WHERE id=?");
        ) {
            ps.setLong(1, id);
            if (ps.executeUpdate() == 1)
                return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
