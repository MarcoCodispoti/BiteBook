package com.example.bitebook.controller.view1;

import com.example.bitebook.model.bean.AllergenBean;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class AllergyCardControllerG{


    @FXML
    private Label allergenName;


    private AllergiesPageControllerG parentController;
    private Parent cardUi;
    private AllergenBean cardAllergenBean;



    @FXML
    private void handleClick(){
        if(parentController != null){
            parentController.setSelectedAllergy(cardAllergenBean,cardUi);
        }
    }



    public void initData(AllergenBean cardAllergenBean){
        this.cardAllergenBean = cardAllergenBean;
        allergenName.setText(cardAllergenBean.getName());
    }



    public void setParentController(AllergiesPageControllerG parentController){
        this.parentController = parentController;
    }



    public void setCardUi(Parent cardUi){
        this.cardUi = cardUi;
    }


}
