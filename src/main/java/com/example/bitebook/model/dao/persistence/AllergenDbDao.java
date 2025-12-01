package com.example.bitebook.model.dao.persistence;

import com.example.bitebook.exceptions.FailedDatabaseConnectionException;
import com.example.bitebook.exceptions.FailedRemoveException;
import com.example.bitebook.exceptions.QueryException;
import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.dao.AllergenDao;
import com.example.bitebook.util.Connector;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class AllergenDbDao implements AllergenDao{

    @Override
    public Vector<Allergen> getClientAllergies(Client client) throws Exception {
        // to be implemented

        Vector<Allergen> allergies = new Vector<>();
        Connection conn;

        try {
            System.out.println("Preparo la seconda prepared call e l'id è: " + client.getId());
            conn = Connector.getInstance().getConnection();
            CallableStatement cstmt = conn.prepareCall("{call GetClientAllergies(?)}");
            cstmt.setInt(1, client.getId());
            System.out.println("Chiamo la seconda query");
            cstmt.execute();
            System.out.println("Ho eseguito la seconda call a query");
            ResultSet rs = cstmt.getResultSet();


            while (rs.next()) {
                Allergen allergy = new Allergen(rs.getInt("AllergenId"), rs.getString("AllergenName"));
                System.out.println("Found allergy: " + rs.getString("AllergenName"));
                allergies.add(allergy);
                System.out.println("Le allergie del client sono: " + allergies);
            }
            rs.close();
            cstmt.close();
            // client.setAllergies(allergies);

        } catch (SQLException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
            throw new Exception();
        }
        return allergies;
    }

    // Ok
    public void removeClientAllergy(int clientId, int allergenId) throws FailedRemoveException {
        try(Connection conn = Connector.getInstance().getConnection();
            CallableStatement cstmt = conn.prepareCall("{call removeClientAllergy(?,?)}")){
            cstmt.setInt(1, clientId);
            cstmt.setInt(2, allergenId);
            cstmt.execute();
        } catch (SQLException e){
            throw new FailedRemoveException(new QueryException(e));
        } catch (FailedDatabaseConnectionException e){
            throw new FailedRemoveException(e);
        }
    }

    public Vector<Allergen>  getAllergens() throws SQLException {
        Vector<Allergen> allergens = new Vector<>();
        Connection conn;

        try{
            conn = Connector.getInstance().getConnection();
            CallableStatement cstmt = conn.prepareCall("{call getAllergens()}");
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();

            while(rs.next()){
                Allergen allergen = new Allergen();
                allergen.setId(rs.getInt("IdAllergen"));
                allergen.setName(rs.getString("Name"));
                allergens.add(allergen);
            }

            cstmt.close();
            rs.close();
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            e.getCause();
            throw new SQLException();
        }
        return allergens;
    }


    public void insertAllergy(Allergen allergen, int clientId) throws SQLException {
        Connection conn;
        try{
            conn = Connector.getInstance().getConnection();
            CallableStatement cstmt = conn.prepareCall("{call insertAllergy(?,?)}");
            cstmt.setInt(1, clientId);
            cstmt.setInt(2, allergen.getId());

            cstmt.execute();
            cstmt.close();
        }catch(Exception e){
            e.getMessage();
            e.printStackTrace();
            e.getCause();
            throw new SQLException();
        }
    }
}
