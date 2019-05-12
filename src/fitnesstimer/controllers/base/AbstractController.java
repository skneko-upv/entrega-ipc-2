/*
 * IPC - Entrega 2
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package fitnesstimer.controllers.base;

import fitnesstimer.i18n.I18n;
import javafx.fxml.Initializable;

/**
 *
 * @author Dani
 */
public abstract class AbstractController implements Initializable {
    
    protected final I18n i18n = I18n.getInstance();
    
}
