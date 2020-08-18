package com.tiledmapstest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tiledmapstest.TiledMapsTest;

public class MapScreen extends BaseScreen {

    TiledMap tiledMap;
    OrthographicCamera camera;
    TiledMapRenderer renderer;
    float width, height;
    Stage stage;

    public MapScreen(TiledMapsTest game) {
        super(game);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        tiledMap = new TmxMapLoader().load("androidTest.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap);
        Gdx.input.setInputProcessor(stage);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
        camera.update();

        stage.addListener(new InputListener(){
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if(keycode == Input.Keys.LEFT)
                    camera.translate(-32,0);
                if(keycode == Input.Keys.RIGHT)
                    camera.translate(32,0);
                if(keycode == Input.Keys.UP)
                    camera.translate(0,-32);
                if(keycode == Input.Keys.DOWN)
                    camera.translate(0,32);
                if(keycode == Input.Keys.NUM_1)
                    tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
                if(keycode == Input.Keys.NUM_2)
                    tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
                if(keycode == Input.Keys.A)
                    camera.zoom += 0.1f;
                if(keycode == Input.Keys.Q)
                    camera.zoom -= 0.1f;
                return false;
            }
        });

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.5f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        renderer.setView(camera);
        renderer.render();

        stage.act();
        stage.draw();
    }

    @Override
    public void hide() {
        stage.dispose();
        tiledMap.dispose();

    }
}
