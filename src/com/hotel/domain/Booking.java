package com.hotel.domain;

import java.util.Date;

/*CREATE TABLE hotel.Booking (ID bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, roomID bigint NOT NULL, clientID bigint NOT NULL, checkInDate date NOT NULL, checkOutDate date,
  CONSTRAINT `room` FOREIGN KEY (`ROOMID`) REFERENCES `hotel`.`Room` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `client` FOREIGN KEY (`CLIENTID`) REFERENCES `hotel`.`Clients` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION);*/

public class Booking {
    // Оголошення змінних для збереження даних про бронювання
    private long id;

    private Room room;
    private Clients client;
    private Date checkInDate;
    private Date checkOutDate;

    // Конструктори
    public Booking() {
        super();
    }

    public Booking(long id, Room room, Clients client, Date checkInDate, Date checkOutDate) {
        this.id = id;
        this.room = room;
        this.client = client;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Booking(Room room, Clients client, Date checkInDate, Date checkOutDate) {
        this.room = room;
        this.client = client;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Booking(long id, Room room, Clients client, Date checkInDate) {
        this.id = id;
        this.room = room;
        this.client = client;
        this.checkInDate = checkInDate;
    }

    public Booking(Room room, Clients client, Date checkInDate) {
        this.room = room;
        this.client = client;
        this.checkInDate = checkInDate;
    }

    public Booking(long id, Date checkOutDate) {
        this.id = id;
        this.checkOutDate = checkOutDate;
    }

    // Геттери і сеттери

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Clients getClient() {
        return client;
    }

    public void setClient(Clients client) {
        this.client = client;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    @Override
    public String toString() {
        return "Booking[" +
                "id=" + id +
                ", room=" + room +
                ", client=" + client +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ']';
    }
}