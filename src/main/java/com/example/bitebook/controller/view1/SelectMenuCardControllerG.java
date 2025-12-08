package com.example.bitebook.controller.view1;

import com.example.bitebook.model.bean.MenuBean;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class SelectMenuCardControllerG{

    private SelectMenuPageControllerG parentController;
    private Parent cardUi;
    private MenuBean cardMenuBean;

    @FXML private Label nameLabel;
    @FXML private Label dietTypeLabel;
    @FXML private Label numberOfCoursesLabel;
    @FXML private Label pricePerPersonLabel;

    @FXML
    void handleClick(){
        if (parentController != null && cardMenuBean != null) {
            parentController.setSelectedMenu(cardMenuBean, cardUi);
        }
    }

    public void initData(MenuBean menuBean) {
        this.cardMenuBean = menuBean;

        if (menuBean == null) {
            return;
        }

        nameLabel.setText(menuBean.getName());

        if (menuBean.getDietType() != null) {
            dietTypeLabel.setText(menuBean.getDietType().toString().toLowerCase());
        } else {
            dietTypeLabel.setText("-");
        }

        numberOfCoursesLabel.setText(String.valueOf(menuBean.getNumberOfCourses()));
        pricePerPersonLabel.setText(menuBean.getPricePerPerson() + " €");
    }

    public void setParentController(SelectMenuPageControllerG parentController) {
        this.parentController = parentController;
    }

    public void setCardUi(Parent cardUi) {
        this.cardUi = cardUi;
    }



}
