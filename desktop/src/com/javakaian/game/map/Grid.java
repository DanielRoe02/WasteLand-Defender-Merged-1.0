package com.javakaian.game.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.javakaian.game.entity.GameObject;
import com.javakaian.game.resources.MyAtlas;

public class Grid extends GameObject {

    private EnumGridType type;

    public Grid(float x, float y, float width, float height, EnumGridType type) {
        super(x, y, width, height);
        this.type = type;
    }

    @Override
    public void render(ShapeRenderer sr) {
        sr.setColor(Color.FIREBRICK);
        super.render(sr);

    }

    public void render(SpriteBatch sb) {
        // Always draw LAND first as the base
        sb.draw(MyAtlas.LAND_TILE, position.x, position.y, size.x, size.y);

        // Then draw type-specific tile on top
        switch (type) {
            case PATH:
                sb.draw(MyAtlas.PATH_TILE, position.x, position.y, size.x, size.y);
                break;
            case WATER:
                sb.draw(MyAtlas.WATER_TILE, position.x, position.y, size.x, size.y);
                break;
            case POLLWATER:
                sb.draw(MyAtlas.POLLUTED_WATER_TILE, position.x, position.y, size.x, size.y);
                break;
            case TOXIC:
                sb.draw(MyAtlas.TOXIC_TILE, position.x, position.y, size.x, size.y);
                break;
            case GRASS:
                sb.draw(MyAtlas.GRASS_TILE, position.x, position.y, size.x, size.y);
                break;
            case TOWER:
                break;

            // No need to draw LAND again — it’s already drawn above
        }
    }



    public boolean contains(float x, float y) {
        return x >= position.x && x < position.x + size.x && y >= position.y && y < position.y + size.y;
    }

    public EnumGridType getType() {
        return type;
    }

    public void setType(EnumGridType type) {
        this.type = type;
    }

    public enum EnumGridType {
        PATH, LAND, TOWER, WATER, TOXIC, GRASS, POLLWATER
    }

}
