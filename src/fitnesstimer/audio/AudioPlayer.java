/*
 * IPC - Entrega 2
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package fitnesstimer.audio;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.media.AudioClip;

/**
 *
 * @author Dani
 */
public class AudioPlayer {
    
    private static final double DEFAULT_VOLUME = 0.5;
    
    private static AudioPlayer instance;
    
    private final DoubleProperty volume;
    
    AudioClip begin = new AudioClip(
            getClass().getResource("/fitnesstimer/resources/sounds/begin.wav").toString()
    );
    AudioClip countdown5 = new AudioClip(
            getClass().getResource("/fitnesstimer/resources/sounds/countdown5.wav").toString()
    );
    AudioClip resume = new AudioClip(
            getClass().getResource("/fitnesstimer/resources/sounds/resume.mp3").toString()
    );
    AudioClip pause = new AudioClip(
            getClass().getResource("/fitnesstimer/resources/sounds/pause.wav").toString()
    );
    AudioClip end = new AudioClip(
            getClass().getResource("/fitnesstimer/resources/sounds/final.wav").toString()
    );
    
    private AudioClip current;
    
    public static AudioPlayer getInstance() {
        if (instance == null) {
            instance = new AudioPlayer(DEFAULT_VOLUME);
        }
        return instance;
    }
    
    private AudioPlayer(double volume) {
        this.volume = new SimpleDoubleProperty(volume);
    }
    
    public void setVolume(double volume) {
        assert volume >= 0.0 && volume <= 1.0;
        this.volume.set(volume);
    }
    
    public DoubleProperty volumeProperty() {
        return volume;
    }
    
    public double getVolume() {
        return volumeProperty().get();
    }
    
    public void play(AudioClip clip) {
        stop();
        current = clip;
        clip.volumeProperty().bind(volume);
        clip.play();
    }
    
    public void stop() {
        if (current != null) {
            current.stop();
        }
    }
    
    public void playBegin() {
        play(begin);
    }
    
    public void playCountdown5() {
        play(countdown5);
    }
    
    public void playResume() {
        play(resume);
    }
    
    public void playPause() {
        play(pause);
    }
    
    public void playFinal() {
        play(end);
    }
    
}
