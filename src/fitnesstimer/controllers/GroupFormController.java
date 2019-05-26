/*
 * IPC - Entrega 2
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package fitnesstimer.controllers;

import fitnesstimer.component.ValidatedTextField;
import fitnesstimer.controllers.base.AbstractFormController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import modelo.Grupo;

/**
 * FXML Controller class
 *
 * @author Dani
 */
public class GroupFormController extends AbstractFormController<Grupo> {
    
    public static final String ID_PATTERN = "G%d";

    @FXML
    private TextField codeField;
    @FXML
    private Label codeErrorLabel;
    @FXML
    private TextArea descriptionField;
    @FXML
    private Label descriptionErrorLabel;
    
    private ValidatedTextField description;
    
    private ObservableList<Grupo> groups;
    private int editIndex;

    /**
     * Initializes the controller class.
     * @param url Not used
     * @param rb Not used
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {}
    
    public void setup(Grupo prefill, boolean editMode, ObservableList<Grupo> groups, int editIndex) {
        this.groups = groups;
        this.editIndex = editIndex;

        description = new ValidatedTextField(descriptionField, descriptionErrorLabel);

        super.setup(prefill, editMode);
    }
    
    @Override
    protected void onSaveValidated(ActionEvent e) {
        Grupo group = new Grupo();
        
        //TODO: group.setDefaultTipoSesion();
        group.setDescripcion(description.getText());
        
        if (editIndex < 0) {
            group.setCodigo(String.format(ID_PATTERN, groups.size() + 1));
            groups.add(group);
        } else {
            group.setCodigo(groups.get(editIndex).getCodigo());
            groups.set(editIndex, group);
        }
    }
    
    @Override
    protected boolean validateAll() {
        return description.validate();
    }
    
    @Override
    protected void prefill() {
        description.setText(prefill.getDescripcion());
    }
    
    @Override
    protected void setUneditableAll() {
        description.setEditable(false);
    }
    
    @Override
    protected void setClearAll() {
        description.setClear();
    }
    
}
