package model;

import java.io.IOException;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;

public class SpeechRecognizer {
    private static SpeechRecognizer instance;

    private LiveSpeechRecognizer recognizer;

    public SpeechRecognizer() throws IOException {
        Configuration configuration = new Configuration();

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
        recognizer = new LiveSpeechRecognizer(configuration);
    }

    public static SpeechRecognizer getInstance() throws IOException {
        if (instance == null) {
            instance = new SpeechRecognizer();
        }
        return instance;
    }

    public void startRecognition(boolean clear) {
        recognizer.startRecognition(clear);
    }

    public void stopRecognition() {
        recognizer.stopRecognition();
    }

    public SpeechResult getResult() {
        return recognizer.getResult();
    }
}
