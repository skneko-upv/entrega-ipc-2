/*
 * IPC - Entrega 2
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package fitnesstimer.controllers;

import fitnesstime.component.Countdown;
import fitnesstimer.controllers.base.AbstractController;
import java.net.URL;
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
    
    private static final short COUNTDOWN_RATE = 5;
    private Countdown countdown;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        countdown = new Countdown(0,2,0,0,COUNTDOWN_RATE);
        label.textProperty().bind(countdown.getStringBinding());
        
        countdown.finishedProperty().addListener((_val, _old, finished) -> {
            if (finished) System.out.println("RING RING");
        });
        // TODO
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        countdown.resume();
    }
    
    @FXML
    private void onNext(ActionEvent event) {
        // TODO
    }
    
    @FXML
    private void onPause(ActionEvent event) {
        countdown.pause();
        // TODO
    }
    
    @FXML
    private void onResume(ActionEvent event) {
        countdown.resume();
        // TODO
    }
    
    @FXML
    private void onTogglePause(ActionEvent event) {
        if (countdown.isPaused()) {
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
