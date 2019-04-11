package com.hotel.dao;

import com.hotel.domain.Clients;

import java.util.List;

public interface ClientsDAO {
    Clients getClientByID(long id);

    List<Clients> getAllClients();

    List<Clients> getClientByName(String firstName, String lasName);

    boolean insertClient(Clients client);

    boolean updateClient(Clients client);

    boolean deleteClient(long id);
}
