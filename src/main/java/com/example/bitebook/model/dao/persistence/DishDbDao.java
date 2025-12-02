package com.example.bitebook.model.dao.persistence;

import com.example.bitebook.exceptions.FailedDatabaseConnectionException;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.QueryException;
import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.dao.DishDao;
import com.example.bitebook.util.Connector;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class DishDbDao implements DishDao{

    //  Okk -> Va bene
    public List<Allergen> getDishAllergens(int dishId) throws FailedSearchException {
        List<Allergen> allergens = new ArrayList<>();

        try (Connection conn = Connector.getInstance().getConnection();
             CallableStatement cstmt = conn.prepareCall("{call getDishAllergens(?)}")) {

            cstmt.setInt(1, dishId);
            cstmt.execute();
            // Da gestire questaa duplicazione

            try (ResultSet rs = cstmt.getResultSet()) {
                while (rs.next()) {
                    Allergen allergen = new Allergen();
                    allergen.setId(rs.getInt("IdAllergen"));
                    allergen.setName(rs.getString("Name"));
                    allergens.add(allergen);
                }
            }
        } catch (SQLException e){
            throw new FailedSearchException("SQL query error while tryingo to acquire dish allergens", new QueryException(e));
        } catch (FailedDatabaseConnectionException e) {
            throw new FailedSearchException(e);
        }

        return allergens;
    }


}
