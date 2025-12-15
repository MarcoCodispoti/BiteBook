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
import java.util.List;

public class ChefDbDao implements ChefDao{



    public void findCityChefs(String cityName) throws FailedSearchException, NoChefInCityException {
        try(Connection conn = Connector.getInstance().getConnection();
            CallableStatement cstmt = conn.prepareCall("{call FindCityChefs(?)}")){
            cstmt.setString(1, cityName);
            cstmt.execute();

            try (ResultSet rs = cstmt.getResultSet()) {
                if (!rs.next()){
                    throw new NoChefInCityException(cityName);
                }
            }
        } catch (SQLException e){
            throw new FailedSearchException("Query error while searching for chefs in " + cityName, new QueryException(e));
        } catch (FailedDatabaseConnectionException e) {
            throw new FailedSearchException(e);
        }
    }



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

                    chef.setStyle(CookingStyle.valueOf(rs.getString("CookingStyle")));
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



    private List<SpecializationType> convertSpecializationString(String specializationString) {
        List<SpecializationType> result = new ArrayList<>();
        if (specializationString == null || specializationString.trim().isEmpty()) {
            return result;
        }
        String[] parts = specializationString.split(",\\s*");
        for (String part : parts) {
            String cleaned = part.trim();
          if (!cleaned.isEmpty()) {
                try {
                  result.add(SpecializationType.valueOf(cleaned));
              } catch (IllegalArgumentException _){
                 // ignore
             }
         }
     }
     return result;
    }



    @Override
    public Chef getChefFromMenu(int menuId) throws FailedSearchException {
        Chef chef = null;
        try (Connection conn = Connector.getInstance().getConnection();
             CallableStatement cstmt = conn.prepareCall("{call getChefFromMenu(?)}")) {
            cstmt.setInt(1, menuId);
            cstmt.execute();
            try (ResultSet rs = cstmt.getResultSet()) {
                if (rs.next()) {
                    chef = new Chef();
                    chef.setId(rs.getInt("ChefId"));
                    chef.setName(rs.getString("ChefName"));
                    chef.setSurname(rs.getString("ChefSurname"));
                }
            }
        } catch (SQLException e){
            throw new FailedSearchException("Error obtaining chef from Menu ID: " + menuId, new QueryException(e));
        } catch (FailedDatabaseConnectionException e){
            throw new FailedSearchException(e);
        }
        return chef;
    }


}
