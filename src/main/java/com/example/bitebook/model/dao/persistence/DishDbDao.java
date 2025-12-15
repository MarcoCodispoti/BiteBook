package com.example.bitebook.model.dao.persistence;

import com.example.bitebook.exceptions.FailedDatabaseConnectionException;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.QueryException;
import com.example.bitebook.model.Dish;
import com.example.bitebook.model.dao.DishDao;
import com.example.bitebook.model.enums.CourseType;
import com.example.bitebook.util.Connector;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class DishDbDao implements DishDao{



    public List<Dish> getMenuCourses(int menuId) throws FailedSearchException {
        List<Dish> courses = new ArrayList<>();
        try (Connection conn = Connector.getInstance().getConnection();
             CallableStatement cstmt = conn.prepareCall("{call getMenuCourses(?)}")) {
            cstmt.setInt(1, menuId);
            cstmt.execute();
            try (ResultSet rs = cstmt.getResultSet()) {
                while (rs.next()) {
                    Dish dish = new Dish();
                    dish.setId(rs.getInt("IdDish"));
                    dish.setName(rs.getString("Name"));
                    dish.setCourseType(CourseType.valueOf(rs.getString("Type")));
                    dish.setDescription(rs.getString("Description"));
                    courses.add(dish);
                }
            }
        } catch (SQLException e){
            throw new FailedSearchException("Error while obtaining courses of menu ID:" + menuId, new QueryException(e));
        } catch (FailedDatabaseConnectionException e) {
            throw new FailedSearchException(e);
        }
        return courses;
    }


}
