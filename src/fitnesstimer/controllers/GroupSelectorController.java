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
    
    private TimeDashboardController dashboard;

    /**
     * Initializes the controller class.
     * @param url Not used
     * @param rb Not used
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        
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
    
    public void setup(TimeDashboardController dashboard) {
        this.dashboard = dashboard;
    }

    @FXML
    private void onAdd(ActionEvent event) {
        launchForm(event, false);
    }

    @FXML
    private void onEdit(ActionEvent event) {
        launchForm(event, true);
    }

    @FXML
    private void onShowHistory(ActionEvent event) {
        Grupo selected = groupsView.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fitnesstimer/views/GroupHistory.fxml"));
            Parent root = loader.load();

            GroupHistoryController controller = loader.<GroupHistoryController>getController();
            controller.setup(selected);

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
    private void onNext(ActionEvent event) {
        Grupo selected = groupsView.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fitnesstimer/views/SessionSelector.fxml"));
            Parent root = loader.load();
            
            SessionSelectorController controller = loader.<SessionSelectorController>getController();
            controller.setup(dashboard, selected);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene next = new Scene(root);
            stage.titleProperty().bind(i18n.getStringBinding("sessionSelector.window.title"));
            stage.setScene(next);
        } catch (IOException e) {
            System.err.println("Cannot switch to next scene: " + e);
        }
    }
    
    private void launchForm(ActionEvent event, boolean editing) {
        Grupo prefill = null;
        int index = -1;
        if (editing) {
            prefill = groupsView.getSelectionModel().getSelectedItem();
            if (prefill == null) return;
            index = groupsView.getSelectionModel().getSelectedIndex();
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fitnesstimer/views/GroupForm.fxml"));
            Parent root = loader.load();
            
            GroupFormController controller = loader.<GroupFormController>getController();
            controller.setup(prefill, true, groups, index);
            
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.titleProperty().bind(i18n.getStringBinding("groupForm.window.title"));
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("Cannot launch form: " + e);
        }
    }
}
