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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import modelo.SesionTipo;

/**
 * FXML Controller class
 *
 * @author Dani
 */
public class SessionFormController extends AbstractFormController<SesionTipo> {

    @FXML
    private TextField tracksField;
    @FXML
    private Label tracksErrorLabel;
    @FXML
    private TextField exercisesField;
    @FXML
    private Label exercisesErrorLabel;
    @FXML
    private TextField exerciseTimeField;
    @FXML
    private Label exerciseTimeErrorLabel;
    @FXML
    private TextField warmupTimeField;
    @FXML
    private Label warmupTimeErrorLabel;
    @FXML
    private TextField exerciseRestTimeField;
    @FXML
    private Label exerciseRestTimeErrorLabel;
    @FXML
    private TextField trackRestTimeField;
    @FXML
    private Label trackRestTimeErrorLabel;

    private ValidatedIntField tracks;
    private ValidatedIntField exercises;
    private ValidatedIntField exerciseTime;
    private ValidatedIntField warmupTime;
    private ValidatedIntField exerciseRestTime;
    private ValidatedIntField trackRestTime;

    /**
     * Initializes the controller class.
     * @param url Not used
     * @param rb Not used
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @Override
    public void setup(SesionTipo prefill, boolean editMode) {
        tracks = new ValidatedIntField(tracksField, tracksErrorLabel);
        exercises = new ValidatedIntField(exercisesField, exercisesErrorLabel);
        exerciseTime = new ValidatedIntField(exerciseTimeField, exerciseTimeErrorLabel);
        warmupTime = new ValidatedIntField(warmupTimeField, warmupTimeErrorLabel);
        exerciseRestTime = new ValidatedIntField(exerciseRestTimeField, exerciseRestTimeErrorLabel);
        trackRestTime = new ValidatedIntField(trackRestTimeField, trackRestTimeErrorLabel);

        super.setup(prefill, editMode);
    }

    @Override
    protected boolean validateAll() {
        return tracks.validate()
                && exercises.validate()
                && exerciseTime.validate()
                && warmupTime.validate()
                && exerciseRestTime.validate()
                && trackRestTime.validate();
    }

    @Override
    protected void prefill() {
        tracks.setValue(prefill.getNum_circuitos());
        exercises.setValue(prefill.getNum_ejercicios());
        exerciseTime.setValue(prefill.getT_ejercicio());
        warmupTime.setValue(prefill.getT_calentamiento());
        exerciseRestTime.setValue(prefill.getD_ejercicio());
        trackRestTime.setValue(prefill.getD_circuito());
    }

    @Override
    public void onSaveValidated(ActionEvent e) {
        SesionTipo plan = new SesionTipo();

        plan.setCodigo(String.format("P%d", db.getGym().getTiposSesion().size() + 1));
        plan.setNum_circuitos(tracks.getValue());
        plan.setNum_ejercicios(exercises.getValue());
        plan.setT_ejercicio(exerciseTime.getValue());
        plan.setT_calentamiento(warmupTime.getValue());
        plan.setD_ejercicio(exerciseRestTime.getValue());
        plan.setD_circuito(trackRestTime.getValue());

        db.getGym().getTiposSesion().add(plan);
        db.salvar();
    }
    
    @Override
    public void setUneditableAll() {
        tracks.setEditable(false);
        exercises.setEditable(false);
        exerciseTime.setEditable(false);
        warmupTime.setEditable(false);
        exerciseRestTime.setEditable(false);
        trackRestTime.setEditable(false);
    }
    
    @Override
    public void setClearAll() {
        tracks.setClear();
        exercises.setClear();
        exerciseTime.setClear();
        warmupTime.setClear();
        exerciseRestTime.setClear();
        trackRestTime.setClear();
    }

    class ValidatedIntField extends ValidatedTextField {
        public boolean isNumeric(String s) {
            try {
                Integer.parseInt(s);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        
        public ValidatedIntField(TextField field, Label errorLabel) {
            super(field, errorLabel);
            addValidator(value -> {
                if (value.equals("")) {
                    return i18n.getResources().getString("generic.form.error.empty");
                } else {
                    return null;
                }
            });
            addValidator(value -> {
                if (!isNumeric(value)) {
                    return i18n.getResources().getString("generic.form.error.notNumber");
                }
                if (Integer.parseInt(value) < 0) {
                    return i18n.getResources().getString("generic.form.error.notPositive");
                }
                
                return null;
            });
        }

        public int getValue() {
            return Integer.parseInt(getText());
        }

        public void setValue(int value) {
            setText(String.valueOf(value));
        }
    }
}
