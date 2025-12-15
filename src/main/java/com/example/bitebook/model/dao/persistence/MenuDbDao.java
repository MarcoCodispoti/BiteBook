package com.example.bitebook.model.dao.persistence;

import com.example.bitebook.exceptions.FailedDatabaseConnectionException;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.exceptions.QueryException;
import com.example.bitebook.model.Menu;
import com.example.bitebook.model.dao.MenuDao;
import com.example.bitebook.model.enums.DietType;
import com.example.bitebook.util.Connector;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class MenuDbDao implements MenuDao {



    public List<Menu> getChefMenus(int chefId) throws FailedSearchException{
        List<Menu> chefMenus = new ArrayList<>();
        try(Connection conn = Connector.getInstance().getConnection();
            CallableStatement cstmt = conn.prepareCall("{call getChefMenus(?)}")){
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



    @Override
    public Menu getMenuLevelsSurcharge(int menuId) throws FailedSearchException {
        Menu menu = null;
        try (Connection conn = Connector.getInstance().getConnection();
             CallableStatement cstmt = conn.prepareCall("{call getMenuLevelsSurcharge(?)}")){
            cstmt.setInt(1, menuId);
            cstmt.execute();
            try (ResultSet rs = cstmt.getResultSet()) {
                if (rs.next()) {
                    menu = new Menu();
                    menu.setPremiumLevelSurcharge(rs.getInt("PremiumSurcharge"));
                    menu.setLuxeLevelSurcharge(rs.getInt("LuxeSurcharge"));
                }
            }
        } catch (SQLException e){
            throw new FailedSearchException("Error while recovering menu level surcharges", new QueryException(e));
        } catch (FailedDatabaseConnectionException e){
            throw new FailedSearchException(e);
        }

        return menu;
    }


}
