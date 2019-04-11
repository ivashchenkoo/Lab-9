package com.hotel.domain;

import java.sql.Date;

/*CREATE TABLE hotel.Clients(ID bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, firstName varchar (255) NOT NULL, middleName varchar (255),
lastName varchar (255) NOT NULL, birthDt date,serialOfPassport varchar (30), numOfPassport int (30), phone varchar (255) NOT NULL);*/

public class Clients {
    // Оголошення змінних для збереження даних про клієнта
    private long id;

    private String firstName;
    private String middleName;
    private String lastName;
    private Date birthDt;
    private String serialOfPassport;
    private int numOfPassport;
    private String phone;

    // Конструктори
    public Clients() {
        super();
    }

    public Clients(String firstName, String middleName, String lastName, Date birthDt, String serialOfPassport, int numOfPassport, String phone) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.birthDt = birthDt;
        this.serialOfPassport = serialOfPassport;
        this.numOfPassport = numOfPassport;
        this.phone = phone;
    }

    public Clients(long id, String firstName, String middleName, String lastName, Date birthDt, String serialOfPassport, int numOfPassport, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.birthDt = birthDt;
        this.serialOfPassport = serialOfPassport;
        this.numOfPassport = numOfPassport;
        this.phone = phone;
    }

    // Геттери і сеттери
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDt;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDt = birthDate;
    }

    public String getSerialOfPassport() {
        return serialOfPassport;
    }

    public void setSerialOfPassport(String serialOfPassport) {
        this.serialOfPassport = serialOfPassport;
    }

    public int getNumOfPassport() {
        return numOfPassport;
    }

    public void setNumOfPassport(int numOfPassport) {
        this.numOfPassport = numOfPassport;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Clients[" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDt +
                ", serialOfPassport='" + serialOfPassport + '\'' +
                ", numOfPassport=" + numOfPassport +
                ", phone='" + phone + '\'' +
                ']';
    }
}
