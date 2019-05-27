/*
 * IPC - Entrega 2
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package fitnesstimer.component;

import javafx.animation.Timeline;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;

/**
 *
 * @author Dani
 */
public class Countdown implements Timer {

    IntegerProperty millis;
    IntegerProperty secs;
    IntegerProperty mins;
    IntegerProperty hours;
    
    BooleanProperty paused;
    BooleanProperty finished;
    
    Timeline time;
    
    public Countdown(int hours, int mins, int secs, int millis) {
        // TODO: init timeline
        setDuration(hours, mins, secs, millis);
    }
    
    @Override
    public void resume() {
        if (!isFinished()) {
            paused.set(false);
            time.play();
        }
    }
    
    @Override
    public void pause() {
        time.pause();
        paused.set(true);
    }
    
    @Override
    public void reset() {
        time.stop();
        paused.set(true);
        finished.set(false);
    }
    
    @Override
    public IntegerProperty millisProperty() {
        return millis;
    }
    
    @Override
    public IntegerProperty secsProperty() {
        return secs;
    }
    
    @Override
    public IntegerProperty minsProperty() {
        return mins;
    }
    
    @Override
    public IntegerProperty hoursProperty() {
        return hours;
    }
    
    @Override
    public final void setDuration(int hours, int mins, int secs, int millis) {
        pause();
        
        assert hours >= 0;
        assert mins >= 0 && mins < 60;
        assert secs >= 0 && secs < 60;
        assert millis >= 0 && millis < 1000;
        
        this.hours.set(hours);
        this.mins.set(mins);
        this.secs.set(secs);
        this.millis.set(millis);
        
        reset();
    }
    
    @Override
    public void finish() {
        pause();
        setDuration(0, 0, 0, 0);
        finished.set(true);
    }
    
    @Override
    public BooleanProperty finishedProperty() {
        return finished;
    }
    
    @Override
    public BooleanProperty pausedProperty() {
        return paused;
    }
    
    @Override
    public StringBinding getMillisBinding() {
        return makeStringBinding(millis);
    }
    
    @Override
    public StringBinding getSecsBinding() {
        return makeStringBinding(secs);
    }
    
    @Override
    public StringBinding getMinsBinding() {
        return makeStringBinding(mins);
    }
    
    @Override
    public StringBinding getHoursBinding() {
        return makeStringBinding(hours);
    }
    
    private StringBinding makeStringBinding(IntegerProperty value) {
        return new StringBinding() {
            
            { bind(value); }
            
            @Override
            public String computeValue() {
                return String.valueOf(value.get());
            }
        };
    }
}
