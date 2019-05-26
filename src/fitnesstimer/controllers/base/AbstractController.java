/*
 * IPC - Entrega 2
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package fitnesstimer.controllers.base;

import accesoBD.AccesoBD;
import fitnesstimer.i18n.I18n;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 *
 * @author Dani
 */
public abstract class AbstractController implements Initializable {
    
    protected final AccesoBD db = AccesoBD.getInstance();
    protected final I18n i18n = I18n.getInstance();
    
    protected void close(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
