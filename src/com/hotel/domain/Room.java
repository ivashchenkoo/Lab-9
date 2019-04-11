package com.hotel.domain;

/*CREATE TABLE hotel.Room(ID bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, number int (30), seats int (30), price int (30), description varchar(255) NOT NULL);*/

public class Room {
    // Оголошення змінних для збереження даних про номер
    private long id;

    private int number;
    private int seats;
    private int price;
    private String description;

    // Конструктори
    public Room() {
        super();
    }

    public Room(long id, int number, int seats, int price, String description) {
        this.id = id;
        this.number = number;
        this.seats = seats;
        this.price = price;
        this.description = description;
    }

    public Room(int number, int seats, int price, String description) {
        this.number = number;
        this.seats = seats;
        this.price = price;
        this.description = description;
    }

    // Геттери і сеттери для полів класу
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Room[" +
                "id=" + id +
                ", number=" + number +
                ", seats=" + seats +
                ", price=" + price +
                ", description='" + description + '\'' +
                ']';
    }
}
