/*
 * IPC - Entrega 2
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package fitnesstimer.application;

import fitnesstimer.controllers.TimeDashboardController;
import fitnesstimer.i18n.I18n;
import java.util.Locale;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FitnessTimer extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        I18n.createInstance(Locale.getDefault());
        Application.setUserAgentStylesheet(STYLESHEET_MODENA);
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fitnesstimer/views/TimeDashboard.fxml"));
        Parent root = loader.load();
        
        TimeDashboardController controller = loader.<TimeDashboardController>getController();
        controller.setup(stage);
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
