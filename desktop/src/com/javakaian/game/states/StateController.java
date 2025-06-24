package com.javakaian.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.javakaian.game.states.State.StateEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


public class StateController extends InputAdapter {

    private final Map<StateEnum, State> stateMap;
    private State currentState;
    private State previousState;
    private int selectedLevel = 1;

    private int currentLevel = 1;

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int level) {
        this.currentLevel = level;
    }

    public void incrementLevel() {
        if (currentLevel < 3) { // cap at level 3
            currentLevel++;
        }
    }


    public StateController() {
        stateMap = new HashMap<>();
        Gdx.input.setInputProcessor(this);
    }

    public void setState(StateEnum stateEnum) {
        previousState = currentState;
        currentState = getState(stateEnum);
    }

    public void setState(State state) {
        previousState = currentState;
        currentState = state;
    }

    public void setSelectedLevel(int level) {
        this.selectedLevel = level;
    }

    public int getSelectedLevel() {
        return selectedLevel;
    }

    /**
     * Goes back to previous state, which means we have to swap current and previous state.
     * */
    public void goBack() {
        State tmp = previousState;
        previousState = currentState;
        currentState = tmp;
    }

    /**
     * Return the state, if it already exist or make a new one.
     * */
    public State getState(StateEnum stateEnum){
        return stateMap.computeIfAbsent(stateEnum, this::createState);
    }

    public void render(SpriteBatch sb, ShapeRenderer sr) {
        currentState.render(sb,sr);
    }

    public void update(float deltaTime) {
        currentState.update(deltaTime);
        Vector2 result = unproject(Gdx.input.getX(),Gdx.input.getY());
        currentState.updateInputs(result.x,result.y);
    }


    private Vector2 unproject(int x, int y){
        final OrthographicCamera c = currentState.camera;
        Vector3 r = c.unproject(new Vector3(x,y,1));
        return new Vector2(r.x,r.y);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        final Vector2 result = unproject(screenX,screenY);
        currentState.touchDown(result.x,result.y,pointer,button);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        final Vector2 result = unproject(screenX,screenY);
        currentState.touchUp(result.x,result.y,pointer,button);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        currentState.scrolled(amount);
        return false;
    }
    private State createState(StateEnum stateEnum) {
        switch (stateEnum) {
            case PlayState:
                return new PlayState(this, 1); // 👈 default to level 1
            case GameWinState:
                return new GameWinState(this);
            case GameOverState:
                return new GameOverState(this);
            case MenuState:
                return new MenuState(this);
            case OptionState:
                return new OptionsState(this);
            case CreditsState:
                return new CreditState(this);
            case PauseState:
                return new PauseState(this);
            case TutorialState:
                return new TutorialState(this);
            default:
                throw new IllegalArgumentException("Invalid state enum: " + stateEnum);
        }
    }
}
