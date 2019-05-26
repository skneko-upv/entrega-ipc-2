/*
 * IPC - Entrega 2
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package fitnesstimer.controllers.base;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 *
 * @author Dani
 * @param <T> The type of the object created, shown or edited by this form.
 */
public abstract class AbstractFormController<T> extends AbstractController {
    
    protected T prefill;
    protected boolean editMode;
    
    @FXML
    Button saveBtn;
    @FXML
    Button cancelBtn;
    
    protected void setup(T prefill, boolean editMode) {
        this.editMode = editMode;
        this.prefill = prefill;
        if (this.prefill != null) {
            prefill();
        }
        if (!this.editMode) {
            setUneditableAll();
            saveBtn.setVisible(false);
            i18n.bind(cancelBtn, "generic.ok");
        }
    }
    
    protected abstract void setClearAll();

    protected abstract void setUneditableAll();

    protected abstract void prefill();

    protected abstract boolean validateAll();

    protected abstract void onSaveValidated(ActionEvent event);

    @FXML
    private void onSave(ActionEvent event) {
        setClearAll();
        if (validateAll()) {
            onSaveValidated(event);
            close(event);
        }
    }

    @FXML
    protected void onCancel(ActionEvent event) {
        close(event);
    }
    
}
