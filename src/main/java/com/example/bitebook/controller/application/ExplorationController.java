package com.example.bitebook.controller.application;

import com.example.bitebook.exceptions.NoChefInCityException;
import com.example.bitebook.model.*;
import com.example.bitebook.model.bean.*;
import com.example.bitebook.model.dao.*;
import com.example.bitebook.model.dao.persistence.ChefDbDao;
import com.example.bitebook.model.dao.persistence.DishDbDao;
import com.example.bitebook.model.dao.persistence.MenuDbDao;
import com.example.bitebook.model.enums.RequestStatus;
import com.example.bitebook.model.singleton.LoggedUser;

import java.sql.SQLException;
import java.util.Vector;

public class ExplorationController{

    public Boolean checkCityChefs(ChefBean chefBean){
        Vector<Chef> chefsInCity = new Vector<>();
        chefsInCity = null;
        ChefDbDao chefDbDao = new ChefDbDao();
        try{
            chefsInCity = chefDbDao.findCityChefs(chefBean.getCity());
        } catch (NoChefInCityException e) {
            return false;
        }
        return true;
    }


    public static boolean isLoggedClient(){
        return LoggedUser.getInstance().getClient() != null ;
    }


    public Vector<ChefBean> getChefsInCity(ChefBean chefBean){
        Vector<Chef> chefsInCity = new Vector<>();
        Vector<ChefBean> chefInCityBeans = new Vector<>();
        chefsInCity = null;
        // ChefDbDao chefDbDao = new ChefDbDao();
        ChefDao chefDao = DaoFactory.getChefDao();
        try{
            chefsInCity = chefDao.getChefsInCity(chefBean.getCity());
        if(chefsInCity == null || chefsInCity.isEmpty()){
            throw new SQLException();
        }

        for(Chef chef : chefsInCity){
            ChefBean newChefbean = new ChefBean(chef);
            chefInCityBeans.add(newChefbean);
        }


        } catch(SQLException e){
            e.printStackTrace();
            e.getMessage();
            e.getCause();
            return null;
        }
        return chefInCityBeans;
        // sreturn chefsInCity; // Qui dovrei ritornare il Bean o un Vector<ChefBean> ??
                            // potrei ritornarli come un attributo Vector<Chef> di Bean?
    }

    // il controller applicativo non dovrebbe contenere blocchi try-catch (per best practice)
    // dovrebbe o gestire completamente le eccezioni o limitarsi a rilanciare quelle che si presentano in esso

    // Dividere questo controller in due controller: ExploreChefController e ExploreMenuController ??

    public Vector<MenuBean> getChefMenus(ChefBean chefBean){
        // Memo: Internamente (e quando comunica con la DAO) il controller applicativo uso il Model
        // Memo: Usa i Bean per comunicare Da/Verso il controller grafico
        // Controllare che questa cosa sia stata rispettata anche in altre parti del codice
        Vector<Menu> chefMenus = new Vector<>();
        chefMenus = null;
        // MenuDbDao menuDbDao = new MenuDbDao();
        MenuDao menuDao = DaoFactory.getMenuDao();
        Vector<MenuBean> chefMenuBeans = new Vector<>();
        try{
            chefMenus = menuDao.getChefMenus(chefBean.getId());

            for(Menu menu : chefMenus){
                MenuBean menuBean;
                menuBean = new MenuBean(menu);
                chefMenuBeans.add(menuBean);
            }
        } catch (Exception e){
            return null;
        }
        return chefMenuBeans; // da adattare poi e convertire in un vettore di MenuBean o come prima passarlo come attributo del bean?
    }


