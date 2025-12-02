package com.example.bitebook.model.dao.persistence;

import com.example.bitebook.exceptions.FailedDatabaseConnectionException;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.NoChefInCityException;
import com.example.bitebook.exceptions.QueryException;
import com.example.bitebook.model.Chef;
import com.example.bitebook.model.dao.ChefDao;
import com.example.bitebook.model.enums.CookingStyle;
import com.example.bitebook.model.enums.SpecializationType;
import com.example.bitebook.util.Connector;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChefDbDao implements ChefDao{


    // Ok -> Va bene
    public void findCityChefs(String cityName) throws FailedSearchException, NoChefInCityException {
        try(Connection conn = Connector.getInstance().getConnection();
            CallableStatement cstmt = conn.prepareCall("{call FindCityChefs(?)}")){
            cstmt.setString(1, cityName);
            cstmt.execute();

            try (ResultSet rs = cstmt.getResultSet()) {
                if (!rs.next()){
                    throw new NoChefInCityException(cityName);
                }
                // arrivati qui vuol dire che c'è uno chef -> il metodo prosegue senza eccezioni
            }
        } catch (SQLException e){
            throw new FailedSearchException("Query error while searching for chefs in " + cityName, new QueryException(e));
        } catch (FailedDatabaseConnectionException e) {
            throw new FailedSearchException(e);
        }
    }




    // Okk -> Va bene
    public List<Chef> getChefsInCity(String cityName) throws FailedSearchException {
        List<Chef> cityChefs = new ArrayList<>();

        try (Connection conn = Connector.getInstance().getConnection();
             CallableStatement cstmt = conn.prepareCall("{call getChefsInCity(?)}")) {

            cstmt.setString(1, cityName);
            cstmt.execute();
            try (ResultSet rs = cstmt.getResultSet()) {
                while (rs.next()) {
                    Chef chef = new Chef();
                    chef.setId(rs.getInt("IdChef"));
                    chef.setName(rs.getString("Name"));
                    chef.setSurname(rs.getString("Surname"));
                    chef.setCity(cityName);

                    chef.setStyle(CookingStyle.fromString(rs.getString("CookingStyle")));
                    chef.setSpecializations(convertSpecializationString(rs.getString("Specializations")));

                    cityChefs.add(chef);
                }
            }
        } catch (SQLException e) {
            throw new FailedSearchException("SQL query error occurred ", new QueryException(e));

        } catch (FailedDatabaseConnectionException e) {
            throw new FailedSearchException(e);
        }
        return cityChefs;
    }




    public List<SpecializationType> convertSpecializationString(String specializationString) {
        // 1. Gestione del caso nullo/vuoto
        if (specializationString == null || specializationString.trim().isEmpty()) {
            // Restituisce un List vuoto se non ci sono specializzazioni
            return new ArrayList<>();
        }

        // 2. Scomposizione della stringa
        // La stringa è separata da ", " come definito in GROUP_CONCAT(..., ', ')
        String[] specializationsArray = specializationString.split(",\\s*");
        // Usiamo la regex ",\\s*" per gestire ', ' e ', ' o anche solo ','

        // 3. Conversione in List<SpecializationType> usando lo Stream API
        return Arrays.stream(specializationsArray)
                .map(String::trim) // Rimuove eventuali spazi bianchi aggiuntivi
                .filter(s -> !s.isEmpty()) // Rimuove eventuali stringhe vuote dopo il trim
                .map(s -> {
                    try {
                        // Utilizza il tuo metodo fromString per la conversione
                        return SpecializationType.fromString(s);
                    } catch (IllegalArgumentException e) {
                        // Gestione di una specializzazione non valida (non mappata nell'Enum)
                        // Puoi loggare l'errore o ignorare l'elemento. Qui, lo ignoriamo (ritorna null)
                        System.err.println("Specializzazione non riconosciuta nel DB: " + s);
                        return null;
                    }
                })
                .filter(s -> s != null) // Filtra via gli elementi nulli (quelli che hanno generato l'errore)
                .collect(Collectors.toCollection(ArrayList::new)); // Colleziona il risultato in un List
    }



    public Chef getChefFromMenu(int menuId) throws SQLException{
        Chef chef = new Chef();
        Connection conn = null;

        try{
            conn = Connector.getInstance().getConnection();
            CallableStatement cstmt = conn.prepareCall("{call getChefFromMenu(?)}");
            cstmt.setInt(1,menuId);

            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();

            if(rs.next()){
                chef.setId(rs.getInt("ChefId"));
                chef.setName(rs.getString("ChefName"));
                chef.setSurname(rs.getString("ChefSurname"));
                System.out.println("ChefID: " + chef.getId() + " " + chef.getName() + " " + chef.getSurname());
            } else{
                throw new SQLException();
            }
            cstmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            e.getMessage();
            e.getCause();
            throw new SQLException();
        } catch (FailedDatabaseConnectionException e) {
            throw new RuntimeException(e);
        }
        return chef;
    }




}
