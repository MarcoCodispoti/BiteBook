package com.example.bitebook.controller.view1;

import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.bean.DishBean;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public class DishCardControllerG {
    private MenuDetailsPageControllerG parentController;
    DishBean dishBean;

    @FXML
    private Label allergensLabel;

    @FXML
    private Label courseTypeLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label nameLabel;

    public void initData(DishBean dishBean) {
        this.dishBean = dishBean;
        nameLabel.setText(dishBean.getName());
        courseTypeLabel.setText(String.valueOf(dishBean.getCourseType()).toLowerCase().replaceAll("_", " "));
        descriptionLabel.setText(dishBean.getDescription());
        allergensLabel.setText(getAllergensString());
    }

    public void setParentController(MenuDetailsPageControllerG parentController) {
        this.parentController = parentController;
    }

    ;

    private String getAllergensString() {
        String allergensString = "";
        List<Allergen> allergensList = dishBean.getAllergens();
        int index = 0;
        for (Allergen allergen : allergensList) {
            if (index == 0) {
                allergensString = allergensString.concat(allergen.getName());
            } else {
                allergensString = allergensString.concat(", ".concat(allergen.getName()));
            }
            index++;
        }
        return allergensString;
    }

}

