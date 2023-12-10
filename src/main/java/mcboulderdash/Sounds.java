package mcboulderdash;

import com.almasb.fxgl.audio.Sound;

import static com.almasb.fxgl.dsl.FXGL.getAssetLoader;
import static com.almasb.fxgl.dsl.FXGL.getAudioPlayer;

public class Sounds {
    private static Sound collect = getAssetLoader().loadSound("collect.mp3");
    private static Sound death = getAssetLoader().loadSound("death.mp3");
    private static Sound portal = getAssetLoader().loadSound("portal.mp3");

    public static void playCollect() {
        getAudioPlayer().playSound(collect);
    }

    public static void playDeath() {
        getAudioPlayer().playSound(death);
    }

    public static void playPortal() {
        getAudioPlayer().playSound(portal);
    }
}
