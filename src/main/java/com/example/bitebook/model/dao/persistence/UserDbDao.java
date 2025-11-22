package com.example.bitebook.model.dao.persistence;

import com.example.bitebook.exceptions.FailedDatabaseConnectionException;
import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.Chef;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.dao.UserDao;
import com.example.bitebook.model.enums.CookingStyle;
import com.example.bitebook.model.enums.Role;
import com.example.bitebook.model.enums.SpecializationType;
import com.example.bitebook.util.Connector;

import javax.security.auth.login.FailedLoginException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class UserDbDao implements UserDao {

    @Override
    public Role getCredentialsRole(String email, String password) throws FailedDatabaseConnectionException, FailedLoginException{
        Connection conn = null;
        Role role = null;

        try{
            System.out.println("Vado a prendere la connessione" );
            conn = Connector.getInstance().getConnection();
            System.out.println("Effettuo la connessione database: " + conn );
            CallableStatement cstmt = conn.prepareCall("{call GetCredentialsRole(?,?)}");
            cstmt.setString(1, email);
            cstmt.setString(2, password);

            cstmt.execute();
            ResultSet rs = cstmt.getResultSet();

            try{
                if(rs.next()){
                    System.out.println("La stringa ottenuta dal databse è: " + rs.getString("UserRole"));
                    role = Role.fromString(rs.getString("UserRole"));
                    System.out.println("La stringa ottenuta dal databse è: " + rs.getString("UserRole"));
                } else {
                    System.out.println("Nessun account con queste credenziali");
                    throw new FailedLoginException("Nessun account con queste credenziali");
                }
            } catch(IllegalArgumentException e){
                // e.printStackTrace();
            } catch(FailedLoginException e){
                throw new FailedLoginException(e.getMessage());
            }
            rs.close();
            cstmt.close();
        } catch(FailedLoginException e){
            throw new FailedLoginException();
        } catch (SQLException e){
            // to be handled
        }
        return role;
    }


    @Override
    public Chef getChefInfo(String email, String password){
        Connection conn = null;
        Chef chef = null;

        try{
            conn = Connector.getInstance().getConnection();
            System.out.println("Effettuo la connessione database: " + conn + " Procedo alla prima query" );
            CallableStatement cstmt = conn.prepareCall("{call GetChefInfo(?,?)}");
            cstmt.setString(1, email);
            cstmt.setString(2, password);
            System.out.println("Chiamo la prima query");
            cstmt.execute();
            System.out.println("Ho eseguito la prima query");

            ResultSet rs = cstmt.getResultSet();

            while(rs.next()){
                chef = new Chef(rs.getInt("IdUser"), rs.getString("Name"), rs.getString("Surname"),CookingStyle.fromString(rs.getString("CookingStyle")),rs.getString("City"));
                // Convertire CookingStyle da Enum SQL (Stringa) a Enum Java
                chef.setEmail(email);
                chef.setPassword(password);
            }
            rs.close();
            cstmt.close();

            System.out.println("Ho chiuso la prima call a query");

            System.out.println("Preparo la seconda prepared call e l'id è: " +chef.getId());
            conn = Connector.getInstance().getConnection();
            CallableStatement cstmt2 = conn.prepareCall("{call GetChefSpecializations(?)}");
            cstmt2.setInt(1, chef.getId());
            System.out.println("Chiamo la seconda query");
            cstmt2.execute();
            System.out.println("Ho eseguito la seconda call a query");
            ResultSet rs2 = cstmt2.getResultSet();

            Vector<SpecializationType> specializations = new Vector<>();

            while(rs2.next()){
                SpecializationType specialization = SpecializationType.fromString(rs2.getString("Specialization"));
                System.out.println("Found specialization: " + rs2.getString("Specialization"));
                specializations.add(specialization);
            }
            rs2.close();
            cstmt2.close();

            chef.setSpecializations(specializations);

            // anche qui dovrei prendere i menu ma lascio stare
            chef.setOfferedMenus(null);

        } catch (Exception e){
            System.out.println("Si è verificata un eccezione");
            // e.printStackTrace(); e.getMessage();
        }
        if(chef == null){
            // throw new FailedLoginException();  da aggiungere l'eccezione a tutti i dao
            System.out.println("non è stato possibile ottenre lo chef");
        }
        return chef;
    }

    @Override
    public Client getClientInfo(String email, String password) {
        Connection conn = null;
        Client client = null;

        try{
            conn = Connector.getInstance().getConnection();
            CallableStatement cstmt = conn.prepareCall("{call GetClientInfo(?,?)}");
            cstmt.setString(1, email);
            cstmt.setString(2, password);
            cstmt.execute();
            System.out.println("Ho eseguito la prima query");

            ResultSet rs = cstmt.getResultSet();

            while(rs.next()){
                client = new Client(rs.getInt("IdUser"), rs.getString("Name"), rs.getString("Surname"));
                client.setEmail(email);
                client.setPassword(password);
            }
            rs.close();
            cstmt.close();

            System.out.println("Ho chiuso la prima call a query");

            // Devo eliminare questa parte

//            System.out.println("Preparo la seconda prepared call e l'id è: " +client.getId());
//            conn = Connector.getInstance().getConnection();
//            CallableStatement cstmt2 = conn.prepareCall("{call GetClientAllergies(?)}");
//            cstmt2.setInt(1, client.getId());
//            System.out.println("Chiamo la seconda query");
//            cstmt2.execute();
//            System.out.println("Ho eseguito la seconda call a query");
//            ResultSet rs2 = cstmt2.getResultSet();
//
//            Vector<Allergen> allergies = new Vector<>();
//
//            while(rs2.next()){
//                Allergen allergy = new Allergen(rs2.getInt("AllergenId"),rs2.getString("AllergenName"));
//                System.out.println("Found allergy: " + rs2.getString("AllergenName"));
//                allergies.add(allergy);
//                System.out.println("Le allergie del client sono: " + allergies);
//            }
//            rs2.close();
//            cstmt2.close();
//
//            client.setAllergies(allergies);

            // fino a qui dovrei eliminare

        } catch (Exception e){
            System.out.println("Si è verificata un eccezione");
            // e.printStackTrace(); e.getMessage();
        }
        if(client == null){
            // throw new FailedLoginException();  da aggiungere l'eccezione a tutti i dao
            System.out.println("non è stato possibile ottenre lo chef");
        }

        System.out.println("Fin qui funziona bene");
        return client;


    }

    public String OttieniAllergies(Vector<Allergen> allergies) {
        if (allergies == null || allergies.isEmpty()) {
            return "Nessuna allergia";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < allergies.size(); i++) {
            sb.append(allergies.get(i).getName());
            if (i < allergies.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
