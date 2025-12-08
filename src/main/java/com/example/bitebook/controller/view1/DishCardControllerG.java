package com.example.bitebook.controller.view1;

import com.example.bitebook.model.bean.AllergenBean;
import com.example.bitebook.model.bean.DishBean;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public class DishCardControllerG{


    private DishBean dishBean;

    @FXML
    private Label allergensLabel;

    @FXML
    private Label courseTypeLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label nameLabel;


    public void initData(DishBean dishBean) {
        if (dishBean == null) return;

        this.dishBean = dishBean;
        nameLabel.setText(dishBean.getName());
        if (dishBean.getCourseType() != null) {
            String formattedType = dishBean.getCourseType().toString().toLowerCase().replace("_", " ");
            courseTypeLabel.setText(formattedType.substring(0, 1).toUpperCase() + formattedType.substring(1));
        } else {
            courseTypeLabel.setText("");
        }
        descriptionLabel.setText(dishBean.getDescription());
        allergensLabel.setText(formatAllergensString());
    }



    private String formatAllergensString() {
        List<AllergenBean> allergensList = dishBean.getAllergens();

        if (allergensList == null || allergensList.isEmpty()) {
            return "No Allergens";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < allergensList.size(); i++) {
            sb.append(allergensList.get(i).getName());
            if (i < allergensList.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }


}

