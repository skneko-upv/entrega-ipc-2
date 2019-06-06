/*
 * IPC - Entrega 2
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package fitnesstimer.controllers;

import fitnesstimer.component.Countdown;
import fitnesstimer.component.Timer;
import fitnesstimer.controllers.base.AbstractController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import modelo.SesionTipo;

/**
 * FXML Controller class
 *
 * @author Dani
 */
public class TimeDashboardController extends AbstractController {

    @FXML
    private Button restartExerciseBtn;
    @FXML
    private Button resumeBtn;
    @FXML
    private Button pauseBtn;
    @FXML
    private Button nextExerciseBtn;
    @FXML
    private Label statusLabel;
    @FXML
    private Text trackNumber;
    @FXML
    private Text exerciseNumber;
    @FXML
    private Text groupNumber;
    @FXML
    private Text minutes;
    @FXML
    private Text seconds;
    @FXML
    private Text millis;

    private Timer timer;

    private enum ActivityKind {
        WARM_UP, EXERCISE_RUN, EXERCISE_REST, TRACK_REST, FINISHED
    }

    private SesionTipo plan;
    private int track;
    private int exercise;
    private ActivityKind phase;
    private boolean sessionFinished;

    /**
     * Initializes the controller class.
     * @param url Not used
     * @param rb Not used
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        timer = new Countdown(0, 0, 15, 0);
        timer.finishedProperty().addListener((_val, wasZero, isNowZero) -> {
            if (!sessionFinished && !wasZero && isNowZero) {    // timer just reached zero
                nextActivity();
                timer.resume();
                // TODO: warnings
            }
        });

        minutes.textProperty().bind(Bindings.format("%02d", timer.getMinsBinding()));
        seconds.textProperty().bind(Bindings.format("%02d", timer.getSecsBinding()));
        millis.textProperty().bind(Bindings.format("%03d", timer.getMillisBinding()));

        // TODO: launch group selector
        this.plan = db.getGym().getTiposSesion().get(2);
        System.out.println(String.format(
                            "[%s] %dx%d %ds",
                            plan.getCodigo(),
                            plan.getNum_circuitos(),
                            plan.getNum_ejercicios(),
                            plan.getT_ejercicio()
                    ));

        setupSession();
    }

    private void nextActivity() {
        timer.pause();
        prepareNextActivity();
        setupScene();
    }

    private void prepareNextActivity() {
        switch (phase) {
            case WARM_UP:
                track = 1;
                exercise = 1;
                phase = ActivityKind.EXERCISE_RUN;
                break;

            case EXERCISE_RUN:
                if (exercise >= plan.getNum_ejercicios()) {  // if last exercise in track
                    phase = ActivityKind.TRACK_REST;
                    return;
                }
                
                phase = ActivityKind.EXERCISE_REST;
                break;

            case EXERCISE_REST:
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
            case WARM_UP:
                duration = plan.getT_calentamiento();
                descKey = "phase.warmup";
                break;
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
                sessionFinished = true;
                i18n.bind(statusLabel, "phase.finished");
                timer.finish();
                return;
        }

        i18n.bind(statusLabel, descKey, track, exercise);
        trackNumber.setText(String.valueOf(track));
        exerciseNumber.setText(String.valueOf(exercise));
        // TODO: hide numbers during warmup
        
        int h = duration / 3600;
        int m = (duration % 3600) / 60;
        int s = duration % 60;
        timer.setDuration(h, m, s, 0);
        timer.reset();
    }

    private void setupSession() {
        this.sessionFinished = false;
        track = 0;
        exercise = 0;
        phase = ActivityKind.WARM_UP;
        setupScene();
    }

    @FXML
    private void onNext(ActionEvent event) {
        if (!sessionFinished) {
            nextActivity();
        }
    }

    @FXML
    private void onPause(ActionEvent event) {
        if (!sessionFinished) {
            timer.pause();
        }
    }

    @FXML
    private void onResume(ActionEvent event) {
        if (!sessionFinished) {
            timer.resume();
        }
    }

    private void onTogglePause(ActionEvent event) {
        if (timer.isPaused()) {
            onResume(event);
        } else {
            onPause(event);
        }
    }

    @FXML
    private void onResetCurrent(ActionEvent event) {
        if (!sessionFinished) {
            timer.reset();
            setupScene();
        }
    }

    private void onResetSession(ActionEvent event) {
        if (!sessionFinished) timer.pause();
        setupSession();
    }

    private void onQuit(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

}
