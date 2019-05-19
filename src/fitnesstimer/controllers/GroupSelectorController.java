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
import javafx.stage.Stage;
import modelo.Grupo;

/**
 * FXML Controller class
 *
 * @author Dani
 */
public class GroupSelectorController extends AbstractController {

    @FXML
    private ListView<Grupo> groupsView;
    @FXML
    private Button addBtn;
    @FXML
    private Button editBtn;
    @FXML
    private Button showHistoryBtn;
    @FXML
    private Button nextBtn;
    
    private ObservableList<Grupo> groups;

    /**
     * Initializes the controller class.
     * @param url Not used
     * @param rb Not used
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        groups = FXCollections.observableArrayList(db.getGym().getGrupos());
        groupsView.setCellFactory(view -> new ListCell<Grupo>() {
            @Override
            protected void updateItem(Grupo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); }
                else { 
                    setText(String.format("[%s] %s", item.getCodigo(), item.getDescripcion()));
                }
            }
        });
        groupsView.setItems(groups);
        
        nextBtn.disableProperty().bind(
                Bindings.isNull(groupsView.getSelectionModel().selectedItemProperty())
        );
        editBtn.disableProperty().bind(
                Bindings.isNull(groupsView.getSelectionModel().selectedItemProperty())
        );
    }    

    @FXML
    private void onAdd(ActionEvent event) {
        // TODO
    }

    @FXML
    private void onEdit(ActionEvent event) {
        // TODO
    }

    @FXML
    private void onShowHistory(ActionEvent event) {
        // TODO
    }

    @FXML
    private void onNext(ActionEvent event) {
        Grupo selected = groupsView.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fitnesstimer/views/SessionSelector.fxml"));
            Parent root = loader.load();
            
            SessionSelectorController controller = loader.<SessionSelectorController>getController();
            controller.setup(selected);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene next = new Scene(root);
            stage.setTitle(i18n.getResources().getString("sessionSelector.window.title"));
            stage.setScene(next);
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
}
