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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.util.StringConverter;
import modelo.Grupo;
import modelo.SesionTipo;

/**
 * FXML Controller class
 *
 * @author Dani
 */
public class GroupFormController extends AbstractFormController<Grupo> {
    
    public static final String ID_PATTERN = "G%d";

    @FXML
    private Label codeErrorLabel;
    @FXML
    private TextArea descriptionField;
    @FXML
    private Label descriptionErrorLabel;
    
    private ValidatedTextField description;
    
    private ObservableList<Grupo> groups;
    private int editIndex;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private ChoiceBox<SesionTipo> sessionChoiceBox;

    /**
     * Initializes the controller class.
     * @param url Not used
     * @param rb Not used
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
    }
    
    public void setup(Grupo prefill, boolean editMode, ObservableList<Grupo> groups, int editIndex) {
        this.groups = groups;
        this.editIndex = editIndex;

        description = new ValidatedTextField(descriptionField, descriptionErrorLabel);
        
        sessionChoiceBox.setItems(FXCollections.observableArrayList(
                db.getGym().getTiposSesion()
        ));
        
        sessionChoiceBox.setConverter(new StringConverter<SesionTipo>() {
            @Override
            public SesionTipo fromString(String s) {
                final String code = s.substring(1, s.indexOf("]"));
                return db.getGym().getTiposSesion()
                        .stream().filter(p -> p.getCodigo().equals(code)).findFirst().get();
            }
            
            @Override
            public String toString(SesionTipo p) {
                return String.format("[%s] %dx%d %ds", 
                        p.getCodigo(), p.getNum_circuitos(), 
                        p.getNum_ejercicios(), p.getT_ejercicio());
            }
        });

        super.setup(prefill, editMode);
    }
    
    @Override
    protected void onSaveValidated(ActionEvent e) {
        Grupo group = new Grupo();
        
        group.setDefaultTipoSesion(sessionChoiceBox.getValue());
        group.setDescripcion(description.getText());
        
        if (editIndex < 0) {
            group.setCodigo(String.format(ID_PATTERN, groups.size() + 1));
            groups.add(group);
        } else {
            group.setCodigo(groups.get(editIndex).getCodigo());
            groups.set(editIndex, group);
        }
        
        db.salvar();
    }
    
    @Override
    protected boolean validateAll() {
        return description.validate();
    }
    
    @Override
    protected void prefill() {
        description.setText(prefill.getDescripcion());
        sessionChoiceBox.setValue(prefill.getDefaultTipoSesion());
    }
    
    @Override
    protected void setUneditableAll() {
        description.setEditable(false);
        sessionChoiceBox.setDisable(true);
    }
    
    @Override
    protected void setClearAll() {
        description.setClear();
    }
    
}
