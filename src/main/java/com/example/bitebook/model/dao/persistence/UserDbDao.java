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
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, email);
            cstmt.setString(2, password);
            cstmt.execute();

            try (ResultSet rs = cstmt.getResultSet()){
                return extractRoleFromResultSet(rs);
            }

        } catch (SQLException e) {
            throw new FailedSearchException("DB Error while verifying credentials", new QueryException(e));
        } catch (FailedDatabaseConnectionException e){
            throw new FailedSearchException(e);
        }
    }




    private Role extractRoleFromResultSet(ResultSet rs) throws SQLException, WrongCredentialsException, FailedSearchException {
        if (rs.next()) {
            String roleString = rs.getString("UserRole");
            return parseRole(roleString);
        } else {
            throw new WrongCredentialsException("No user found with such credentials");
        }
    }


    private Role parseRole(String roleName) throws FailedSearchException {
        try {
            return Role.valueOf(roleName);
        } catch (IllegalArgumentException | NullPointerException e){
            throw new FailedSearchException("Role not recognized or corrupted ", e);
        }
    }




//    @Override
//    public Chef getChefInfo(String email, String password) throws FailedSearchException {
//        Chef chef = null;
//        try (Connection conn = Connector.getInstance().getConnection()) {
//            try (CallableStatement cstmt = conn.prepareCall("{call GetChefInfo(?,?)}")) {
//                cstmt.setString(1, email);
//                cstmt.setString(2, password);
//                cstmt.execute();
//                try (ResultSet rs = cstmt.getResultSet()) {
//                    if (rs.next()) {
//                        chef = new Chef(
//                                rs.getInt("IdUser"),
//                                rs.getString("Name"),
//                                rs.getString("Surname"),
//                                CookingStyle.valueOf(rs.getString("CookingStyle")), // Gestione Enum
//                                rs.getString("City")
//                        );
//                        chef.setEmail(email);
//                        chef.setPassword(password);
//                    }
//                }
//            }
//            if (chef == null) {
//                return null;
//            }
//            try (CallableStatement cstmt2 = conn.prepareCall("{call GetChefSpecializations(?)}")) {
//                cstmt2.setInt(1, chef.getId());
//                cstmt2.execute();
//                List<SpecializationType> specializations = new ArrayList<>();
//                try (ResultSet rs2 = cstmt2.getResultSet()) {
//                    while (rs2.next()) {
//                        try {
//                            SpecializationType spec = SpecializationType.valueOf(rs2.getString("Specialization"));
//                            specializations.add(spec);
//                        } catch (IllegalArgumentException _) {
//                            // Ignore
//                        }
//                    }
//                }
//                chef.setSpecializations(specializations);
//            }
//            chef.setOfferedMenus(null);
//        } catch (SQLException e) {
//            throw new FailedSearchException("DB Error while recovering chef profile", new QueryException(e));
//        } catch (FailedDatabaseConnectionException e) {
//            throw new FailedSearchException(e);
//        }
//        return chef;
//    }


    @Override
    public Chef getChefInfo(String email, String password) throws FailedSearchException {
        try (Connection conn = Connector.getInstance().getConnection()){
            Chef chef = fetchChefBasicData(conn, email, password);
            if (chef != null) {
                List<SpecializationType> specs = fetchChefSpecializations(conn, chef.getId());
                chef.setSpecializations(specs);
                chef.setOfferedMenus(null);
            }

            return chef;

        } catch (SQLException e) {
            throw new FailedSearchException("DB Error while searching chef info", new QueryException(e));
        } catch (FailedDatabaseConnectionException e) {
            throw new FailedSearchException(e);
        }
    }


    private Chef fetchChefBasicData(Connection conn, String email, String password) throws SQLException {
        String sql = "{call GetChefInfo(?,?)}";

        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setString(1, email);
            cstmt.setString(2, password);
            cstmt.execute();

            try (ResultSet rs = cstmt.getResultSet()) {
                if (rs.next()){
                    CookingStyle style = parseCookingStyle(rs.getString("CookingStyle"));

                    Chef chef = new Chef(
                            rs.getInt("IdUser"),
                            rs.getString("Name"),
                            rs.getString("Surname"),
                            style,
                            rs.getString("City")
                    );
                    chef.setEmail(email);
                    chef.setPassword(password);
                    return chef;
                }
            }
        }
        return null;
    }





    private List<SpecializationType> fetchChefSpecializations(Connection conn, int chefId) throws SQLException {
        List<SpecializationType> specializations = new ArrayList<>();
        String sql = "{call GetChefSpecializations(?)}";
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setInt(1, chefId);
            cstmt.execute();
            try (ResultSet rs = cstmt.getResultSet()) {
                while (rs.next()) {
                    try {
                        SpecializationType spec = SpecializationType.valueOf(rs.getString("Specialization"));
                        specializations.add(spec);
                    } catch (IllegalArgumentException | NullPointerException _) {
                        // Ignore
                    }
                }
            }
        }
        return specializations;
    }



    private CookingStyle parseCookingStyle(String styleName) {
        try {
            return CookingStyle.valueOf(styleName);
        } catch (IllegalArgumentException | NullPointerException e){
            return null;
        }
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
            throw new FailedSearchException("DB Error while recovering client profile", new QueryException(e));
        } catch (FailedDatabaseConnectionException e) {
            throw new FailedSearchException(e);
        }
        return client;
    }
}
