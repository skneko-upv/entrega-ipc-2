/*
 * IPC - Entrega 2
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package fitnesstimer.controllers;

import fitnesstimer.controllers.base.AbstractController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import modelo.Grupo;
import modelo.Sesion;

/**
 * FXML Controller class
 *
 * @author Dani
 */
public class GroupHistoryController extends AbstractController {

    private static final int DEFAULT_SESSION_NUM = 10;

    @FXML
    private LineChart<String,Number> chart;
    @FXML
    private NumberAxis timeAxis;
    @FXML
    private CategoryAxis sessionAxis;
    @FXML
    private TextField sessionNumPicker;

    private XYChart.Series<String,Number> timePerSessionSeries;

    private Grupo group;
    private int sessionNum;

    /**
     * Initializes the controller class.
     * @param url Not used
     * @param rb Not used
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        
        sessionNum = DEFAULT_SESSION_NUM;
    }

    public void setup(Grupo group) {
        sessionNumPicker.setText(String.valueOf(sessionNum));

        this.group = group;

        chart.titleProperty().bind(i18n.getStringBinding("history.chart.title", group.getCodigo()));

        timePerSessionSeries = new XYChart.Series<>();
        timePerSessionSeries.nameProperty().bind(i18n.getStringBinding("history.timeSeries.name"));

        chart.getData().add(timePerSessionSeries);

        sessionNumPicker.textProperty().addListener((_val, _oldVal, newVal) -> {
            try {
                sessionNum = Integer.parseInt(newVal);
                drawChart();
            } catch (NumberFormatException e) {}
        });

        drawChart();
    }

    private void drawChart() {
        timePerSessionSeries.getData().clear();
        group.getSesiones().sort((Sesion a, Sesion b) -> -(a.getFecha().compareTo(b.getFecha())));
        for (int i = 0; i < sessionNum && i < group.getSesiones().size(); i++) {
            Sesion s = group.getSesiones().get(i);
            timePerSessionSeries.getData().add(
                    new XYChart.Data<>(
                            s.getFecha().toString(),
                            (double) s.getDuracion().getSeconds() / 60
                    )
            );
        }
    }

    @FXML
    private void onClose(ActionEvent event) {
        close(event);
    }
}
