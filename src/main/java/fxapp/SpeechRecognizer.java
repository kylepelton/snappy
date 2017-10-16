package fxapp;

import java.io.IOException;
import java.util.Observable;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;

/**
 *  SpeechRecognizer is class which handles speech recognition in this application
 *
 *  Speech recognition is done using CMUSphinx's sphinx4 library, which is contained in the lib folder.
 *  It is performed on its own thread.
 *
 *  SpeechRecognizer is Observable. Any controller (screen) which needs to handle voice control functionality
 *  should implement Observer and override the update(Observable o, Object arg) function. Object arg will always
 *  be the resulting string taken in by the speech recognizer. For an example, see TaggingScreenController
 */
public class SpeechRecognizer extends Observable {
    private LiveSpeechRecognizer recognizer;
    private SpeechThread speechThread;
    private boolean started;

    public SpeechRecognizer() throws IOException {
        Configuration configuration = new Configuration();

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
        recognizer = new LiveSpeechRecognizer(configuration);
        recognizer.startRecognition(true);
        speechThread = new SpeechThread();
        speechThread.setDaemon(true);
    }

    /*
     * Start/resume the speech thread
     */
    public void startRecognition() {
        if (!started) {
            speechThread.start();
            started = true;
        } else {
            speechThread.speechResume();
        }
    }

    /*
     * Pause the speech thread/speech recognition
     */
    public void stopRecognition() {
        try {
            speechThread.speechPause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Inner class representing the thread for speech recognition
     */
    private class SpeechThread extends Thread {
        SpeechResult result;
        private volatile boolean running = true;
        private volatile boolean paused = false;
        private final Object pauseLock = new Object();
        @Override
        public void run() {
            while (running) {
                synchronized (pauseLock) {
                    if (!running) {
                        break;
                    }
                    if (paused) {
                        try {
                            pauseLock.wait();
                        } catch (Exception e) {
                            e.printStackTrace();
                            break;
                        }
                        if (!running) {
                            break;
                        }
                    }
                    // Get the spoken input and notify all observers that something was said
                    String tokens = recognizer.getResult().getHypothesis().trim();
                    setChanged();
                    notifyObservers(tokens);
                }
            }
        }

        public void speechStop() {
            running = false;
            speechResume();
        }

        public void speechPause() {
            paused = true;
        }

        public void speechResume() {
            synchronized (pauseLock) {
                paused = false;
                pauseLock.notifyAll();
            }
        }
    }

}