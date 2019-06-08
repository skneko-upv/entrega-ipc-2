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
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Grupo;
import modelo.Sesion;
import modelo.SesionTipo;

/**
 * FXML Controller class
 *
 * @author Dani
 */
public class TimeDashboardController extends AbstractController {

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
    @FXML
    private Slider volumeSlider;
    @FXML
    private ImageView muteImage;
    @FXML
    private Text volumeNumber;
    @FXML
    private HBox timerBox;
    @FXML
    private TextFlow timerFlow;

    private Stage ownStage;

    private Timer timer;
    private LocalDateTime startTime;
    private long startEpoch;
    
    private DoubleProperty volume;


    private Grupo group;
    private SesionTipo plan;
    private int track;
    private int exercise;
    private ActivityKind phase;
    private boolean firstPlay;
    private boolean sessionFinished;

    /**
     * Initializes the controller class.
     * @param url Not used
     * @param rb Not used
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        
        timer = new Countdown(0, 0, 15, 0);
        timer.finishedProperty().addListener((_val, wasZero, isNowZero) -> {
            if (!sessionFinished && !wasZero && isNowZero) {    // timer just reached zero
                nextActivity();
                timer.resume();
            }
        });
        timer.secsProperty().addListener((_val, oldVal, newVal) -> {
            if (!timer.isPaused() && oldVal.equals(5) && !newVal.equals(5)) { // just passed 5 secs
                audio.playCountdown5();
                timerFlow.getChildren().forEach((child) -> {
                    ((Shape)child).setFill(Color.RED);
                });
            }
        });

        minutes.textProperty().bind(Bindings.format("%02d", timer.getMinsBinding()));
        seconds.textProperty().bind(Bindings.format("%02d", timer.getSecsBinding()));
        millis.textProperty().bind(Bindings.format("%03d", timer.getMillisBinding()));
        
        volume = new SimpleDoubleProperty(volumeSlider.getValue());
        volume.bind(volumeSlider.valueProperty());
        volumeNumber.textProperty().bind(Bindings.format("%3.0f", volume));
        audio.volumeProperty().bind(volume.divide(100));
        
        muteImage.imageProperty().bind(
                Bindings.when(volumeSlider.valueProperty().isEqualTo(0.0, 0.5))
                    .then(new Image("fitnesstimer/resources/images/mute.png"))
                    .otherwise(new Image("fitnesstimer/resources/images/speaker.png"))
        );
    }

    public void setup(Stage stage) {
        this.ownStage = stage;
        launchSessionSetup();
    }

    private void launchSessionSetup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fitnesstimer/views/GroupSelector.fxml"));
            Parent root = loader.load();
            
            GroupSelectorController controller = loader.<GroupSelectorController>getController();
            controller.setup(this);

            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            newStage.titleProperty().bind(i18n.getStringBinding("groupSelector.window.title"));
            newStage.setScene(scene);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.initOwner(ownStage);
            newStage.showAndWait();
        } catch (IOException e) {
        }
        
        if (plan == null) { System.exit(0); }
    }
    
    public void setupSession(Grupo group, SesionTipo plan) {
        this.group = group;
        this.plan = plan;
        
        groupNumber.setText(group.getCodigo());
        
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
                timerBox.setStyle("-fx-border-color: gold; -fx-border-width: 4px;");
                break;
            case EXERCISE_RUN:
                duration = plan.getT_ejercicio();
                descKey = "phase.exerciseRun";
                timerBox.setStyle("-fx-border-color: orange; -fx-border-width: 4px;");
                break;
            case EXERCISE_REST:
                duration = plan.getD_ejercicio();
                descKey = "phase.exerciseRest";
                timerBox.setStyle("-fx-border-color: cyan; -fx-border-width: 1px;");
                break;
            case TRACK_REST:
                duration = plan.getD_circuito();
                descKey = "phase.trackRest";
                timerBox.setStyle("-fx-border-color: cyan; -fx-border-width: 1px;");
                break;
            case FINISHED:
            default:
                timerBox.setStyle("-fx-border-color: green; -fx-border-width: 4px;");
                timerFlow.getChildren().forEach((child) -> {
                    ((Shape)child).setFill(Color.BLACK);
                });
                
                i18n.bind(statusLabel, "phase.finished");
                if (!sessionFinished) {
                    sessionFinished = true;
                    timer.finish();
                    audio.playFinal();
                    
                    int i = db.getGym().getGrupos().indexOf(group);
                    if (i >= 0 && i < db.getGym().getGrupos().size()) {
                        Sesion s = new Sesion();
                        s.setDuracion(Duration.ofMillis(System.currentTimeMillis() - startEpoch));
                        s.setFecha(startTime);
                        s.setTipo(plan);
                        db.getGym().getGrupos().get(i).getSesiones().add(s);
                        db.salvar();
                    }
                }
                return;
        }

        i18n.bind(statusLabel, descKey, track, exercise);
        trackNumber.setText(String.valueOf(track));
        exerciseNumber.setText(String.valueOf(exercise));

        int h = duration / 3600;
        int m = (duration % 3600) / 60;
        int s = duration % 60;
        timer.setDuration(h, m, s, 0);
        
        timerFlow.getChildren().forEach((child) -> {
            ((Shape)child).setFill(Color.BLACK);
        });
        
        timer.reset();
    }

    private void setupSession() {
        this.startTime = LocalDateTime.now();
        this.startEpoch = System.currentTimeMillis();
        this.sessionFinished = false;
        track = 0;
        exercise = 0;
        phase = ActivityKind.WARM_UP;
        firstPlay = true;
        setupScene();
    }
    
    @FXML
    private void onFinish(ActionEvent event) {
        if (!sessionFinished) {
            timer.pause();
        }
        this.launchSessionSetup();
    }

    @FXML
    private void onMute(MouseEvent event) {
        if (volumeSlider.valueProperty().isEqualTo(0.0, 0.5).get()) {
            volumeSlider.setValue(50.0);
        } else {
            volumeSlider.setValue(0.0);
        }
        
    }

    @FXML
    private void onNext(ActionEvent event) {
        audio.stop();
        if (!sessionFinished) {
            nextActivity();
        }
    }

    private void onPause(ActionEvent event) {
        if (!sessionFinished) {
            audio.playPause();
            timer.pause();
        }
    }

    private void onResume(ActionEvent event) {
        if (!sessionFinished) {
            if (firstPlay) {
                audio.playBegin();
                firstPlay = false;
            } else {
                audio.playResume();
            }
            timer.resume();
        }
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
        audio.stop();
        if (!sessionFinished) {
            timer.reset();
            setupScene();
        }
    }

    @FXML
    private void onResetSession(ActionEvent event) {
        audio.stop();
        if (!sessionFinished) {
            timer.pause();
        }
        setupSession();
    }

    @FXML
    private void onQuit(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
    private enum ActivityKind {
        WARM_UP, EXERCISE_RUN, EXERCISE_REST, TRACK_REST, FINISHED
    }
    
}
