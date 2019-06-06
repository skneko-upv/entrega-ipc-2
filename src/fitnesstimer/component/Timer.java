/*
 * IPC - Entrega 2
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package fitnesstimer.component;

import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;

/**
 *
 * @author neko
 */
public interface Timer {
    void resume();
    void pause();
    void reset();

    IntegerProperty millisProperty();
    IntegerBinding getMillisBinding();
    default int getMillis() {
        return millisProperty().get();
    }

    IntegerProperty secsProperty();
    IntegerBinding getSecsBinding();
    default int getSecs() {
        return secsProperty().get();
    }

    IntegerProperty minsProperty();
    IntegerBinding getMinsBinding();
    default int getMins() {
        return minsProperty().get();
    }

    IntegerProperty hoursProperty();
    IntegerBinding getHoursBinding();
    default int getHours() {
        return hoursProperty().get();
    }

    void setDuration(int hours, int mins, int secs, int millis);

    void finish();

    BooleanProperty finishedProperty();
    default boolean isFinished() {
        return finishedProperty().get();
    }

    BooleanProperty pausedProperty();
    default boolean isPaused() {
        return pausedProperty().get();
    }
}
