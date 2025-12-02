package com.example.bitebook.model.dao.persistence;

import com.example.bitebook.exceptions.FailedDatabaseConnectionException;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.QueryException;
import com.example.bitebook.exceptions.WrongCredentialsException;
import com.example.bitebook.model.Chef;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.dao.UserDao;
import com.example.bitebook.model.enums.CookingStyle;
import com.example.bitebook.model.enums.Role;
import com.example.bitebook.model.enums.SpecializationType;
import com.example.bitebook.util.Connector;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDbDao implements UserDao {


    @Override
    public Role getCredentialsRole(String email, String password) throws WrongCredentialsException, FailedSearchException {
        String sql = "{call GetCredentialsRole(?,?)}";
        try (Connection conn = Connector.getInstance().getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)){
            cstmt.setString(1, email);
            cstmt.setString(2, password);
            cstmt.execute();
            try (ResultSet rs = cstmt.getResultSet()) {
                if (rs.next()) {
                    try {
                        return Role.valueOf(rs.getString("UserRole"));
                    } catch (IllegalArgumentException e) {
                        throw new FailedSearchException("Ruolo non riconosciuto nel database per l'utente: " + email, e);
                    }
                } else {
                    throw new WrongCredentialsException("Nessun utente trovato con queste credenziali.");
                }
            }

        } catch (SQLException e) {
            throw new FailedSearchException("Errore DB durante verifica credenziali", new QueryException(e));
        } catch (FailedDatabaseConnectionException e) {
            throw new FailedSearchException(e);
        }
    }



    @Override
    public Chef getChefInfo(String email, String password) throws FailedSearchException {
        Chef chef = null;
        try (Connection conn = Connector.getInstance().getConnection()) {
            try (CallableStatement cstmt = conn.prepareCall("{call GetChefInfo(?,?)}")) {
                cstmt.setString(1, email);
                cstmt.setString(2, password);
                cstmt.execute();
                try (ResultSet rs = cstmt.getResultSet()) {
                    if (rs.next()) {
                        chef = new Chef(
                                rs.getInt("IdUser"),
                                rs.getString("Name"),
                                rs.getString("Surname"),
                                CookingStyle.fromString(rs.getString("CookingStyle")), // Gestione Enum
                                rs.getString("City")
                        );
                        chef.setEmail(email);
                        chef.setPassword(password);
                    }
                }
            }
            if (chef == null) {
                return null;
            }
            try (CallableStatement cstmt2 = conn.prepareCall("{call GetChefSpecializations(?)}")) {
                cstmt2.setInt(1, chef.getId()); // Qui chef è sicuro non null
                cstmt2.execute();
                List<SpecializationType> specializations = new ArrayList<>();
                try (ResultSet rs2 = cstmt2.getResultSet()) {
                    while (rs2.next()) {
                        try {
                            SpecializationType spec = SpecializationType.valueOf(rs2.getString("Specialization"));
                            specializations.add(spec);
                        } catch (IllegalArgumentException e) {
                            // Ignore
                        }
                    }
                }
                chef.setSpecializations(specializations);
            }
            chef.setOfferedMenus(null);
        } catch (SQLException e) {
            throw new FailedSearchException("Errore DB recupero profilo Chef", new QueryException(e));
        } catch (FailedDatabaseConnectionException e) {
            throw new FailedSearchException(e);
        }
        return chef;
    }



    @Override
    public Client getClientInfo(String email, String password) throws FailedSearchException {
        Client client = null;
        try (Connection conn = Connector.getInstance().getConnection();
             CallableStatement cstmt = conn.prepareCall("{call GetClientInfo(?,?)}")){
            cstmt.setString(1, email);
            cstmt.setString(2, password);
            cstmt.execute();
            try (ResultSet rs = cstmt.getResultSet()) {
                if (rs.next()) {
                    client = new Client(
                            rs.getInt("IdUser"),
                            rs.getString("Name"),
                            rs.getString("Surname")
                    );
                    client.setEmail(email);
                    client.setPassword(password);
                }
            }
        } catch (SQLException e) {
            throw new FailedSearchException("Errore DB recupero profilo Client", new QueryException(e));
        } catch (FailedDatabaseConnectionException e) {
            throw new FailedSearchException(e);
        }
        return client;
    }
}
