package controllers;

import config.AppProperties;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Adrian Wieczorek on 11/10/2017.
 */
public class AboutController {
    @FXML
    private Button skipButton;
    @FXML
    private Button exitButton;
    @FXML
    private Label aboutLabel;

    private HomeController homeController;

    @FXML
    public void initialize() throws IOException{
        aboutLabel.setText(getInfo());

    }

    public HomeController getHomeController() {
        return homeController;
    }

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    private String getInfo() throws IOException{
        return  "RCSM " + AppProperties.getProperty("version") +
                "\nApplication by" +
                "\n" + AppProperties.getProperty("author") +
                "\n\nContact with administrator: " +
                "\n" + AppProperties.getProperty("contactEmail");
    }

    public void onClickActionBackButton() {
        setHomeStage();
    }

    private void setHomeStage() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.setScene(homeController.getSaveScene());
        stage.centerOnScreen();
    }

    public void onClickActionExitButton(){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
