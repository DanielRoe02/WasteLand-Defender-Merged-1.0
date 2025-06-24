package com.javakaian.game.states;

import static com.javakaian.game.util.GameConstants.ALPHA;
import static com.javakaian.game.util.GameConstants.BLUE;
import static com.javakaian.game.util.GameConstants.GREEN;
import static com.javakaian.game.util.GameConstants.GRID_HEIGHT;
import static com.javakaian.game.util.GameConstants.GRID_WIDTH;
import static com.javakaian.game.util.GameConstants.RED;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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

public class GameWinState extends State {

    private final String stateName = "YOU WIN";

    private OButton btnReplay;
    private OButton btnMenu;
    private OButton btnNextLevel;
    private final List<OButton> buttons;

    public GameWinState(StateController stateController) {
        super(stateController);

        bitmapFont = GameUtils.generateBitmapFont(100, Color.WHITE);
        glyphLayout.setText(bitmapFont, stateName);
        buttons = new ArrayList<>();
        initButtons();
        setListeners();
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        super.render(sb, sr);

        Gdx.gl.glClearColor(RED, GREEN, BLUE, ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.begin();
        GameUtils.renderCenter(stateName, sb, bitmapFont);
        buttons.forEach(b -> b.render(sb)); // 👈 Render each button manually
        sb.end();
    }

    @Override
    public void update(float deltaTime) {
    }

    private void initButtons() {
        final ButtonFactory bf = new ButtonFactory(GRID_WIDTH * 1.5f, GRID_HEIGHT * 1.5f);

        btnReplay = bf.createOButton(MyAtlas.RESTART_GAME);
        btnMenu = bf.createOButton(MyAtlas.MENU_BUTTON);
        btnNextLevel = bf.createOButton(MyAtlas.MENU_PLAY); // should be next level but not yet exist

        buttons.add(btnReplay);
        buttons.add(btnMenu);
        buttons.add(btnNextLevel);

        // 👇 Manual positioning
        float buttonY = 350f;      // Vertical line for all buttons
        float spacing = 100f;       // Space between buttons
        float startX = 350f;       // Starting X position

        btnReplay.setPosition(startX, buttonY);

        float menuX = startX + btnReplay.getSize().x + spacing;
        btnMenu.setPosition(menuX, buttonY);

        float nextX = menuX + btnMenu.getSize().x + spacing;
        btnNextLevel.setPosition(nextX, buttonY);
    }

    @Override
    public void updateInputs(float x, float y) {
    }

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
    public void scrolled(int amount) {
    }

    private void setListeners() {
        btnNextLevel.setButtonListener((event, x, y) -> {
            if (event == OButtonListener.TouchEvent.RELEASE) {
                getStateController().setState(StateEnum.PlayState); // Update to Level2 if needed
                MusicHandler.playBackgroundMusic();
            }
        });

        btnReplay.setButtonListener((event, x, y) -> {
            if (event == OButtonListener.TouchEvent.RELEASE) {
                getStateController().setState(StateEnum.PlayState);
                MusicHandler.playBackgroundMusic();
            }
        });

        btnMenu.setButtonListener((event, x, y) -> {
            if (event == OButtonListener.TouchEvent.RELEASE) {
                getStateController().setState(StateEnum.MenuState);
                MusicHandler.playMenuMusic();
            }
        });
    }
}
