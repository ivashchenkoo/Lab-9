package com.hotel.dao;

import com.hotel.ConnectionFactory;
import com.hotel.domain.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAOImpl implements RoomDAO {

    private Room getRoomFromRS(ResultSet rs) throws SQLException {
        Room room = new Room();

        room.setId(rs.getLong("id"));
        room.setDescription(rs.getString("description"));
        room.setNumber(rs.getInt("number"));
        room.setPrice(rs.getInt("price"));
        room.setSeats(rs.getInt("seats"));

        return room;
    }

    @Override
    public Room getRoomByID(long id) {
        //try-with-resources - відкриває ресурси і закриває їх автоматично без використання finally
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM room WHERE id=?")) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return getRoomFromRS(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Room> getAllRooms() {
        try (Connection connection = ConnectionFactory.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM room"))
        // Метод executeQuery використовується для отримання даних з БД командою SELECT
        // Повертає об'єкт ResultSet, який зберігає в собі вибірку даних після запиту
        {
            List<Room> rooms = new ArrayList<>();
            while (rs.next()) {
                rooms.add(getRoomFromRS(rs));
            }
            return rooms;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Room> getRoomBySeatsAndMaxPrice(int seats, int maxPrice) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM room WHERE seats=? and price<=?")) {
            ps.setInt(1, seats);
            ps.setInt(2, maxPrice);
            ResultSet rs = ps.executeQuery();
            List<Room> rooms = new ArrayList<>();
            while (rs.next()) {
                rooms.add(getRoomFromRS(rs));
            }
            return rooms;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insertRoom(Room room) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO room(number, seats, price, description) VALUES (?, ?, ?, ?)")
        ) {
            ps.setInt(1, room.getNumber());
            ps.setInt(2, room.getSeats());
            ps.setInt(3, room.getPrice());
            ps.setString(4, room.getDescription());
            if (ps.executeUpdate() == 1)
                return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateRoom(Room room) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("UPDATE room set number = ?, seats = ?, price = ?, description = ? WHERE id=?")
        ) {
            ps.setInt(1, room.getNumber());
            ps.setInt(2, room.getSeats());
            ps.setInt(3, room.getPrice());
            ps.setString(4, room.getDescription());
            ps.setLong(5, room.getId());
            if (ps.executeUpdate() == 1)
                return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteRoom(long id) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM room WHERE id=?");
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
