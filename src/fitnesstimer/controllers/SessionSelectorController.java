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
    private ListView<SesionTipo> plansView;
    @FXML
    private Button nextBtn;

    private Grupo group;
    private ObservableList<SesionTipo> plans;

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
        // TODO
    }

    public void setup(Grupo group) {
        this.group = group;
    }

    @FXML
    private void onShowHistory(ActionEvent event) {
        // TODO
    }

    @FXML
    private void onAdd(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fitnesstimer/views/SessionForm.fxml"));
            Parent root = loader.load();

            SessionFormController controller = loader.<SessionFormController>getController();
            controller.setup(null, true);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.titleProperty().bind(i18n.getStringBinding("sessionForm.window.title"));
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    @FXML
    private void onPrev(ActionEvent event) {
        // TODO
    }

    @FXML
    private void onNext(ActionEvent event) {
        // TODO
    }

}
