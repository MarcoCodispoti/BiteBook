package com.example.bitebook.controller.view2;

import com.example.bitebook.controller.application.ExplorationController;
import com.example.bitebook.exceptions.FailedSearchException;
import com.example.bitebook.model.bean.AllergenBean;
import com.example.bitebook.model.bean.ChefBean;
import com.example.bitebook.model.bean.DishBean;
import com.example.bitebook.model.bean.MenuBean;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SelectMenuPageControllerG2{

    private static final Logger logger = Logger.getLogger(SelectMenuPageControllerG2.class.getName());


    private final ExplorationController explorationController = new ExplorationController();

    private ChefBean selectedChefBean;
    private MenuBean selectedMenuBean;
    private List<AllergenBean> menuAllergenBeans = new ArrayList<>();


    @FXML private ComboBox<ChefBean> chefComboBox;
    @FXML private ComboBox<MenuBean> menuComboBox;
    @FXML private ListView<DishBean> menuDetailsListView;

    @FXML private Label allergensLabel;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        setupChefComboBox();
        setupMenuComboBox();
        setupDishListView();

        chefComboBox.valueProperty().addListener((_, _, newVal) -> onChefSelected(newVal));
        menuComboBox.valueProperty().addListener((_, _, newVal) -> onMenuSelected(newVal));
    }

    public void initData(ChefBean chefBean) {
        try {
            List<ChefBean> chefsInCity = explorationController.getChefsInCity(chefBean);

            chefComboBox.getItems().clear();
            if (chefsInCity != null) {
                chefComboBox.getItems().addAll(chefsInCity);
            }
        } catch (FailedSearchException e) {
            logger.log(Level.SEVERE, "Error while getting chefs list.", e);
            displayError("System Error: Unable to retrieve chefs in city.");
        }
    }


    private void onChefSelected(ChefBean chef) {
        this.selectedChefBean = chef;
        menuComboBox.getItems().clear();
        menuDetailsListView.getItems().clear();
        allergensLabel.setText("");
        menuAllergenBeans.clear();

        if (chef != null) {
            try {
                List<MenuBean> chefMenus = explorationController.getChefMenus(chef);
                if (chefMenus != null) {
                    menuComboBox.getItems().addAll(chefMenus);
                }
            } catch (FailedSearchException e) {
                logger.log(Level.SEVERE, "Error while getting menus list.", e);
                displayError("Error occurred while searching menus.");
            }
        }
    }


    private void onMenuSelected(MenuBean menu) {
        this.selectedMenuBean = menu;
        menuDetailsListView.getItems().clear();
        allergensLabel.setText("");
        if (menu != null) {
            try {
                List<DishBean> dishes = explorationController.getCourses(menu);
                if (dishes != null) {
                    menuDetailsListView.getItems().addAll(dishes);
                    this.menuAllergenBeans = explorationController.getMenuAllergens(dishes);
                    allergensLabel.setText(formatAllergensList(menuAllergenBeans));
                }
            } catch (FailedSearchException e){
                logger.log(Level.SEVERE, "Error while getting menu details.", e);
                displayError("Error occurred while getting menu details.");
            }
        }
    }


    private void setupChefComboBox() {
        chefComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(ChefBean chef) {
                if (chef == null) return null;
                String specs = chef.getSpecializationTypes().stream()
                        .map(Enum::toString)
                        .collect(Collectors.joining(", "));
                return String.format("%s %s (%s - %s)",
                        chef.getName(), chef.getSurname(), chef.getCookingStyle(), specs);
            }

            @Override
            public ChefBean fromString(String string) { return null; }
        });
    }

    private void setupMenuComboBox() {
        menuComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(MenuBean menu) {
                if (menu == null) return null;
                return String.format("%s  ( Courses: %d | Style: %s | Price per person: %d € )",
                        menu.getName(), menu.getNumberOfCourses(), String.valueOf(menu.getDietType()).toLowerCase(), menu.getPricePerPerson());
            }

            @Override
            public MenuBean fromString(String string) { return null; }
        });
    }

    private void setupDishListView() {
        menuDetailsListView.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(DishBean dish, boolean empty) {
                super.updateItem(dish, empty);
                if (empty || dish == null) {
                    setText(null);
                } else {
                    setText(String.format("%s: %s - %s",
                            dish.getCourseType().toString().replace("_", " "),
                            dish.getName(),
                            dish.getDescription()));
                }
            }
        });
    }


    @FXML
    void clickedOnBook() {
        if (!explorationController.isLoggedClient()) {
            displayError("You must be logged in to proceed!");
            return;
        }

        if (selectedChefBean == null || selectedMenuBean == null) {
            displayError("Please select a chef and a menu first.");
            return;
        }

        ServiceRequestPageControllerG2 serviceRequestPageControllerG2 = FxmlLoader2.setPageAndReturnController("ServiceRequestPage2");
        if(serviceRequestPageControllerG2 != null){
            serviceRequestPageControllerG2.initData(selectedChefBean,selectedMenuBean,menuAllergenBeans);
        }



    }

    @FXML
    void clickedOnBack() {
        FxmlLoader2.setPage("ClientHomePage2");
    }

    @FXML
    void clickedOnAllergies() {
        navigateToIfLogged("AllergiesPage2", "You must be logged in to view Allergies");
    }

    @FXML
    void clickedOnRequests() {
        navigateToIfLogged("ClientRequestsPage2", "You must be logged in to view Requests");
    }


    private String formatAllergensList(List<AllergenBean> allergens) {
        if (allergens == null || allergens.isEmpty()) return "None";
        return allergens.stream()
                .map(AllergenBean::getName)
                .distinct()
                .collect(Collectors.joining(", "));
    }


    private void navigateToIfLogged(String pageName, String errorMsg) {
        if (explorationController.isLoggedClient()) {
            FxmlLoader2.setPage(pageName);
        } else {
            displayError(errorMsg);
        }
    }


    private void displayError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }



}
