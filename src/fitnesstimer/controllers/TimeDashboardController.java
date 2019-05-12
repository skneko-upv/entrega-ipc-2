/*
 * IPC - Entrega 2
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package fitnesstimer.controllers;

import fitnesstimer.i18n.I18n;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Dani
 */
public class TimeDashboardController implements Initializable {
    
    private final I18n i18n = I18n.getInstance();

    @FXML
    private Button button;
    @FXML
    private Label label;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        label.textProperty().bind(i18n.getStringBinding("app.title"));
        // TODO
    }    

    @FXML
    private void handleButtonAction(ActionEvent event) {
        i18n.setLocale(Locale.ITALY);
    }
    
}
