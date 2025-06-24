package com.javakaian.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.javakaian.game.level.Level;
import com.javakaian.game.resources.MusicHandler;

public class PlayState extends State {

    private final Level level;
    private boolean paused = false;

    private final int levelNumber;

    public PlayState(StateController stateController, int levelNumber) {
        super(stateController);
        this.levelNumber = levelNumber;
        this.level = new Level(this, levelNumber); // 👈 pass levelNumber
    }

    @Override
    public void render(SpriteBatch sb,ShapeRenderer sr) {
        super.render(sb,sr);
        sb.begin();
        level.render(sb);
        sb.end();

        sr.begin(ShapeType.Line);
        level.render(sr);
        sr.end();
    }

    @Override
    public void update(float deltaTime) {
        if (!paused) {
            level.update(Gdx.graphics.getDeltaTime());
        }
    }

    public void gameWin() {
        MusicHandler.stopBackgroundMusic();
        level.restart();
        stateController.setState(StateEnum.GameWinState); // you'll add this enum
        //MusicHandler.playGameWinMusic(); // or add a win theme if available
        //MusicHandler.WaveClearMusicStop();
    }


    public void gameOver() {
        MusicHandler.stopBackgroundMusic();
        level.restart();
        stateController.setState(StateEnum.GameOverState);
        MusicHandler.playGameoverMusic();
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    @Override
    public void updateInputs(float x, float y) {
        level.updateInputs(x,y);
    }

    public void restart() {
        level.restart();
    }

    @Override
    public void touchDown(float x, float y, int pointer, int button) {
        level.touchDown(x,y);
    }

    @Override
    public void touchUp(float x, float y, int pointer, int button) {
        level.touchRelease(x,y);
    }

    @Override
    public void scrolled(int amount) {
        if (amount > 0) {
            camera.zoom += 0.5;
        } else {
            camera.zoom -= 0.5;
        }
    }

}
