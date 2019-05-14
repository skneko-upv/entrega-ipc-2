/*
 * IPC - Entrega 2
 * Ingeniería Informática, UPV 2019
 * Por:
 *  Daniel Galán Pascual
 *  Alberto Baixauli Herráez
 */
package fitnesstimer.audio;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import marytts.util.data.audio.MonoAudioInputStream;
import marytts.util.data.audio.StereoAudioInputStream;


/**
 *
 * @author Dani
 */
public class AudioPlayer extends Thread {
    
    public enum Status { PLAYING, WAITING }
    
    public static final int MONO = 0;
    public static final int STEREO = 3;
    public static final int LEFT_ONLY = 1;
    public static final int RIGHT_ONLY = 2;
    
    private AudioInputStream input;
    private LineListener lineListener;
    private SourceDataLine line;
    private int outputMode;
    
    private Status status = Status.WAITING;
    private boolean exitRequested = false;
    private float gain = 1.0f;
    
    public AudioPlayer(
            AudioInputStream input, 
            SourceDataLine line, 
            LineListener lineListener, 
            int outputMode
    ) {
        this.input = input;
        this.line = line;
        this.lineListener = lineListener;
        this.outputMode = outputMode;
    }
    
    public AudioPlayer(
            File input, 
            SourceDataLine line, 
            LineListener lineListener, 
            int outputMode
    ) throws IOException, UnsupportedAudioFileException {
        this(
                AudioSystem.getAudioInputStream(input),
                line,
                lineListener,
                outputMode
        );
    }
    
    public void setInput(AudioInputStream input) {
        if (status == Status.PLAYING) {
            throw new IllegalStateException("Cannot set input while playing");
        }
        this.input = input;
    }
    
    public void abort() {
        if (line != null) {
            line.stop();
        }
        exitRequested = true;
    }
    
    public float getGain() {
        return gain;
    }
    
    public void setGain(float gain) {
        this.gain = gain;
        
        if (line != null && line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            ((FloatControl)line.getControl(FloatControl.Type.MASTER_GAIN))
                    .setValue((float)(20 * Math.log10(gain <= 0.0 ? 0.0 : gain)));
        }
    }
    
    public SourceDataLine getLine() {
        return line;
    }
    
    
    @Override
    public void run() {
        status = Status.PLAYING;
        AudioFormat format = input.getFormat();
    }
}
