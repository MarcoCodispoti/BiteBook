package com.example.bitebook.model.dao.persistence;

import com.example.bitebook.exceptions.*;
import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.dao.AllergenDao;
import com.example.bitebook.util.Connector;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


// pulita

public class AllergenDbDao implements AllergenDao{

    @Override
    public List<Allergen> getClientAllergies(Client client) throws FailedSearchException{
        List<Allergen> allergies = new ArrayList<>();

        try (Connection conn = Connector.getInstance().getConnection();
             CallableStatement cstmt = conn.prepareCall("{call GetClientAllergies(?)}")) {

            cstmt.setInt(1, client.getId());
            cstmt.execute();

            try (ResultSet rs = cstmt.getResultSet()) {
                while (rs.next()) {
                    Allergen allergy = new Allergen(
                            rs.getInt("AllergenId"),
                            rs.getString("AllergenName")
                    );
                    allergies.add(allergy);
                }
            }

        } catch (SQLException e){
            throw new FailedSearchException("SQL query error", new QueryException(e));
        } catch (FailedDatabaseConnectionException e) {
            throw new FailedSearchException(e);
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


    // Ok
    public List<Allergen>  getAllergens() throws FailedSearchException {
        List<Allergen> allergens = new ArrayList<>();

        try(Connection conn = Connector.getInstance().getConnection();
            CallableStatement cstmt = conn.prepareCall("{call getAllergens()}")){
            cstmt.execute();

            try(ResultSet rs = cstmt.getResultSet()) {
                while (rs.next()) {
                    Allergen allergen = new Allergen();
                    allergen.setId(rs.getInt("IdAllergen"));
                    allergen.setName(rs.getString("Name"));
                    allergens.add(allergen);
                }
            }
        } catch(SQLException e) {
            throw new FailedSearchException(new QueryException(e));
        } catch (FailedDatabaseConnectionException e){
            throw new FailedSearchException(e);
        }
        return allergens;
    }


    // Ok -> Gestisce bene tutto
    public void insertAllergy(Allergen allergen, int clientId) throws FailedInsertException{
        try(Connection conn = Connector.getInstance().getConnection();
            CallableStatement cstmt = conn.prepareCall("{call insertAllergy(?,?)}")){
            cstmt.setInt(1, clientId);
            cstmt.setInt(2, allergen.getId());
            cstmt.execute();

        }catch (SQLException e){
            throw new FailedInsertException("SQL insert error", new QueryException(e));

        } catch (FailedDatabaseConnectionException e){
            throw new FailedInsertException(e);
        }
    }
}
