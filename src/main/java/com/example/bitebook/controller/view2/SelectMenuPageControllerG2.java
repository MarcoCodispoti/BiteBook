package com.example.bitebook.controller.view2;

import com.example.bitebook.controller.application.ExplorationController;
import com.example.bitebook.model.bean.ChefBean;
import com.example.bitebook.model.bean.MenuBean;
import com.example.bitebook.model.enums.SpecializationType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.Vector;

import static java.lang.String.valueOf;

public class SelectMenuPageControllerG2{

    @FXML
    private Hyperlink requestsHyperlink;

    @FXML
    private ComboBox<String> menuComboBox;

    @FXML
    private Button bookButton;

    @FXML
    private ComboBox<String> chefComboBox;

    @FXML
    private ListView<String> menuDetailsListView;

    @FXML
    private Button backButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Hyperlink allergiesHyperlink;

    @FXML
    private Label errorLabel;


    ExplorationController explorationController = new ExplorationController();
    private String city;
    private Vector<ChefBean> chefListBeans;
    private ChefBean selectedChefBean;
    private Vector<MenuBean> selectedChefMenuBeans;
    private MenuBean selectedMenuBean;



    public void initData(ChefBean chefBean){
        this.city = chefBean.getCity();

        // ExplorationController explorationController = new ExplorationController();
        chefListBeans = explorationController.getChefsInCity(chefBean);

        fillChefComboBox();



        chefComboBox.valueProperty().addListener((observable, oldValue, selectedChefString) -> {
            // 1. Controllo base: esegui solo se c'è una nuova selezione valida
            if(selectedChefString != null && !selectedChefString.isEmpty()) {
                // 2. Estrai l'ID dello Chef dalla stringa e popola i menu
                selectedChefBean = extractChefBean(selectedChefString);
                fillMenuComboBox(selectedChefBean);
            } else {
                // Se la selezione è stata azzerata, svuota anche la ComboBox dei Menu
                menuComboBox.getItems().clear();
                // E aggiorna lo stato visivo (es. nascondi i dettagli)
                // hideMenuDetails();
            }
        });


        menuComboBox.valueProperty().addListener((observable, oldValue, selectedMenuString) -> {
            // 1. Controllo base: esegui solo se c'è una nuova selezione valida
            if(selectedMenuString != null && !selectedMenuString.isEmpty()) {
                // 2. Estrai l'ID dello Chef dalla stringa e popola i menu
                selectedMenuBean = extractMenuBean(selectedMenuString);
                fillMenuDetailsListView();
            } else {
                // Se la selezione è stata azzerata, svuota anche la ComboBox dei Menu
                menuDetailsListView.getItems().clear();
                // E aggiorna lo stato visivo (es. nascondi i dettagli)
                // hideMenuDetails();
            }
        });



    }


    @FXML
    void clickedOnAllergies(ActionEvent event) {

    }

    @FXML
    void clickedOnRequests(ActionEvent event) {

    }

    @FXML
    void clickedOnLogout(ActionEvent event) {

    }

    @FXML
    void clickedOnBook(ActionEvent event) {

    }

    @FXML
    void clickedOnBack(ActionEvent event) {

    }




    public void fillChefComboBox(){
        chefComboBox.getItems().clear();
        for(ChefBean chefBean:chefListBeans){
            chefComboBox.getItems().add("ID:  " + chefBean.getId() + "  Chef: " + chefBean.getName() + "  " + chefBean.getSurname() +  "  Style: " + chefBean.getCookingStyle() + "  Specializations: " + getSpecializationsAsString(chefBean));
        }
    }

    public String getSpecializationsAsString(ChefBean chefBean){
        Vector<SpecializationType> specializationTypes = chefBean.getSpecializationTypes();
        String specializationsString = "";
        int index = 0;
        for(SpecializationType specializationType:specializationTypes){
            if(index==0) {
                specializationsString = specializationsString.concat(valueOf(specializationType)); index++;
            } else{
                specializationsString = specializationsString.concat(" , " + valueOf(specializationType));
            }
        }
        return specializationsString;
    }


    public ChefBean extractChefBean(String chefString){
        int selectedChefId = extractChefId(chefString);
        ChefBean selectedChefBean = null;
        for(ChefBean chefBean:chefListBeans){
            if(chefBean.getId()==selectedChefId){
                selectedChefBean = chefBean;
            }
        }
        return selectedChefBean;
    }


    public MenuBean extractMenuBean(String menuString){
        int selectedMenuId = extractMenuId(menuString);
        MenuBean selectedMenuBean = null;
        for(MenuBean menuBean:selectedChefMenuBeans){
            if(menuBean.getId()==selectedMenuId){
                selectedMenuBean = menuBean;
            }
        }
        return selectedMenuBean;
    }



    public int extractChefId(String selectedChefString) throws NumberFormatException, StringIndexOutOfBoundsException {

        if (selectedChefString == null || selectedChefString.isEmpty()) {
            throw new IllegalArgumentException("Nessuna selezione trovata.");
        }

        // 1. Definisci i limiti della sottostringa che contiene l'ID
        // L'ID inizia SUBITO dopo l'etichetta "ID: " (che ha 4 caratteri)
        final String START_TAG = "ID:";
        final String END_TAG = "Chef:";

        // Cerca l'indice iniziale e finale
        int startIndex = selectedChefString.indexOf(START_TAG);
        int endIndex = selectedChefString.indexOf(END_TAG);

        // Controllo di validità dei tag
        if (startIndex == -1 || endIndex == -1 || endIndex <= startIndex) {
            throw new StringIndexOutOfBoundsException("Formato stringa non valido. Manca 'ID:' o 'Chef:'.");
        }

        // 2. Estrai la sottostringa contenente solo il numero dell'ID
        // Inizio: Subito dopo "ID: " (indice di "ID:" + la sua lunghezza)
        // Fine: L'indice dove inizia "Chef:"
        // Nota: Il "+ 4" tiene conto della lunghezza di "ID:  " (ID e i 2 spazi)
        String idString = selectedChefString.substring(startIndex + START_TAG.length(), endIndex).trim();

        // 3. Converte la stringa pulita in un intero
        return Integer.parseInt(idString);
    }



    public void fillMenuComboBox(ChefBean chefBean){
        menuComboBox.getItems().clear();
        if(chefBean == null){
            errorLabel.setText("Error occured while searching menus"); return;
        }
        selectedChefMenuBeans = explorationController.getChefMenus(chefBean);
        for(MenuBean menuBean:selectedChefMenuBeans){
            menuComboBox.getItems().add("Name: " + menuBean.getName() + "      Diet Type: " + menuBean.getDietType() + "        Number of courses:  " + menuBean.getNumberOfCourses() + "      Price per person: " + menuBean.getPricePerPerson() + "€ ");
        }
    }

    public void fillMenuDetailsListView(){
        menuDetailsListView.getItems().clear();


    }


}
