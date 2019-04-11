package com.hotel.dao;

import com.hotel.ConnectionFactory;
import com.hotel.domain.Clients;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientsDAOImpl implements ClientsDAO {

    private Clients getClientFromRS(ResultSet rs) throws SQLException {
        Clients client = new Clients();

        client.setId(rs.getLong("id"));
        client.setFirstName(rs.getString("firstName"));
        client.setMiddleName(rs.getString("middleName"));
        client.setLastName(rs.getString("lastName"));
        client.setBirthDate(rs.getDate("birthDt"));
        client.setSerialOfPassport(rs.getString("serialOfPassport"));
        client.setNumOfPassport(rs.getInt("numOfPassport"));
        client.setPhone(rs.getString("phone"));

        return client;
    }

    @Override
    public Clients getClientByID(long id) {
        //try-with-resources - відкриває ресурси і закриває їх автоматично без використання finally
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM clients WHERE id=?")) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return getClientFromRS(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Clients> getAllClients() {
        try (Connection connection = ConnectionFactory.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM clients"))
        // Метод executeQuery використовується для отримання даних з БД командою SELECT
        // Повертає об'єкт ResultSet, який зберігає в собі вибірку даних після запиту
        {
            List<Clients> clients = new ArrayList<>();
            while (rs.next()) {
                clients.add(getClientFromRS(rs));
            }
            return clients;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Clients> getClientByName(String firstName, String lastName) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM clients WHERE firstName=? and lastName=?")) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ResultSet rs = ps.executeQuery();
            List<Clients> clients = new ArrayList<>();
            while (rs.next()) {
                clients.add(getClientFromRS(rs));
            }
            return clients;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insertClient(Clients client) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO clients(firstName, middleName, lastName, birthDt, serialOfPassport, numOfPassport, phone) VALUES (?, ?, ?, ?, ?, ?, ?)")
        ) {
            ps.setString(1, client.getFirstName());
            ps.setString(2, client.getMiddleName());
            ps.setString(3, client.getLastName());
            ps.setDate(4, client.getBirthDate());
            ps.setString(5, client.getSerialOfPassport());
            ps.setInt(6, client.getNumOfPassport());
            ps.setString(7, client.getPhone());
            if (ps.executeUpdate() == 1)
                return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateClient(Clients client) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("UPDATE clients set firstName = ?, middleName = ?, lastName = ?, birthDt = ?, serialOfPassport = ?, numOfPassport = ?, phone = ? WHERE id=?")
        ) {
            ps.setString(1, client.getFirstName());
            ps.setString(2, client.getMiddleName());
            ps.setString(3, client.getLastName());
            ps.setDate(4, client.getBirthDate());
            ps.setString(5, client.getSerialOfPassport());
            ps.setInt(6, client.getNumOfPassport());
            ps.setString(7, client.getPhone());
            ps.setLong(8, client.getId());
            if (ps.executeUpdate() == 1)
                return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteClient(long id) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM clients WHERE id=?");
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
