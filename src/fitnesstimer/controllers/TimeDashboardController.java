/*
 * IPC - Entrega 2
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package fitnesstimer.controllers;

import fitnesstimer.controllers.base.AbstractController;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Dani
 */
public class TimeDashboardController extends AbstractController {

    @FXML
    private Button button;
    @FXML
    private Label label;
    
    private boolean paused = true;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        label.textProperty().bind(i18n.getStringBinding("app.title"));
        // TODO
    }    

    @FXML
    private void handleButtonAction(ActionEvent event) {
        i18n.setLocale(Locale.ITALY);
    }
    
    @FXML
    private void onNext(ActionEvent event) {
        // TODO
    }
    
    @FXML
    private void onPause(ActionEvent event) {
        paused = true;
        // TODO
    }
    
    @FXML
    private void onResume(ActionEvent event) {
        paused = false;
        // TODO
    }
    
    @FXML
    private void onTogglePause(ActionEvent event) {
        if (paused) {
            onResume(event);
        } else {
            onPause(event);
        }
    }
    
    @FXML
    private void onResetCurrent(ActionEvent event) {
        // TODO
    }
    
    @FXML
    private void onResetSession(ActionEvent event) {
        // TODO
    }
    
    private void onQuit(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
    
}
