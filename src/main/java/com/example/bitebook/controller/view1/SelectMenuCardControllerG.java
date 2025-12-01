package com.example.bitebook.controller.view1;

import com.example.bitebook.model.bean.MenuBean;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class SelectMenuCardControllerG {
    private SelectMenuPageControllerG parentController;
    private Parent cardUi;
    MenuBean cardMenuBean;


    @FXML
    private Label nameLabel;

    @FXML
    private Label dietTypeLabel;

    @FXML
    private Label numberOfCoursesLabel;

    @FXML
    private Label pricePerPersonLabel;



    @FXML
    void handleClick() {
        if(parentController != null){
            parentController.setSelectedMenu(cardMenuBean,cardUi);
        }
    }

    public void initData(MenuBean menuBean){
        this.cardMenuBean = menuBean;
        nameLabel.setText(menuBean.getName());
        dietTypeLabel.setText(menuBean.getDietType().toString().toLowerCase());
        numberOfCoursesLabel.setText(String.valueOf(menuBean.getNumberOfCourses()));
        pricePerPersonLabel.setText(String.valueOf(menuBean.getPricePerPerson()) + " €");
    }

    public void setParentController(SelectMenuPageControllerG parentController){this.parentController = parentController;}

    public void setCardUi(Parent cardUi){this.cardUi = cardUi;}
}
