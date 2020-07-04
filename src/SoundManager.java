import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.net.URL;

public class SoundManager {
    URL shoot;
    URL jump;
    URL click;
    URL click2;
    URL swoosh;
    URL collect;
    URL land;
    URL step1;
    URL step2;
    URL splat;
    URL explode;
    URL deepjump;
    URL yes;
    URL no;

    URL song;

    public SoundManager() {
        shoot = loadSound("shoot.wav");
        jump = loadSound("jump.wav");
        click = loadSound("click.wav");
        click2 = loadSound("click2.wav");
        swoosh = loadSound("swoosh.wav");
        collect = loadSound("collect.wav");
        land = loadSound("land.wav");
        step1 = loadSound("step1.wav");
        step2 = loadSound("step2.wav");
        splat = loadSound("splat.wav");
        explode = loadSound("explode.wav");
        deepjump = loadSound("deepjump.wav");
        yes = loadSound("yes.wav");
        no = loadSound("no.wav");
        song = loadSound("song.wav");
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
    public void playSoundLoop() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    AudioInputStream audioIn = AudioSystem.getAudioInputStream(song);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioIn);
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                    while (true) {
                        Thread.sleep(10000);
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

}
