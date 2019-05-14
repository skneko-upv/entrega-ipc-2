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
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import modelo.SesionTipo;

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
    private Countdown timer;
    
    private enum ActivityKind { 
        EXERCISE_RUN, EXERCISE_REST, TRACK_REST, FINISHED 
    }
    
    private SesionTipo plan;
    private int track;
    private int exercise;
    private ActivityKind phase;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        timer = new Countdown(0,2,0,0,COUNTDOWN_RATE);
        label.textProperty().bind(timer.getStringBinding());
        
        timer.finishedProperty().addListener((_val, _old, finished) -> {
            if (finished) System.out.println("RING RING");
        });
        // TODO
    }
    
    private void nextActivity() {
        prepareNextActivity();
        setupScene();
    }
    
    private void prepareNextActivity() {
        switch (phase) {
            case EXERCISE_RUN:
                phase = ActivityKind.EXERCISE_REST;
                break;
                
            case EXERCISE_REST:
                if (exercise >= plan.getNum_ejercicios()) {  // if last exercise in track
                    phase = ActivityKind.TRACK_REST;
                    return;
                }
                
                phase = ActivityKind.EXERCISE_RUN;
                exercise++;
                break;
                
            case TRACK_REST:
                if (track >= plan.getNum_circuitos()) {  // if last track in session
                    phase = ActivityKind.FINISHED;
                    return;
                }
                
                phase = ActivityKind.EXERCISE_RUN;
                track++;
                exercise = 1;
                break;
                
            case FINISHED:
            default:
                {}
        }
    }
    
    private void setupScene() {
        int duration;
        String descKey;
        
        switch (phase) {
            case EXERCISE_RUN:
                duration = plan.getT_ejercicio();
                descKey = "phase.exerciseRun";
                break;
            case EXERCISE_REST:
                duration = plan.getD_ejercicio();
                descKey = "phase.exerciseRest";
                break;
            case TRACK_REST:
                duration = plan.getD_circuito();
                descKey = "phase.trackRest";
                break;
            case FINISHED:
            default:
                i18n.bind(label, "phase.finished");
                timer.setToZero();
                return;
        }
        
        i18n.bind(label, descKey, track, exercise);
        timer = new Countdown(
                TimeUnit.SECONDS.toMillis(duration),
                COUNTDOWN_RATE
        );
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        timer.resume();
    }
    
    @FXML
    private void onNext(ActionEvent event) {
        // TODO
    }
    
    @FXML
    private void onPause(ActionEvent event) {
        timer.pause();
        // TODO
    }
    
    @FXML
    private void onResume(ActionEvent event) {
        timer.resume();
        // TODO
    }
    
    @FXML
    private void onTogglePause(ActionEvent event) {
        if (timer.isPaused()) {
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
