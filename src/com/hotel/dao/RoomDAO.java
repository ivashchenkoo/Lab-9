package com.hotel.dao;

import com.hotel.domain.Room;

import java.util.List;

public interface RoomDAO {
    Room getRoomByID(long id);

    List<Room> getAllRooms();

    List<Room> getRoomBySeatsAndMaxPrice(int seats, int maxPrice);

    boolean insertRoom(Room room);

    boolean updateRoom(Room room);

    boolean deleteRoom(long id);

    Room getFirstRoom();
}