    public Vector<DishBean> getCourses(MenuBean menuBean){
        Vector<Dish> courses = new Vector<>();
        courses = null;
        // MenuDbDao menuDbDao = new MenuDbDao();
        MenuDao menuDao = DaoFactory.getMenuDao();

        Vector<DishBean> coursesBean = new Vector<>();

        try{
            courses = menuDao.getMenuCourses(menuBean.getId());

            System.out.println("ho trovato: " + courses.size() + " portate nel controler ");

            // Prendo tutti i piatti con i loro dettagli

            DishBean dishBean = null;


            // per ogni piatto -> recupero i suoi allergeni
            for(Dish dish : courses){
                Vector< Allergen> dishAllergens = new Vector<>();
                // DishDbDao dishDbDao = new DishDbDao();
                DishDao dishDao = DaoFactory.getDishDao();
                dishAllergens = dishDao.getDishAllergens(dish.getId());
                dish.setAllergens(dishAllergens);
            }

            for(Dish dish : courses){
                dishBean = new DishBean(dish);
                coursesBean.add(dishBean);
            }
            System.out.println("ho trovato: " + coursesBean.size() + " portate nel bean nel controler ");
        } catch (Exception e){
            return null;
        }

        // da Inserire qui il dao da cui prendere le cose
        return coursesBean;
    }


    public Vector<AllergenBean> getMenuAllergens(Vector<DishBean> courseBeans){
        Vector<AllergenBean> menuAllergenBeans = new Vector<>();

        // 1. Itera su tutti i piatti forniti
        for (DishBean dish : courseBeans) {

            Vector<Allergen> dishAllergens = dish.getAllergens();

            if (dishAllergens != null) {

                // 2. Itera sugli allergeni del piatto corrente (Allergen)
                for (Allergen newAllergen : dishAllergens) {

                    boolean alreadyExists = false;

                    // 3. Controlla manualmente l'unicità confrontando gli ID con gli AllergenBean già presenti
                    // Questo è l'approccio meno efficiente (O(n^2) complessità).
                    for (AllergenBean existingBean : menuAllergenBeans) {

                        // Confronta l'ID del nuovo Allergen con l'ID dell'AllergenBean esistente
                        if (existingBean.getId() == newAllergen.getId()) {
                            alreadyExists = true;
                            break;
                        }
                    }

                    // 4. Se non esiste un Bean con quell'ID, lo crea e lo aggiunge
                    if (!alreadyExists) {
                        // Crea l'AllergenBean dal nuovo Allergen
                        AllergenBean allergenBean = new AllergenBean(newAllergen);
                        menuAllergenBeans.add(allergenBean);
                    }
                }
            }
        }
        return menuAllergenBeans;
    }

    // Da spostare in Manage Request Controller e rinomicare Request Controller?
    public static Vector<ServiceRequestBean> getClientRequests(){
        Vector<ServiceRequestBean> clientRequestBeans = new Vector<>();
        Vector<ServiceRequest> clientRequests = new Vector<>();

        ServiceRequestDao serviceRequestDao = DaoFactory.getServiceRequestDao();
        try {
            clientRequests = serviceRequestDao.getClientServiceRequests(LoggedUser.getInstance().getClient());
            if(clientRequests != null){
                for(ServiceRequest clientRequest : clientRequests){
                    ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
                    ChefBean chefBean = new ChefBean();
                    MenuBean menuBean = new MenuBean();
                    ReservationDetailsBean reservationDetailsBeans = new ReservationDetailsBean();
                    serviceRequestBean.setId(clientRequest.getId());
                    chefBean.setName(clientRequest.getChef().getName());
                    chefBean.setSurname(clientRequest.getChef().getSurname());
                    System.out.println("Controller dalla classe client request: " + clientRequest.getMenu().getName());
                    menuBean.setName(clientRequest.getMenu().getName());
                    System.out.println("Controller MenuName: " + menuBean.getName());
                    reservationDetailsBeans.setSelectedMenuLevel(clientRequest.getReservationDetails().getSelectedMenuLevel());
                    reservationDetailsBeans.setParticipantNumber(clientRequest.getReservationDetails().getParticipantNumber());
                    serviceRequestBean.setTotalPrice(clientRequest.getTotalPrice());
                    reservationDetailsBeans.setDate(clientRequest.getReservationDetails().getDate());
                    reservationDetailsBeans.setTime(clientRequest.getReservationDetails().getTime());
                    reservationDetailsBeans.setAddress(clientRequest.getReservationDetails().getAddress());
                    serviceRequestBean.setStatus(clientRequest.getStatus());
                    // serviceRequestBean.setClient(clientRequest.getClient());

//                    serviceRequestBean.setClientBean(new ClientBean(clientRequest.getClient().getName(), clientRequest.getClient().getSurname()));

                    serviceRequestBean.setChefBean(chefBean);

                    serviceRequestBean.setMenuBean(menuBean);
                    serviceRequestBean.setReservationDetails(reservationDetailsBeans);
                    clientRequestBeans.add(serviceRequestBean);
                }
            } else{
                return null;
            }
        } catch (Exception e){
            e.printStackTrace();
            e.getMessage();
            e.getCause();
            return null;
        }
        return clientRequestBeans;
    }



