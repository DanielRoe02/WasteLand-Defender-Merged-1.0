package com.javakaian.game.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.javakaian.game.controllers.EnemyController;
import com.javakaian.game.controllers.TowerController;
import com.javakaian.game.map.Grid;
import com.javakaian.game.map.Grid.EnumGridType;
import com.javakaian.game.map.Map;
import com.javakaian.game.resources.MusicHandler;
import com.javakaian.game.ui.menu.InformationMenu;
import com.javakaian.game.ui.menu.MainControlMenu;
import com.javakaian.game.resources.MyAtlas;
import com.javakaian.game.states.PlayState;
import com.javakaian.game.states.State.StateEnum;
import com.javakaian.game.towers.BaseTower;
import com.javakaian.game.towers.BaseTower.TowerType;
import com.javakaian.game.util.GameConstants;
import com.javakaian.game.util.GameUtils;

public class Level {

    private EnemyController enemyController;
    private TowerController towerController;

    private Map map;
    private MainControlMenu towerSelectionMenu;
    private InformationMenu informationMenu;

    private int enemyNumber;
    private int enemyHealth;
    private int score;
    private int money;
    private int remainingHealth;
    private final BitmapFont bitmapFont;
    private final PlayState state;

    private int timeLeft;
    private boolean renderTimeAndWaveNumber = false;
    private int waveNumber = 1;
    private boolean showWinMessage = false;
    private float winMessageTimer = 0;

    private int maxWaves; // or any number you want


    private final int levelNumber;

    public Level(PlayState state, int levelNumber) {
        this.state = state;
        this.levelNumber = levelNumber;
        this.bitmapFont = GameUtils.generateBitmapFont(80, Color.BLACK);
        init(levelNumber);// 👈 internally use levelNumber
    }

    public int getLevelNumber() {
        return levelNumber;
    }


    public void init(int levelNumber) {
        switch (levelNumber) {
            case 1:
                maxWaves = 1;
                enemyNumber = 5;
                enemyHealth = 100;
                remainingHealth = 5;
                break;
            case 2:
                maxWaves = 2;
                enemyNumber = 10;
                enemyHealth = 400;
                remainingHealth = 5;
                break;
            case 3:
                maxWaves = 1;
                enemyNumber = 1;
                enemyHealth = 50000;
                remainingHealth = 1;
                break;
            default:
                maxWaves = 1;
                enemyNumber = 10;
                enemyHealth = 100;
                break;
        }

        score = 0;
        money = GameConstants.INITIAL_MONEY;


        map = new Map(levelNumber);
        enemyController = new EnemyController(this);
        towerController = new TowerController();
        informationMenu = new InformationMenu(MyAtlas.MENU_TILE);
        towerSelectionMenu = new MainControlMenu(this);
    }

    public void render(ShapeRenderer sr) {
        map.render(sr);
        enemyController.render(sr);
        towerController.render(sr);
        towerSelectionMenu.render(sr);
    }

    public void render(SpriteBatch sb) {
        map.render(sb);
        enemyController.render(sb);
        towerController.render(sb);
        towerSelectionMenu.render(sb);
        informationMenu.render(sb);
        if (renderTimeAndWaveNumber && !showWinMessage) {
            GameUtils.renderCenter("Wave: " + waveNumber + " in: " + timeLeft + " second", sb, bitmapFont);
        }

        if (showWinMessage) {
            bitmapFont.setColor(Color.GREEN); // Optional
            GameUtils.renderCenter("YOU CURED THE POLLUTION", sb, bitmapFont);
            bitmapFont.setColor(Color.BLACK); // Reset after
        }


    }

    public void update(float deltaTime) {
        map.update(deltaTime);
        enemyController.update(deltaTime);
        towerController.update(deltaTime);
        if (showWinMessage) {
            winMessageTimer -= deltaTime;
            if (winMessageTimer <= 0f) {
                showWinMessage = false;
            }
        }

    }

    public void updateInputs(float x, float y) {
        towerSelectionMenu.updateInputs(x, y);
    }

    public void createTowerClicked(float x, float y, TowerType type) {

        Grid grid = map.getSelectedGrid(x, y);
        if (grid == null)
            return;
        switch (grid.getType()) {
            case TOWER:
                System.out.println("CAN NOT BUILD TOWER ALREADY EXIST");
                break;
            case LAND:
                int cost = towerController.buildTower(grid.getPosition().x, grid.getPosition().y,
                        enemyController.getEnemyList(), type, money);
                if (cost != 0) {
                    grid.setType(EnumGridType.TOWER);
                    decreaseMoney(cost);
                }
                this.map.getBoard().setRender(false);
                break;
            case PATH:
                System.out.println("CAN NOT BUILD TO THE PATH");
                break;
        }
    }

