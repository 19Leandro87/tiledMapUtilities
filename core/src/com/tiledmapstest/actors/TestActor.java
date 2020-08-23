package com.tiledmapstest.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TestActor extends Actor {

    private Sprite sprite;
    private float sizeX, sizeY;

    public void setSizeY(float sizeY) {
        this.sizeY = sizeY;
    }

    public void setSizeX(float sizeX) {
        this.sizeX = sizeX;
    }

    public float getSizeX() {
        return sizeX;
    }

    public float getSizeY() {
        return sizeY;
    }

    public TestActor (Sprite sprite){
        this.sprite = sprite;
        setSize(sizeX, sizeY);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(sprite, getX(), getY(), sizeX, sizeY);
    }

    @Override
    public void act(float delta) {
        this.setX(this.getX() - 1f);
    }
}
