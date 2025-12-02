package com.example.bitebook.controller.view1;

import com.example.bitebook.controller.application.ExplorationController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.Allergen;
import com.example.bitebook.model.bean.AllergenBean;
import com.example.bitebook.model.bean.ChefBean;
import com.example.bitebook.model.bean.DishBean;
import com.example.bitebook.model.bean.MenuBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class MenuDetailsPageControllerG{
    ChefBean menusChefBean;
    MenuBean selectedMenuBean;
    List<DishBean> courseBeans;
    List<Allergen> menuAllergens;         // da mettere List<AllergenBean> menuAllergenBeans
    List<AllergenBean> menuAllergenBeans;

    @FXML
    private ScrollPane menusScrollPane;

    @FXML
    private Hyperlink requestsHyperlink;

    @FXML
    private VBox coursesVBox;

    @FXML
    private Label nameLabel;

    @FXML
    private Label numberOfCoursesLabel;

    @FXML
    private Label themeLabel;

    @FXML
    private Label dietTypeLabel;

    @FXML
    private Label pricePerPersonLabel;

    @FXML
    private Label allergensLabel;

    @FXML
    private Hyperlink allergiesHyperlink;

    @FXML
    private Hyperlink homepageHyperlink;

    @FXML
    private Label errorLabel;

    @FXML
    private Button selectRequestButton;

    @FXML
    private Button backToMenusButton;

    @FXML
    void clickedOnHomepage(ActionEvent event){
        FxmlLoader.setPage("ClientHomePage");
    }


    @FXML
    void clickedOnRequests(ActionEvent event){
        ExplorationController explorationController = new ExplorationController();
        if(explorationController.isLoggedClient()){
            FxmlLoader.setPage("ClientRequestsPage");
        } else{
            errorLabel.setText("You must be logged in to access this page!");
        }
    }


    @FXML
    void clickedOnAllergies(ActionEvent event){
        ExplorationController explorationController = new ExplorationController();
        if(explorationController.isLoggedClient()){
            FxmlLoader.setPage("AllergiesPage");
        } else{
            errorLabel.setText("You must be logged in to access this page!");
        }
    }




    public void initData(MenuBean menuBean,ChefBean selectedChefBean){
        this.menusChefBean = selectedChefBean;
        this.selectedMenuBean = menuBean;
        nameLabel.setText(menuBean.getName());
        dietTypeLabel.setText(menuBean.getDietType().toString().toLowerCase());
        numberOfCoursesLabel.setText(String.valueOf(menuBean.getNumberOfCourses()));
        pricePerPersonLabel.setText(String.valueOf(menuBean.getPricePerPerson()) + " €");

        errorLabel.setText("Menu: " + selectedMenuBean.getName() + " " +  selectedMenuBean.getId());
        ExplorationController explorationController = new ExplorationController();

        try {
            courseBeans = explorationController.getCourses(selectedMenuBean);
        } catch (FailedSearchException e) {
            errorLabel.setText("Error while getting courses info, please try again! ");
            return;
        }
        errorLabel.setText("trovate " + courseBeans.size() + " courses");
        populateCourses();

        // menuAllergens = explorationController.getMenuAllergens(courseBeans);

        menuAllergenBeans = explorationController.getMenuAllergens(courseBeans);

        // eliminare questo blocco sotto
        System.out.println("menuAllergens: ");
        for(AllergenBean allergenBean : menuAllergenBeans){
            System.out.println(allergenBean.getName() + " ");
        }

        allergensLabel.setText(getAllergensAsString());


    }


    @FXML
    void clickedOnConfirmMenu(ActionEvent event){
        ExplorationController explorationController = new ExplorationController();
        if(explorationController.isLoggedClient()){
            ServiceRequestPageControllerG serviceRequestPageControllerG = FxmlLoader.setPageAndReturnController("ServiceRequestPage");
            if (serviceRequestPageControllerG != null) {
                serviceRequestPageControllerG.initData(selectedMenuBean, menuAllergenBeans, menusChefBean);
            }
            // FxmlLoader.setPage("ServiceRequestPage");
        } else{
            errorLabel.setText("Devi effettuare il login");
        }
    }

    @FXML
    void clickedOnBackToMenus(ActionEvent event){
        SelectMenuPageControllerG selectMenuPageControllerG = FxmlLoader.setPageAndReturnController("SelectMenuPage");
        selectMenuPageControllerG.initData(menusChefBean);
    }



    public void populateCourses(){
        coursesVBox.getChildren().clear();

        for(DishBean dishBean : courseBeans){
            try{
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("/com/example/bitebook/view1/DishCard.fxml"));
                Parent dishCard = cardLoader.load();

                DishCardControllerG controller = cardLoader.getController();
                controller.initData(dishBean);
                // controller.setCardUI(dishCard);
                controller.setParentController(this);

                coursesVBox.getChildren().add(dishCard);

            }
            catch (Exception e){
                e.printStackTrace();
                e.getMessage();
                e.getCause();
                return;
            }
        }
    }

    private String getAllergensAsString(){
        String allergensString = "";
        int index = 0;
        for(AllergenBean allergenBean : menuAllergenBeans){
            if(index == 0){
                allergensString = allergensString.concat(allergenBean.getName());
            } else{
                allergensString = allergensString.concat(", ").concat(allergenBean.getName());
            }
            index++;
        }
        return allergensString;
    }


}
