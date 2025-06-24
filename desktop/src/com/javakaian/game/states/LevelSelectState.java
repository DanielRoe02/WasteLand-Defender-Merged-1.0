package com.javakaian.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.javakaian.game.resources.MusicHandler;
import com.javakaian.game.resources.MyAtlas;
import com.javakaian.game.ui.buttons.ButtonFactory;
import com.javakaian.game.ui.buttons.OButton;
import com.javakaian.game.ui.buttons.OButtonListener;
import com.javakaian.game.util.GameConstants;
import com.javakaian.game.util.GameUtils;

import java.util.ArrayList;
import java.util.List;

public class LevelSelectState extends State {

    private final String stateName = "SELECT LEVEL";

    private OButton btnLevel1;
    private OButton btnLevel2;
    private OButton btnLevel3;
    private OButton btnBack;

    private final List<OButton> buttons;

    public LevelSelectState(StateController stateController) {
        super(stateController);
        bitmapFont.getData().setScale(0.7f);
        glyphLayout.setText(bitmapFont, stateName);

        buttons = new ArrayList<>();
        initButtons();
        setListeners();
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        super.render(sb, sr);

        Gdx.gl.glClearColor(0.2f, 0.3f, 0.4f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.begin();
        GameUtils.renderCenter(stateName, sb, bitmapFont);

        // Render each button manually
        for (OButton button : buttons) {
            button.render(sb);
        }

        sb.end();
    }

    @Override
    public void update(float deltaTime) {
        // Optional: Add animations or transitions here
    }

    private void initButtons() {
        ButtonFactory bf = new ButtonFactory(
                GameConstants.GRID_WIDTH * 1.8f,
                GameConstants.GRID_HEIGHT * 1.8f
        );

        ButtonFactory bbtn = new ButtonFactory(GameConstants.GRID_WIDTH * 1.2f,
                GameConstants.GRID_HEIGHT * 1.2f);

        btnLevel1 = bf.createOButton("LEVEL 1", MyAtlas.GENERIC_BUTTON, true);
        btnLevel2 = bf.createOButton("LEVEL 2", MyAtlas.GENERIC_BUTTON, true);
        btnLevel3 = bf.createOButton("LEVEL 3", MyAtlas.GENERIC_BUTTON, true);
        btnBack   = bf.createOButton("BACK", MyAtlas.GENERIC_BUTTON, true);

        // 🔧 Set manual positions (adjust to fit your screen)
        btnLevel1.setPosition(250, 400);
        btnLevel2.setPosition(550, 400);
        btnLevel3.setPosition(800, 400);
        btnBack.setPosition(50, 50);

        buttons.add(btnLevel1);
        buttons.add(btnLevel2);
        buttons.add(btnLevel3);
        buttons.add(btnBack);
    }

    private void setListeners() {
        btnLevel1.setButtonListener((event, x, y) -> {
            if (event == OButtonListener.TouchEvent.RELEASE) {
                getStateController().setSelectedLevel(1);
                getStateController().setState(StateEnum.TutorialState);
            }
        });

        btnLevel2.setButtonListener((event, x, y) -> {
            if (event == OButtonListener.TouchEvent.RELEASE) {
                getStateController().setSelectedLevel(2);
                getStateController().setState(new PlayState(getStateController(), 2));
                MusicHandler.playBackgroundMusic();
                MusicHandler.stopMenuMusic();
            }
        });

        btnLevel3.setButtonListener((event, x, y) -> {
            if (event == OButtonListener.TouchEvent.RELEASE) {
                getStateController().setSelectedLevel(3);
                getStateController().setState(new PlayState(getStateController(), 3));
                MusicHandler.playBackgroundMusic();
                MusicHandler.stopMenuMusic();
            }
        });

        btnBack.setButtonListener((event, x, y) -> {
            if (event == OButtonListener.TouchEvent.RELEASE) {
                getStateController().setState(StateEnum.MenuState);
                MusicHandler.playMenuMusic();
            }
        });
    }

    @Override
    public void updateInputs(float x, float y) {}

    @Override
    public void touchDown(float x, float y, int pointer, int button) {
        buttons.stream()
                .filter(b -> b.contains(x, y))
                .findFirst()
                .ifPresent(b -> b.touchDown(x, y));
    }

    @Override
    public void touchUp(float x, float y, int pointer, int button) {
        buttons.forEach(b -> b.setPressed(false));
        buttons.stream()
                .filter(b -> b.contains(x, y))
                .findFirst()
                .ifPresent(b -> b.touchRelease(x, y));
    }

    @Override
    public void scrolled(int amount) {}
}
