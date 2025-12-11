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


public class AllergenDbDao implements AllergenDao{

//    @Override
//    public List<Allergen> getClientAllergies(Client client) throws FailedSearchException{
//        List<Allergen> allergies = new ArrayList<>();
//
//        try (Connection conn = Connector.getInstance().getConnection();
//             CallableStatement cstmt = conn.prepareCall("{call GetClientAllergies(?)}")) {
//
//            cstmt.setInt(1, client.getId());
//            cstmt.execute();
//
//            try (ResultSet rs = cstmt.getResultSet()) {
//                while (rs.next()) {
//                    Allergen allergy = new Allergen(
//                            rs.getInt("AllergenId"),
//                            rs.getString("AllergenName")
//                    );
//                    allergies.add(allergy);
//                }
//            }
//
//        } catch (SQLException e){
//            throw new FailedSearchException("SQL query error", new QueryException(e));
//        } catch (FailedDatabaseConnectionException e) {
//            throw new FailedSearchException(e);
//        }
//        return allergies;
//    }
//
//    // Ok
//    public void removeClientAllergy(int clientId, int allergenId) throws FailedRemoveException {
//        try(Connection conn = Connector.getInstance().getConnection();
//            CallableStatement cstmt = conn.prepareCall("{call removeClientAllergy(?,?)}")){
//            cstmt.setInt(1, clientId);
//            cstmt.setInt(2, allergenId);
//            cstmt.execute();
//        } catch (SQLException e){
//            throw new FailedRemoveException(new QueryException(e));
//        } catch (FailedDatabaseConnectionException e){
//            throw new FailedRemoveException("Error occurred while trying to remove a client allergy",e);
//        }
//    }
//
//
//    // Ok
//    public List<Allergen>  getAllergens() throws FailedSearchException {
//        List<Allergen> allergens = new ArrayList<>();
//
//        try(Connection conn = Connector.getInstance().getConnection();
//            CallableStatement cstmt = conn.prepareCall("{call getAllergens()}")){
//            getResultAllergens(allergens, cstmt);
//        } catch(SQLException e) {
//            throw new FailedSearchException(new QueryException(e));
//        } catch (FailedDatabaseConnectionException e){
//            throw new FailedSearchException(e);
//        }
//        return allergens;
//    }
//
//
//    // Ok -> Gestisce bene tutto
//    public void insertAllergy(Allergen allergen, int clientId) throws FailedInsertException{
//        try(Connection conn = Connector.getInstance().getConnection();
//            CallableStatement cstmt = conn.prepareCall("{call insertAllergy(?,?)}")){
//            cstmt.setInt(1, clientId);
//            cstmt.setInt(2, allergen.getId());
//            cstmt.execute();
//
//        }catch (SQLException e){
//            throw new FailedInsertException("SQL insert error", new QueryException(e));
//
//        } catch (FailedDatabaseConnectionException e){
//            throw new FailedInsertException(e);
//        }
//    }
//
//
//
//    // Okk -> Preso da dishDao
//    public List<Allergen> getDishAllergens(int dishId) throws FailedSearchException {
//        List<Allergen> allergens = new ArrayList<>();
//        try (Connection conn = Connector.getInstance().getConnection();
//             CallableStatement cstmt = conn.prepareCall("{call getDishAllergens(?)}")) {
//            cstmt.setInt(1, dishId);
//            getResultAllergens(allergens, cstmt);
//        } catch (SQLException e){
//            throw new FailedSearchException("SQL query error while trying to acquire dish allergens", new QueryException(e));
//        } catch (FailedDatabaseConnectionException e) {
//            throw new FailedSearchException(e);
//        }
//
//        return allergens;
//    }
//
//
//    // metodo helper
//    private void getResultAllergens(List<Allergen> allergens, CallableStatement cstmt) throws SQLException {
//        cstmt.execute();
//        try (ResultSet rs = cstmt.getResultSet()) {
//            while (rs.next()) {
//                Allergen allergen = new Allergen();
//                allergen.setId(rs.getInt("IdAllergen"));
//                allergen.setName(rs.getString("Name"));
//                allergens.add(allergen);
//            }
//        }
//    }


