package controllers;

import config.AppProperties;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import manager.GoogleCalendarManager;

import java.io.IOException;

/**
 * Created by Adrian Wieczorek on 10/9/2017.
 */
public class HomeController {
    @FXML
    private Button loginButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button skipButton;
    @FXML
    private Button aboutButton;
    @FXML
    private Label accountLabel;
    @FXML
    private Label displayNameLabel;

    private Scene saveScene;
    private UrlController urlController;
    private AboutController aboutController;
    private GoogleCalendarManager manager = new GoogleCalendarManager();

    @FXML
    public void initialize() {
        System.out.println(displayNameLabel.getText());
        if (displayNameLabel.getText().equals("")) {
            accountLabel.setVisible(false);
        }
    }

    public Scene getSaveScene() {
        return saveScene;
    }

    public void setSaveScene(Scene saveScene) {
        this.saveScene = saveScene;
    }

    public void onClickActionLoginButton() {
        showOAuth2WebView();
    }

    private void showOAuth2WebView() {
        BorderPane borderpane = new BorderPane();
        final WebView webView = new WebView();
        borderpane.setCenter(webView);

        webView.getEngine().load(manager.getUrl());

        final Stage loginStage = new Stage();

        webView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if (newValue != Worker.State.SUCCEEDED) {
                    return;
                }

                if (webView.getEngine().getTitle().contains("Success")) {
                    manager.setAuthorizationCode(webView.getEngine().getTitle().substring(13));

                    loginStage.close();

                    try {
                        authorization();
                        setUrlStage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        loginStage.setScene(new Scene(borderpane, 400, 400));
        loginStage.centerOnScreen();
        loginStage.show();
    }

    private void setUrlStage() throws IOException {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        saveScene = exitButton.getScene();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/urlFXML.fxml"));
        Parent root = loader.load();
        urlController = loader.getController();

        urlController.setManager(manager);
        urlController.setHomeController(this);
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
    }

    public void onClickActionExitButton() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void onClickActionSkipButton() throws IOException {
        setUrlStage();
    }

    public void onClickActionAboutButton() throws IOException{
        setAboutStage();
    }

    private void setAboutStage() throws IOException {
        saveScene = exitButton.getScene();
        Stage stage = (Stage) aboutButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/aboutFXML.fxml"));
        Parent root = loader.load();
        aboutController = loader.getController();
        aboutController.setHomeController(this);

        stage.setScene(new Scene(root));
    }

    private void authorization() throws IOException {
        manager.authorization();
        AppProperties.setProperty("refreshToken", manager.getAuthorizationResponseBean().getRefresh_token());
        AppProperties.setProperty("accessToken", manager.getAuthorizationResponseBean().getAccess_token());
    }

    public void setDisplayNameLabel(String displayName) {
        accountLabel.setVisible(true);
        displayNameLabel.setText(displayName);
    }

    public void setLoginButtonDisable(Boolean value) {
        loginButton.setDisable(value);
    }
}
