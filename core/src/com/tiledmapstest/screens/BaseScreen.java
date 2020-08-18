package com.tiledmapstest.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Screen;
import com.tiledmapstest.TiledMapsTest;

public abstract class BaseScreen implements Screen {

    protected TiledMapsTest game;

    public BaseScreen(TiledMapsTest game){
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