    public static Vector<ServiceRequestBean> getApprovedServiceRequests() throws Exception{
        Chef chef = LoggedUser.getInstance().getChef();
            Vector<ServiceRequestBean> chefServiceRequestBeans = new Vector<>();
            Vector<ServiceRequest> chefServiceRequests;

            try{
                ServiceRequestDao serviceRequestDao = DaoFactory.getServiceRequestDao();
                chefServiceRequests = serviceRequestDao.getChefServiceRequests(LoggedUser.getInstance().getChef());
                if(!(chefServiceRequestBeans == null)){
                System.out.println( chefServiceRequests.size() + " richieste trovate dal DAO");

                    for(ServiceRequest chefServiceRequest : chefServiceRequests){
                        if(chefServiceRequest.getStatus().equals(RequestStatus.APPROVED)) {
                            ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
                            // Client client = new Client();
                            MenuBean menuBean = new MenuBean();
                            ReservationDetailsBean reservationDetailsBeans = new ReservationDetailsBean();
                            serviceRequestBean.setId(chefServiceRequest.getId());
//                            client.setName(chefServiceRequest.getClient().getName());
//                            client.setSurname(chefServiceRequest.getClient().getSurname());
                            System.out.println("Controller dalla classe client request: " + chefServiceRequest.getMenu().getName());
                            menuBean.setName(chefServiceRequest.getMenu().getName());
                            System.out.println("Controller MenuName: " + menuBean.getName());
                            reservationDetailsBeans.setSelectedMenuLevel(chefServiceRequest.getReservationDetails().getSelectedMenuLevel());
                            reservationDetailsBeans.setParticipantNumber(chefServiceRequest.getReservationDetails().getParticipantNumber());
                            serviceRequestBean.setTotalPrice(chefServiceRequest.getTotalPrice());
                            reservationDetailsBeans.setDate(chefServiceRequest.getReservationDetails().getDate());
                            reservationDetailsBeans.setTime(chefServiceRequest.getReservationDetails().getTime());
                            reservationDetailsBeans.setAddress(chefServiceRequest.getReservationDetails().getAddress());
                            // serviceRequestBean.setStatus(chefServiceRequest.getStatus());
                            // serviceRequestBean.setClient(chefServiceRequest.getClient());
                            // serviceRequestBean.setClient(client);
                            System.out.println("Nomi e cognomi trovati da getApprovedServiceRequests: " + chefServiceRequest.getClient().getName() + " " + chefServiceRequest.getClient().getSurname());
                            serviceRequestBean.setClientBean(new ClientBean(chefServiceRequest.getClient().getName(),chefServiceRequest.getClient().getSurname()));

                            serviceRequestBean.setMenuBean(menuBean);
                            serviceRequestBean.setReservationDetails(reservationDetailsBeans);
                            chefServiceRequestBeans.add(serviceRequestBean);
                        }
                    }
                } else{
                    System.out.println("Chef service requests empty");
                    // throw new Exception();
                }

            } catch(Exception e){
                e.printStackTrace();
                e.getCause();
                e.getMessage();
                return null;
                // throw new Exception();
            }
            return chefServiceRequestBeans;
        }




}
