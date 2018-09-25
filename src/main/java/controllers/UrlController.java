package controllers;

import config.AppProperties;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import manager.GoogleCalendarManager;
import parser.RefereeCastWebParser;
import sample.RCSMApp;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Adrian Wieczorek on 10/10/2017.
 */
public class UrlController {
    @FXML
    private Button exitButton;

    @FXML
    private TextField urlField;

    @FXML
    private Button goButton;

    @FXML
    private Button backButton;

    private HomeController homeController;

    private GoogleCalendarManager manager;

    public GoogleCalendarManager getManager() {
        return manager;
    }

    public void setManager(GoogleCalendarManager manager) {
        this.manager = manager;
    }

    public HomeController getHomeController() {
        return homeController;
    }

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    public void onClickActionGoButton() throws IOException {
        Map<String, String> responseMap;

        try {
            responseMap = getInformationAboutMatch();
            createEvent(responseMap);
            showSuccessToast();
        } catch (IOException e) {
            showUnsuccessToast("Error 01", "Check your internet connection!");
        } catch (NullPointerException e) {
            showUnsuccessToast("Error 02", "Check your URL address! That don't be match!");
        }
    }

    public void onClickActionExitButton() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void onClickActionBackButton() {
        setHomeStage();
    }

    private void setHomeStage() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.setScene(homeController.getSaveScene());
        stage.centerOnScreen();
    }

    private void createEvent(Map<String, String> informationAboutMatch) throws IOException {
        manager.createEvent(informationAboutMatch);
        if (manager.getCreatedEventResponseBean().getResponseCode() != 200) {
            String headerText = "Sorry, I can't add event to calendar! Try again later...";
            String contentText = "If you don't have event in your calendar, you will write to administrator: theadrian219@gmail.com";
            showUnsuccessToast(headerText, contentText);
        } else {
            setDisplayName();
        }

        urlField.setText(null);
    }

    private void setDisplayName() throws IOException {
        String displayName = manager.getCreatedEventResponseBean().getCreator().getDisplayName();
        AppProperties.setProperty("displayName", displayName);
        homeController.setDisplayNameLabel(displayName);
        homeController.setLoginButtonDisable(true);
    }

    private Map<String, String> getInformationAboutMatch() throws IOException {
        RefereeCastWebParser parser = new RefereeCastWebParser();
        Map<String, String> responseMap;

        responseMap = parser.getInformationAboutMatch(urlField.getText());

        return responseMap;
    }

    private void showSuccessToast() {
        String title = "Success!";
        String headerText = "Your event has been created!";
        String contentText = "If you don't have event in your calendar, you will write to administrator: theadrian219@gmail.com";
        RCSMApp.showAlert(Alert.AlertType.INFORMATION, title, headerText, contentText);
    }

    private void showUnsuccessToast(String headerText, String contentText) {
        String title = "Unsuccess!";
        RCSMApp.showAlert(Alert.AlertType.WARNING, title, headerText, contentText);
    }
}
