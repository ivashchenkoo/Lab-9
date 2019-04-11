package com.hotel;

import com.hotel.dao.*;
import com.hotel.domain.Booking;
import com.hotel.domain.Clients;
import com.hotel.domain.Room;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        RoomDAO roomDAO = new RoomDAOImpl();
        ClientsDAO clientsDAO = new ClientsDAOImpl();
        BookingDAO bookingDAO = new BookingDAOImpl();
        Scanner sc = new Scanner(System.in);

        while (true) {
            getCommands();
            String str = sc.nextLine().toLowerCase();

            switch (str) {
                case "1": //Додати кімнату
                {
                    System.out.print("Внесіть номер кімнати: ");
                    int number = sc.nextInt();
                    System.out.print("Внесіть кількість місць: ");
                    int seats = sc.nextInt();
                    System.out.print("Внесіть ціну номера: ");
                    int price = sc.nextInt();
                    System.out.print("Внесіть додаткову інформацію (опис): ");
                    sc.nextLine();
                    String description = sc.nextLine();

                    Room room = new Room(number, seats, price, description);
                    roomDAO.insertRoom(room);
                }
                break;
                case "2": //Змінити дані про кімнату
                {
                    System.out.print("Внесіть ID номера для зміни даних: ");
                    long roomID = sc.nextLong();
                    System.out.print("Внесіть номер кімнати: ");
                    int number = sc.nextInt();
                    System.out.print("Внесіть кількість місць: ");
                    int seats = sc.nextInt();
                    System.out.print("Внесіть ціну номера: ");
                    int price = sc.nextInt();
                    System.out.print("Внесіть додаткову інформацію (опис): ");
                    sc.nextLine();
                    String description = sc.nextLine();

                    Room room = new Room(roomID, number, seats, price, description);
                    roomDAO.updateRoom(room);
                }
                break;
                case "3": //Видалити кімнату
                {
                    System.out.print("Внесіть ID номера: ");
                    long roomID = sc.nextLong();
                    roomDAO.deleteRoom(roomID);
                }
                break;
                case "4": //Список кімнат
                {
                    List<Room> rooms = roomDAO.getAllRooms();
                    for (Room room : rooms) {
                        System.out.println(room);
                    }
                }
                break;
                case "5": //Знайти за к-тю місць та ціною
                {
                    System.out.print("Внесіть кількість місць: ");
                    int seats = sc.nextInt();
                    System.out.print("Внесіть максимальну ціну номера: ");
                    int price = sc.nextInt();
                    List<Room> rooms = roomDAO.getRoomBySeatsAndMaxPrice(seats, price);
                    for (Room room : rooms) {
                        System.out.println(room);
                    }
                }
                break;
                case "6": //Додати клієнта
                {
                    System.out.print("Внесіть ім'я клієнта: ");
                    String firstName = sc.nextLine();
                    System.out.print("Внесіть по-батькові клієнта: ");
                    String middleName = sc.nextLine();
                    System.out.print("Внесіть прізвище клієнта: ");
                    String lastName = sc.nextLine();
                    System.out.print("Внесіть дату народження (yyyy-mm-dd): ");
                    String birthDateStr = sc.nextLine();
                    Date birthDate = DateUtil.convertStringIntoSqlDate(birthDateStr);
                    System.out.print("Внесіть серію паспорта: ");
                    String serialOfPassport = sc.nextLine();
                    System.out.print("Внесіть номер паспорта: ");
                    int numOfPassport = sc.nextInt();
                    System.out.print("Внесіть номер телефона ");
                    sc.nextLine();
                    String phone = sc.nextLine();

                    Clients client = new Clients(firstName, middleName, lastName, birthDate, serialOfPassport, numOfPassport, phone);
                    clientsDAO.insertClient(client);
                }
                break;
                case "7": //Змінити дані про клієнта
                {
                    System.out.print("Внесіть ID клієнта для зміни даних: ");
                    long clientID = sc.nextLong();
                    System.out.print("Внесіть ім'я клієнта: ");
                    sc.nextLine();
                    String firstName = sc.nextLine();
                    System.out.print("Внесіть по-батькові клієнта: ");
                    String middleName = sc.nextLine();
                    System.out.print("Внесіть прізвище клієнта: ");
                    String lastName = sc.nextLine();
                    System.out.print("Внесіть дату народження (yyyy-mm-dd): ");
                    String birthDateStr = sc.nextLine();
                    Date birthDate = DateUtil.convertStringIntoSqlDate(birthDateStr);
                    System.out.print("Внесіть серію паспорта: ");
                    String serialOfPassport = sc.nextLine();
                    System.out.print("Внесіть номер паспорта: ");
                    int numOfPassport = sc.nextInt();
                    System.out.print("Внесіть номер телефона ");
                    sc.nextLine();
                    String phone = sc.nextLine();

                    Clients client = new Clients(clientID, firstName, middleName, lastName, birthDate, serialOfPassport, numOfPassport, phone);
                    clientsDAO.updateClient(client);
                }
                break;
                case "8": //Видалити клієнта
                {
                    System.out.print("Внесіть ID клієнта: ");
                    long clientID = sc.nextLong();
                    clientsDAO.deleteClient(clientID);
                }
                break;
                case "9": //Список клієнтів
                {
                    List<Clients> clients = clientsDAO.getAllClients();
                    for (Clients client : clients) {
                        System.out.println(client);
                    }
                }
                break;
                case "10": //Знайти клієнта за іменем
                {
                    System.out.print("Внесіть ім'я клієнта: ");
                    String firstName = sc.nextLine();
                    System.out.print("Внесіть прізвище клієнта: ");
                    String lastName = sc.nextLine();

                    List<Clients> clients = clientsDAO.getClientByName(firstName, lastName);
                    for (Clients client : clients) {
                        System.out.println(client);
                    }
                }
                break;
                case "11": //Додати бронювання
                {
                    System.out.print("Внесіть ID номера: ");
                    long roomID = sc.nextLong();
                    System.out.print("Внесіть ID клієнта: ");
                    long clientID = sc.nextLong();
                    System.out.print("Внесіть дату заселення: ");
                    sc.nextLine();
                    String checkInDateStr = sc.nextLine();
                    Date checkInDate = DateUtil.convertStringIntoSqlDate(checkInDateStr);

                    Room room = roomDAO.getRoomByID(roomID);
                    Clients client = clientsDAO.getClientByID(clientID);

                    Booking booking = new Booking(room, client, checkInDate);
                    bookingDAO.insertBooking(booking);
                }
                break;
                case "12": //Змінити дані про бронювання
                {
                    System.out.print("Внесіть ID бронювання: ");
                    long bookingID = sc.nextLong();
                    System.out.print("Внесіть ID номера: ");
                    long roomID = sc.nextLong();
                    System.out.print("Внесіть ID клієнта: ");
                    long clientID = sc.nextLong();
                    System.out.print("Внесіть дату заселення: ");
                    sc.nextLine();
                    String checkInDateStr = sc.nextLine();
                    Date checkInDate = DateUtil.convertStringIntoSqlDate(checkInDateStr);

                    Room room = roomDAO.getRoomByID(roomID);
                    Clients client = clientsDAO.getClientByID(clientID);

                    Booking booking = new Booking(bookingID, room, client, checkInDate);
                    bookingDAO.updateBooking(booking);
                }
                break;
                case "13": //Видалити бронювання
                {
                    System.out.print("Внесіть ID бронювання: ");
                    long bookingID = sc.nextLong();
                    sc.nextLine();
                    bookingDAO.deleteBooking(bookingID);
                }
                break;
                case "14": //Список бронювань
                {
                    List<Booking> bookings = bookingDAO.getAllBookings();
                    for (Booking booking : bookings) {
                        System.out.println(booking);
                    }
                }
                break;
                case "15": //Номер звільнили
                {
                    System.out.print("Внесіть ID бронювання: ");
                    long bookingID = sc.nextLong();
                    System.out.print("Внесіть дату виселення (yyyy-MM-dd): ");
                    sc.nextLine();
                    String checkOutDateStr = sc.nextLine();
                    Date checkOutDate = DateUtil.convertStringIntoSqlDate(checkOutDateStr);

                    Booking booking = new Booking(bookingID, checkOutDate);
                    bookingDAO.updateBookingRoomIsFree(booking);
                }
                break;
                case "16": //Список зайнятих номерів
                {
                    List<Booking> bookings = bookingDAO.getBookingByAvailable(false);
                    for (Booking booking : bookings) {
                        System.out.println(booking);
                    }
                }
                break;
                case "17": //Список звільнених номерів
                {
                    List<Booking> bookings = bookingDAO.getBookingByAvailable(true);
                    for (Booking booking : bookings) {
                        System.out.println(booking);
                    }
                }
                break;
                case "end": //Завершення програми
                    return;
                case "стоп": //Завершення програми
                    return;
                default:
                    break;
            }
        }
    }

    public static void getCommands() {
        System.out.println("1: Додати кімнату                      6:  Додати клієнта                 12: Змінити дані про бронювання");
        System.out.println("2: Змінити дані про кімнату            7:  Змінити дані про клієнта       13: Видалити бронювання");
        System.out.println("3: Видалити кімнату                    8:  Видалити клієнта               14: Список бронювань");
        System.out.println("4: Список кімнат                       9:  Список клієнтів                15: Номер звільнили");
        System.out.println("5: Знайти за к-тю місць та ціною       10: Знайти клієнта за іменем       16: Список зайнятих номерів");
        System.out.println("                                       11: Додати бронювання              17: Список звільнених номерів");
        System.out.print("-> ");
    }
}
