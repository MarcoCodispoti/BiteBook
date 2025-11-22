package com.example.bitebook.model.dao.persistence;

import com.example.bitebook.model.Dish;
import com.example.bitebook.model.Menu;
import com.example.bitebook.model.dao.DishDao;
import com.example.bitebook.model.dao.MenuDao;
import com.example.bitebook.model.enums.CourseType;
import com.example.bitebook.model.enums.DietType;
import com.example.bitebook.util.Connector;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class MenuDbDao implements MenuDao {

    // da spostare questo metodo in ChefDao?
    public Vector<Menu> getChefMenus(int chefId) throws SQLException{
        Vector<Menu> chefMenus = new Vector<>();
        Connection conn = null;

        try{
            conn = Connector.getInstance().getConnection();
            CallableStatement cstmt = conn.prepareCall("{call getChefMenus(?)}");
            cstmt.setInt(1, chefId);

            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();

            while(rs.next()){
                Menu menu = new Menu();
                menu.setId(rs.getInt("Idmenu"));
                menu.setName(rs.getString("Name"));
                menu.setDietType(DietType.valueOf(rs.getString("DietType")));
                menu.setNumberOfCourses(rs.getInt("NumberOfCourses"));
                menu.setPricePerPerson(rs.getInt("PricePerPerson"));
                chefMenus.add(menu);
                System.out.println("Ho trovato il menu " + menu.getId() + " " + menu.getName() + " " + menu.getDietType() + " " + menu.getNumberOfCourses() + " " + menu.getPricePerPerson());
            }
            cstmt.close();  rs.close();

        } catch (Exception e){
            e.printStackTrace();
            e.getMessage();
            e.getStackTrace();
            throw new SQLException(e);
        }
        System.out.println("Ho trovato " + chefMenus.size() + " menu per lo chef selezionato");
        return chefMenus;
    }

    public Vector<Dish> getMenuCourses(int menuId){
        Vector<Dish> courses = new Vector<>();
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

        } catch (SQLException e){
            e.printStackTrace();
            e.getMessage();
            e.getCause();
            throw new SQLException(e);
        }
        return menu;
    }

}
