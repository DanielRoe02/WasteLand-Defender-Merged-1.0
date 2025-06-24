package com.javakaian.game.states;

import static com.javakaian.game.util.GameConstants.ALPHA;
import static com.javakaian.game.util.GameConstants.BLUE;
import static com.javakaian.game.util.GameConstants.GREEN;
import static com.javakaian.game.util.GameConstants.GRID_HEIGHT;
import static com.javakaian.game.util.GameConstants.GRID_WIDTH;
import static com.javakaian.game.util.GameConstants.RED;
import static com.javakaian.game.util.GameConstants.SCREEN_HEIGHT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.javakaian.game.resources.MusicHandler;
import com.javakaian.game.resources.MyAtlas;
import com.javakaian.game.ui.buttons.ButtonFactory;
import com.javakaian.game.ui.buttons.OButton;
import com.javakaian.game.ui.buttons.OButtonListener;
import com.javakaian.game.util.GameUtils;

import java.util.ArrayList;
import java.util.List;

public class TutorialState extends State {

    private final String stateName = "TUTORIAL";

    private int currentSlide = 0;
    private final String[] slides = {
            "Welcome to the game! Defend your base from enemies.",
            "Tap on a tower to build it. Use coins wisely!",
            "Upgrade towers to deal more damage.",
            "Each level has 3 waves. Get ready!"
    };

    private OButton btnNext;
    private OButton btnSkip;
    private final List<OButton> buttons;
    private final BitmapFont tutorialFont;

    public TutorialState(StateController stateController) {
        super(stateController);
        bitmapFont.getData().setScale(0.6f); // Title font size
        tutorialFont = GameUtils.generateBitmapFont(30, Color.WHITE); // Body font

        buttons = new ArrayList<>();
        initButtons();
        setListeners();
    }

    private void initButtons() {
        final ButtonFactory bf = new ButtonFactory(GRID_WIDTH * 1.5f, GRID_HEIGHT * 1.5f);

        float y = GRID_HEIGHT * 7;
        btnSkip = bf.createOButton(GRID_WIDTH * 4, y, "SKIP", MyAtlas.GENERIC_BUTTON, true);
        btnNext = bf.createOButton(GRID_WIDTH * 10, y, "NEXT", MyAtlas.GENERIC_BUTTON, true);

        buttons.add(btnSkip);
        buttons.add(btnNext);
    }

    private void setListeners() {
        btnNext.setButtonListener((event, x, y) -> {
            if (event == OButtonListener.TouchEvent.RELEASE) {
                if (currentSlide < slides.length - 1) {
                    currentSlide++;
                    // If this is the last slide, change button text to "START"
                    if (currentSlide == slides.length - 1) {
                        btnNext.setText("START");
                    }
                } else {
                    int level = getStateController().getSelectedLevel();
                    // In future, you can use `level` to load different levels
                    MusicHandler.stopMenuMusic();
                    MusicHandler.playBackgroundMusic();
                    getStateController().setState(StateEnum.PlayState);

                }
            }
        });


        btnSkip.setButtonListener((event, x, y) -> {
            if (event == OButtonListener.TouchEvent.RELEASE) {
                int level = getStateController().getSelectedLevel();
                // In future, you can use `level` to load different levels
                MusicHandler.stopMenuMusic();
                MusicHandler.playBackgroundMusic();
                getStateController().setState(StateEnum.PlayState);            }
        });
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        super.render(sb, sr);

        Gdx.gl.glClearColor(RED, GREEN, BLUE, ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.begin();

        GameUtils.renderCenter(stateName, sb, bitmapFont);

        float posY = SCREEN_HEIGHT / 2.4f;
        GameUtils.renderCenterWithY(slides[currentSlide], sb, tutorialFont, posY);

        buttons.forEach(b -> b.render(sb));

        sb.end();
    }

    @Override
    public void update(float deltaTime) {}

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