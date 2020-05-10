import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.net.URL;

public class SoundManager {
    URL shoot;
    URL jump;

    public SoundManager() {
        shoot = loadSound("shoot.wav");
        jump = loadSound("jump.wav");
    }

    public URL loadSound(String soundFile) {
        URL s = null;
        try {
            File f = new File("sounds/" + soundFile);
            s = f.toURI().toURL();
        } catch (Exception e) {}
        return s;
    }

    public void playSound(URL url) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioIn);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

}
