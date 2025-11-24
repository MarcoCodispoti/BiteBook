package com.example.bitebook.controller.view2;

import com.example.bitebook.controller.application.ExplorationController;
import com.example.bitebook.model.bean.ChefBean;
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
    private ComboBox<?> menuComboBox;

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



    private String city;
    private Vector<ChefBean> chefListBeans;



    public void initData(ChefBean chefBean){
        this.city = chefBean.getCity();

        ExplorationController explorationController = new ExplorationController();
        chefListBeans = explorationController.getChefsInCity(chefBean);

        fillChefComboBox();
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

}