    @Override
    public List<Allergen> getClientAllergies(Client client) throws FailedSearchException {
        // Riutilizziamo il metodo helper passando i nomi delle colonne specifici
        return executeQuery(
                "{call GetClientAllergies(?)}",
                cstmt -> cstmt.setInt(1, client.getId()), // Lambda per settare i parametri
                "AllergenId", "AllergenName" // Nomi colonne
        );
    }

    @Override
    public void removeClientAllergy(int clientId, int allergenId) throws FailedRemoveException {
        try {
            executeVoid(
                    "{call removeClientAllergy(?,?)}",
                    cstmt -> {
                        cstmt.setInt(1, clientId);
                        cstmt.setInt(2, allergenId);
                    }
            );
        } catch (Exception e) {
            // Traduzione dell'eccezione generica in quella specifica del metodo
            handleExceptionForRemove(e);
        }
    }

    @Override
    public List<Allergen> getAllergens() throws FailedSearchException {
        return executeQuery(
                "{call getAllergens()}",
                cstmt -> {}, // Nessun parametro da settare
                "IdAllergen", "Name"
        );
    }

    @Override
    public void insertAllergy(Allergen allergen, int clientId) throws FailedInsertException {
        try {
            executeVoid(
                    "{call insertAllergy(?,?)}",
                    cstmt -> {
                        cstmt.setInt(1, clientId);
                        cstmt.setInt(2, allergen.getId());
                    }
            );
        } catch (Exception e) {
            handleExceptionForInsert(e);
        }
    }

    @Override
    public List<Allergen> getDishAllergens(int dishId) throws FailedSearchException {
        return executeQuery(
                "{call getDishAllergens(?)}",
                cstmt -> cstmt.setInt(1, dishId),
                "IdAllergen", "Name"
        );
    }

    // ---------------------------------------------------------
    // HELPER PRIVATI (Pure Fabrication interna per rimuovere duplicati)
    // ---------------------------------------------------------

    @FunctionalInterface
    private interface StatementPreparer {
        void prepare(CallableStatement cstmt) throws SQLException;
    }

    // Helper per le Query (Lettura)
    private List<Allergen> executeQuery(String sql, StatementPreparer preparer, String idCol, String nameCol) throws FailedSearchException {
        List<Allergen> list = new ArrayList<>();
        try (Connection conn = Connector.getInstance().getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            preparer.prepare(cstmt); // Setta i parametri (es. ID)
            cstmt.execute();

            try (ResultSet rs = cstmt.getResultSet()) {
                while (rs.next()) {
                    list.add(new Allergen(rs.getInt(idCol), rs.getString(nameCol)));
                }
            }

        } catch (SQLException e) {
            throw new FailedSearchException("SQL query error", new QueryException(e));
        } catch (FailedDatabaseConnectionException e) {
            throw new FailedSearchException(e);
        }
        return list;
    }

    // Helper per Insert/Delete (Void)
    private void executeVoid(String sql, StatementPreparer preparer) throws Exception {
        try (Connection conn = Connector.getInstance().getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            preparer.prepare(cstmt);
            cstmt.execute();
        } catch (SQLException e) {
            throw new QueryException(e);
        }
        // FailedDatabaseConnectionException sale automaticamente
    }

    // Gestori eccezioni specifici per evitare try-catch giganti nei metodi void
    private void handleExceptionForRemove(Exception e) throws FailedRemoveException {
        if (e instanceof QueryException) throw new FailedRemoveException((QueryException) e);
        if (e instanceof FailedDatabaseConnectionException) throw new FailedRemoveException("DB Connection Error", e);
        throw new FailedRemoveException("Unknown error", e);
    }

    private void handleExceptionForInsert(Exception e) throws FailedInsertException {
        if (e instanceof QueryException) throw new FailedInsertException("SQL insert error", (QueryException) e);
        if (e instanceof FailedDatabaseConnectionException) throw new FailedInsertException(e);
        throw new FailedInsertException("Unknown error", e);
    }


}
