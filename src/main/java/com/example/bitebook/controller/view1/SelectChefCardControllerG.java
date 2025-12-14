package com.example.bitebook.controller.view1;

import com.example.bitebook.model.bean.ChefBean;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class SelectChefCardControllerG{

    @FXML
    private Label styleLabel;
    @FXML
    private Label surnameLabel;
    @FXML
    private Label specializationsLabel;
    @FXML
    private Label nameLabel;


    private SelectChefPageControllerG parentController;
    private Parent cardUi;
    private ChefBean cardChefBean;



    @FXML
    private void handleClick(){
        if(parentController != null){
            parentController.setSelectedChef(cardChefBean, cardUi);
        }
    }



    public void initData(ChefBean cardChefBean){
        this.cardChefBean = cardChefBean;
        nameLabel.setText(cardChefBean.getName());
        surnameLabel.setText(cardChefBean.getSurname());
        styleLabel.setText(String.valueOf(cardChefBean.getCookingStyle()).toLowerCase());
        String specializationsString = String.valueOf(cardChefBean.getSpecializationTypes()).toLowerCase().replace("[","").replace("]","");
        specializationsLabel.setText(specializationsString);
    }



    public void setParentController(SelectChefPageControllerG parentController){
        this.parentController=parentController;
    }



    public void setCardUI(Parent cardUi){
        this.cardUi = cardUi;
    }


}
