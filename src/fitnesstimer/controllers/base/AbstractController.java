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
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 *
 * @author Dani
 */
public abstract class AbstractController implements Initializable {
    
    private enum Style {
        DAY, NIGHT
    }
    private static Style currentStyle = Style.DAY;
    
    @FXML
    protected RadioMenuItem dayStyle;
    @FXML
    protected RadioMenuItem nightStyle;
    @FXML
    protected ToggleGroup style;
    
    protected final AccesoBD db = AccesoBD.getInstance();
    protected final I18n i18n = I18n.getInstance();
    
    /**
     * Initializes the controller class.
     * @param url Not used
     * @param rb Not used
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        applyStyle();
        if (style != null) {
            style.selectedToggleProperty().addListener((_val, _oldVal, selected) -> changeStyle(selected));
        }
    }
    
    protected void close(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    protected void changeStyle(Toggle toggle) {
        // TODO
    }
    
    @FXML
    protected void applyStyle() {
        // TODO
    }
    
    @FXML
    private void onQuit(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
}
