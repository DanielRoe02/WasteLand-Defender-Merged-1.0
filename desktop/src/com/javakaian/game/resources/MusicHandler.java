package com.javakaian.game.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class MusicHandler {

    public static boolean soundOn = true;
    private static Sound clickSound;
    private static Sound deadSound;
    private static Music gamePlayMusic;
    private static Music gameOverMusic;
    private static Music gameWinMusic;
    private static Music menuMusic;
    private static Music waveClearMusic;

    public static void init() {

        clickSound = Gdx.audio.newSound(Gdx.files.internal("click.wav"));
        deadSound = Gdx.audio.newSound(Gdx.files.internal("dead.wav"));

        gamePlayMusic = Gdx.audio.newMusic(Gdx.files.internal("Gamestart.mp3"));
        gamePlayMusic.setVolume(0.0005f);
        gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("Gameover.mp3"));
        gameWinMusic = Gdx.audio.newMusic(Gdx.files.internal("GameWin.mp3"));
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("Menumusic.mp3"));
        waveClearMusic = Gdx.audio.newMusic(Gdx.files.internal("mario.mp3"));
        menuMusic.setVolume(0.0005f);
    }

    public static void playBackgroundMusic() {

        gamePlayMusic.setLooping(true);
        gamePlayMusic.play();
    }

    public static void stopBackgroundMusic() {

        gamePlayMusic.stop();
    }

    public static void playMenuMusic() {

        menuMusic.setLooping(true);
        menuMusic.play();
    }

    public static void stopMenuMusic() {

        menuMusic.stop();
    }

    public static void playGameoverMusic() {

        gameOverMusic.play();
    }

    public static void playGameWinMusic() {

        gameWinMusic.play();
    }

    public static void WaveClearMusic() {

        waveClearMusic.play();
    }
    public static void WaveClearMusicStop() {

        waveClearMusic.stop();
    }


    public static void playClickSound() {
        if (soundOn) {
            clickSound.play();
        }

    }

    public static void playDeadSound() {
        if (soundOn) {
            deadSound.play();
        }

    }

    public static void setMusicOnOff(boolean isMusicOn) {
        if (isMusicOn) {
            gameOverMusic.setVolume(0.5f);
            gamePlayMusic.setVolume(0.5f);
            menuMusic.setVolume(0.5f);

        } else {
            gameOverMusic.setVolume(0);
            gamePlayMusic.setVolume(0);
            menuMusic.setVolume(0);

        }
    }

    public static void setSoundOnOff(boolean isSoundOn) {
        soundOn = isSoundOn;
    }

}
