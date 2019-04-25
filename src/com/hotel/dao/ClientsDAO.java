package com.hotel.dao;

import com.hotel.domain.Clients;
import com.mysql.cj.xdevapi.Client;

import java.util.List;

public interface ClientsDAO {
    Clients getClientByID(long id);

    List<Clients> getAllClients();

    List<Clients> getClientByName(String firstName, String lastName);

    boolean insertClient(Clients client);

    boolean updateClient(Clients client);

    boolean deleteClient(long id);

    Clients getFirstClient();
}
