/*
 * IPC - Entrega 2
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package fitnesstimer.controllers;

import fitnesstimer.controllers.base.AbstractController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Grupo;
import modelo.SesionTipo;

/**
 * FXML Controller class
 *
 * @author Dani
 */
public class SessionSelectorController extends AbstractController {
    
    @FXML
    private Button prevBtn;
    @FXML
    private Button nextBtn;
    @FXML
    private ListView<SesionTipo> plansView;
    @FXML
    private TextField tracksNumber;
    @FXML
    private TextField exerciseNumber;
    @FXML
    private TextField exerciseTime;
    @FXML
    private TextField warmUpTime;
    @FXML
    private TextField restTimeExercise;
    @FXML
    private TextField restTimeTrack;

    private Grupo group;
    private ObservableList<SesionTipo> plans;
    private TimeDashboardController dashboard;
    
    /**
     * Initializes the controller class.
     * @param url Not used
     * @param rb Not used
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        plans = FXCollections.observableArrayList(db.getGym().getTiposSesion());
        plansView.setItems(plans);
        plansView.setCellFactory(view -> new ListCell<SesionTipo>() {
            @Override
            protected void updateItem(SesionTipo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); }
                else {
                    setText(String.format(
                            "[%s] %dx%d %ds",
                            item.getCodigo(),
                            item.getNum_circuitos(),
                            item.getNum_ejercicios(),
                            item.getT_ejercicio()
                    ));
                }
            }
        });
        nextBtn.disableProperty().bind(
                Bindings.isNull(plansView.getSelectionModel().selectedItemProperty())
        );
        
        plansView.getSelectionModel().selectedItemProperty().addListener((_val, _oldVal, selected) -> {
            if (selected != null) {
                tracksNumber.setText(String.valueOf(selected.getNum_circuitos()));
                exerciseNumber.setText(String.valueOf(selected.getNum_ejercicios()));
                exerciseTime.setText(String.valueOf(selected.getT_ejercicio()));
                warmUpTime.setText(String.valueOf(selected.getT_calentamiento()));
                restTimeExercise.setText(String.valueOf(selected.getD_ejercicio()));
                restTimeTrack.setText(String.valueOf(selected.getD_circuito()));
            }
        });
        
        try {
            plansView.getSelectionModel().select(group.getDefaultTipoSesion());
        } catch (Exception e) {
            System.err.println("Cannot select default plan of selected group: " + e);
        }
    }

    public void setup(TimeDashboardController dashboard, Grupo group) {
        this.dashboard = dashboard;
        this.group = group;
    }

    @FXML
    private void onShowHistory(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fitnesstimer/views/GroupHistory.fxml"));
            Parent root = loader.load();

            GroupHistoryController controller = loader.<GroupHistoryController>getController();
            controller.setup(group);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.titleProperty().bind(i18n.getStringBinding("groupHistory.window.title"));
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("Cannot launch group history: " + e);
        }
    }

    @FXML
    private void onAdd(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fitnesstimer/views/SessionForm.fxml"));
            Parent root = loader.load();

            SessionFormController controller = loader.<SessionFormController>getController();
            controller.setup(null, true, plans);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.titleProperty().bind(i18n.getStringBinding("sessionForm.window.title"));
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("Cannot launch form: " + e);
        }
        
        plansView.getSelectionModel().select(plans.size() - 1);
    }

    @FXML
    private void onPrev(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fitnesstimer/views/GroupSelector.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene next = new Scene(root);
            stage.titleProperty().bind(i18n.getStringBinding("groupSelector.window.title"));
            stage.setScene(next);
        } catch (IOException e) {
            System.err.println("Cannot switch to previous scene: " + e);
        }
    }

    @FXML
    private void onNext(ActionEvent event) {
        SesionTipo selected = plansView.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        
        dashboard.setupSession(group, selected);
        
        close(event);
    }

}