    public void enemyPassedTheCheckPoint() {
        remainingHealth--;
        towerSelectionMenu.fireHealthChanged(remainingHealth);
        if (remainingHealth == 0) {
            state.gameOver();
        }
    }

    public void enemyKilled(int bounty) {
        score += GameConstants.SCORE_INCREASE_CONSTANT;
        enemyNumber -= 1;
        increaseMoney(bounty);
        informationMenu.fireScoreChanged(this.score);
        towerSelectionMenu.fireEnemyNumberChanged(enemyNumber);

        // Win condition
        if (waveNumber >= maxWaves && enemyNumber <= 0)
        {
            MusicHandler.stopBackgroundMusic();
            MusicHandler.WaveClearMusic();
            System.out.println("All enemies defeated. Transforming land...");
            map.transformToGrasslandAnimated();
            // 💬 Show message
            showCuredPollutionMessage();

            new Thread(() -> {
                try {
                    Thread.sleep(3000); // Wait 3 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Gdx.app.postRunnable(() -> {
                    state.gameWin();
                });
            }).start();
        }
    }

    public void newWaveCreated(int size) {
        if (waveNumber >= maxWaves) {
            renderTimeAndWaveNumber = false;
            return;
        }

        waveNumber++;
        enemyNumber = size;
        renderTimeAndWaveNumber = false;
    }


    public void showCuredPollutionMessage() {
        this.showWinMessage = true;
        this.winMessageTimer = 3f; // show for 3 seconds
    }


    public void touchDown(float x, float y) {

        if (towerSelectionMenu.contains(x, y)) {
            towerSelectionMenu.touchDown(x, y);
        } else {
            selectGrid(x, y);
        }

    }

    public void touchRelease(float x, float y) {
        towerSelectionMenu.touchRelease(x, y);
    }

    public void selectGrid(float x, float y) {

        Grid grid = this.map.getSelectedGrid(x, y);
        if (grid == null)
            return;
        switch (grid.getType()) {
            case TOWER:
                BaseTower t = towerController.getSelectedTower(grid.getPosition());
                informationMenu.updateTowerInformation(t);
                towerSelectionMenu.updateUpgradeButtons(money);
                break;
            case LAND:
                towerController.clearSelectedTower();
                informationMenu.clearInformations();
                towerSelectionMenu.clearSelectedTower();
                break;
            case PATH:
                towerController.clearSelectedTower();
                towerSelectionMenu.clearSelectedTower();
                break;

            default:
                break;

        }

    }

    public BaseTower getSelectedTower() {
        return towerController.getSelectedTower();
    }

    public Map getMap() {
        return map;
    }

    public int getEnemyHealth() {
        return enemyHealth;
    }

    public int getEnemyNumber() {
        return enemyNumber;
    }

    public void renderGrids(boolean b) {
        this.map.getBoard().setRender(b);
    }

    public void nextWaveCountDown(int i) {
        this.timeLeft = i;
        renderTimeAndWaveNumber = true;

    }

    public void increaseAttackClickled() {
        BaseTower t = towerController.getSelectedTower();
        int cost = t.getAttackPrice();
        if (cost <= money) {
            towerController.increaseAttack();
            decreaseMoney(cost);
            informationMenu.updateTowerInformation(t);
        }

    }

    public void increaseRangeClicked() {
        BaseTower t = towerController.getSelectedTower();
        int cost = t.getRangePrice();
        if (cost <= money) {
            towerController.increaseRange();
            decreaseMoney(cost);
            informationMenu.updateTowerInformation(t);
        }

    }

    public void increaseSpeedClicked() {
        BaseTower t = towerController.getSelectedTower();
        int cost = t.getSpeedPrice();
        if (cost <= money) {
            towerController.increaseSpeed();
            decreaseMoney(cost);
            informationMenu.updateTowerInformation(t);
        }
    }

    public void restart() {
        init(levelNumber);
    }

    public void pause() {
        state.pause();
    }

    public void resume() {
        state.resume();
    }

    public void speed2xClicked() {
        towerController.speed2xClicked();
        enemyController.speed2xClicked();
    }

    public void normalSpeedClicked() {
        towerController.normalSpeedClicked();
        enemyController.normalSpeedClicked();
    }

    public void returnToMenuClicked() {
        state.getStateController().setState(StateEnum.PauseState);
    }

    public void increaseMoney(int amount) {
        this.money += amount;
        informationMenu.fireMoneyChanged(money);
        towerSelectionMenu.moneyChanged(money);
    }

    public void decreaseMoney(int amount) {
        this.money -= amount;
        informationMenu.fireMoneyChanged(money);
        towerSelectionMenu.moneyChanged(money);
    }

}
