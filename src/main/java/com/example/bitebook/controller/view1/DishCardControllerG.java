package com.example.bitebook.controller.view1;

import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.bean.AllergenBean;
import com.example.bitebook.model.bean.DishBean;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public class DishCardControllerG {
//    private MenuDetailsPageControllerG parentController;
//    DishBean dishBean;
//
//    @FXML
//    private Label allergensLabel;
//
//    @FXML
//    private Label courseTypeLabel;
//
//    @FXML
//    private Label descriptionLabel;
//
//    @FXML
//    private Label nameLabel;
//
//    public void initData(DishBean dishBean) {
//        this.dishBean = dishBean;
//        nameLabel.setText(dishBean.getName());
//        courseTypeLabel.setText(String.valueOf(dishBean.getCourseType()).toLowerCase().replaceAll("_", " "));
//        descriptionLabel.setText(dishBean.getDescription());
//        allergensLabel.setText(getAllergensString());
//    }
//
//    public void setParentController(MenuDetailsPageControllerG parentController) {
//        this.parentController = parentController;
//    }
//
//    ;
//
//    private String getAllergensString() {
//        String allergensString = "";
//        List<Allergen> allergensList = dishBean.getAllergens();
//        int index = 0;
//        for (Allergen allergen : allergensList) {
//            if (index == 0) {
//                allergensString = allergensString.concat(allergen.getName());
//            } else {
//                allergensString = allergensString.concat(", ".concat(allergen.getName()));
//            }
//            index++;
//        }
//        return allergensString;
//    }


    private MenuDetailsPageControllerG parentController;

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
        // Controllo robustezza: se il bean è null, evitiamo il crash
        if (dishBean == null) return;

        this.dishBean = dishBean;
        nameLabel.setText(dishBean.getName());

        // FIX: Gestione sicura dell'Enum e formattazione
        if (dishBean.getCourseType() != null) {
            String formattedType = dishBean.getCourseType().toString().toLowerCase().replace("_", " ");
            // Capitalize prima lettera (opzionale, per estetica)
            courseTypeLabel.setText(formattedType.substring(0, 1).toUpperCase() + formattedType.substring(1));
        } else {
            courseTypeLabel.setText("");
        }

        descriptionLabel.setText(dishBean.getDescription());
        allergensLabel.setText(formatAllergensString());
    }

    public void setParentController(MenuDetailsPageControllerG parentController) {
        this.parentController = parentController;
    }

    /**
     * Metodo ottimizzato per la costruzione della stringa (Clean Code)
     */
    private String formatAllergensString() {
        // FIX: Uso AllergenBean
        List<AllergenBean> allergensList = dishBean.getAllergens(); // Assumendo che DishBean restituisca Beans
        // DishBean dovrebbe restituire una lista di AllergenBean -> I Bean non contengono altre entità se non i dati primitivi
        // -> Dovrei riscrivere i Bean per fargli ritornare

        // Controllo Null Safety
        if (allergensList == null || allergensList.isEmpty()) {
            return "Nessuna allergia";
        }

        // FIX: StringBuilder è molto più efficiente di String.concat nei loop
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < allergensList.size(); i++) {
            sb.append(allergensList.get(i).getName());

            // Aggiunge la virgola solo se non è l'ultimo elemento
            if (i < allergensList.size() - 1) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }


}

