package com.example.bitebook.model.dao.persistence;

import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.dao.DishDao;
import com.example.bitebook.util.Connector;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class DishDbDao implements DishDao{

    public Vector<Allergen> getDishAllergens(int dishId) throws SQLException {
        Vector<Allergen> allergens = new Vector<>();
        Connection conn = null;

        try{
            conn = Connector.getInstance().getConnection();
            CallableStatement cstmt = conn.prepareCall("{call getDishAllergens(?)}");
            cstmt.setInt(1, dishId);

            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();

            while(rs.next()){
                Allergen allergen = new Allergen();
                allergen.setId(rs.getInt("IdAllergen"));
                allergen.setName(rs.getString("Name"));
                allergens.add(allergen);
            }
            cstmt.close(); rs.close();

        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            e.getStackTrace();
            throw new SQLException(e);
        }
        return allergens;
    }
}
