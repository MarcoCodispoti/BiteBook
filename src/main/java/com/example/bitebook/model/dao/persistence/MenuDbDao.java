package com.example.bitebook.model.dao.persistence;

import com.example.bitebook.exceptions.FailedDatabaseConnectionException;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.QueryException;
import com.example.bitebook.model.Dish;
import com.example.bitebook.model.Menu;
import com.example.bitebook.model.dao.MenuDao;
import com.example.bitebook.model.enums.CourseType;
import com.example.bitebook.model.enums.DietType;
import com.example.bitebook.util.Connector;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class MenuDbDao implements MenuDao {

    // Okk
    public List<Menu> getChefMenus(int chefId) throws FailedSearchException{
        List<Menu> chefMenus = new ArrayList<>();
        try(Connection conn = Connector.getInstance().getConnection();
            CallableStatement cstmt = conn.prepareCall("{call getChefMenus(?)}");){
            cstmt.setInt(1, chefId);
            cstmt.execute();
            try (ResultSet rs = cstmt.getResultSet()) {
                while (rs.next()) {
                    Menu menu = new Menu();
                    menu.setId(rs.getInt("Idmenu"));
                    menu.setName(rs.getString("Name"));
                    menu.setDietType(DietType.valueOf(rs.getString("DietType")));
                    menu.setNumberOfCourses(rs.getInt("NumberOfCourses"));
                    menu.setPricePerPerson(rs.getInt("PricePerPerson"));
                    chefMenus.add(menu);
                }
            }
        } catch (SQLException e) {
            throw new FailedSearchException("Error occurred while obtaining menus for chef whit Id:" + chefId, new QueryException(e));
        } catch (FailedDatabaseConnectionException e){
            throw new FailedSearchException(e);
        }
        return chefMenus;
    }



    public List<Dish> getMenuCourses(int menuId){
        List<Dish> courses = new ArrayList<>();
        Connection conn = null;

        try{
            conn = Connector.getInstance().getConnection();
            CallableStatement cstmt = conn.prepareCall("{call getMenuCourses(?)}");
            cstmt.setInt(1,menuId);
            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();

            while(rs.next()){
                Dish dish = new Dish();
                dish.setId(rs.getInt("IdDish"));
                dish.setName(rs.getString("Name"));
                dish.setCourseType(CourseType.valueOf(rs.getString("Type")));
                dish.setDescription(rs.getString("Description"));
                courses.add(dish);
                System.out.println("Ho trovato il piatto:" + dish.getName() + " con Id: " + dish.getId() + " " + dish.getCourseType() + " " + dish.getDescription());
            }
            // aggiunti dopo se non funziona potrebbe essere questo
            cstmt.close();
            rs.close();

        } catch (Exception e){
            e.printStackTrace();
            e.getMessage();
            e.getStackTrace();
            return null;
        }

        System.out.println("Ho trovato " + courses.size() + " portate per il menu selezionato");
        return courses;
    }

    public Menu getMenuLevelsSurcharge(int menuId) throws SQLException{
        Menu menu = new Menu();
        Connection conn = null;

        try{
            conn = Connector.getInstance().getConnection();
            CallableStatement cstmt = conn.prepareCall("{call getMenuLevelsSurcharge(?)}");
            cstmt.setInt(1,menuId);

            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();

            if(rs.next()){
                menu.setPremiumLevelSurcharge(rs.getInt("PremiumSurcharge"));
                menu.setLuxeLevelSurcharge(rs.getInt("LuxeSurcharge"));
            }else{
                throw new SQLException();
            }

            cstmt.close();
            rs.close();

        } catch (SQLException | FailedDatabaseConnectionException e){
            e.printStackTrace();
            e.getMessage();
            e.getCause();
            throw new SQLException(e);
        }
        return menu;
    }

}
