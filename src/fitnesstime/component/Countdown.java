/*
 * IPC - Entrega 2
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package fitnesstime.component;

import java.util.concurrent.TimeUnit;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.util.Duration;

/**
 *
 * @author Dani
 */
public class Countdown {
    
    private final short rate;
    private final Timeline timeline;
    private boolean paused;
    private BooleanProperty finished;
    
    private LongProperty value;
    
    public Countdown(
            int hours, 
            int minutes, 
            int seconds, 
            int millis, 
            short updateRate
    ) {
        this(toMillis(hours, minutes, seconds, millis), updateRate);
    }
    
    public Countdown(long millis, short updateRate) {
        this.rate = updateRate;
        this.value = new SimpleLongProperty(millis);
        this.paused = true;
        this.finished = new SimpleBooleanProperty(false);
        
        timeline = new Timeline(
                new KeyFrame(Duration.millis(rate), (e) -> { 
                    decrement();
                })
        );
        timeline.setAutoReverse(false);
        timeline.setCycleCount(Timeline.INDEFINITE);
    }
    
    public void resume() {
        if (!finished.get()) {
            timeline.play();
        }
    }
    
    public void pause() {
        paused = true;
        timeline.pause();
    }
    
    public void reset() {
        paused = false;
        timeline.stop();
    }
    
    public void toggle() {
        if (paused) {
            resume();
        } else {
            pause();
        }
    }
    
    public boolean isPaused() {
        return paused;
    }
    
    public BooleanProperty finishedProperty() {
        return finished;
    }
    
    public boolean isFinished() {
        return finishedProperty().get();
    }
    
    public void setToZero() {
        finished.set(true);
        timeline.pause();
        value.set(0);
    }
    
    public StringBinding getStringBinding() {
        return new StringBinding() {
            
            { bind(value); }
            
            @Override
            public String computeValue() {
                return getStringValue();
            }
        };
    }
    
    public String getStringValue() {
        long millis = value.get();
        long hrs = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hrs);
        long mins = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(mins);
        long secs = TimeUnit.MILLISECONDS.toSeconds(millis);
        millis -= TimeUnit.SECONDS.toMillis(secs);
        
        return String.format("%02d:%02d:%02d.%03d", hrs, mins, secs, millis);
    }
    
    public LongProperty valueProperty() {
        return value;
    }
    
    public long getValue() {
        return valueProperty().get();
    }
    
    private static long toMillis(int hrs, int mins, int secs, int millis) {
        return millis 
                + secs * 1000
                + mins * 60 * 1000
                + hrs * 60 * 60 * 1000;
    }
    
    private void decrement() {
        long current = value.get();
        current -= rate;
        if (current <= 0) {
            setToZero();
        } else {
            value.set(current);
        }
    }
}
