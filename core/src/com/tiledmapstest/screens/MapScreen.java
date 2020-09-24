package com.tiledmapstest.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tiledmapstest.OrthogonalTiledMapRendererWithObjects;
import com.tiledmapstest.TiledMapsTest;
import com.tiledmapstest.actors.TestActor;

import java.util.ArrayList;
import java.util.List;

import sun.rmi.runtime.Log;

public class MapScreen extends BaseScreen {

    TiledMap tiledMap;
    OrthographicCamera camera;
    OrthogonalTiledMapRendererWithObjects renderer;
    MapProperties mapProperties;
    int tiledMapWidth, tiledMapHeight, tiledLayerWidth, tiledLayerHeight;
    Vector3 mapCenter;
    Stage stage;
    Texture testTexture, testTexture2;
    Sprite testSprite, testSprite2, testSprite3;
    TestActor testActor, testActor2, testActor3;
    ScreenViewport viewport;
    Array<Actor> actors;
    int [] layers;
    int origIndex, newIndex, tmpTiledIndex;

    public MapScreen(TiledMapsTest game) {
        super(game);
    }
    @Override
    public void show() {
        // -----------> GRAPHICAL ELEMENTS AND PROPERTIES <--- START
        testTexture2 = new Texture("grassTileset1.jpg");
        testSprite3 = new Sprite(testTexture2);

        tiledMap = new TmxMapLoader().load("map2/android4.tmx");
        renderer = new OrthogonalTiledMapRendererWithObjects(tiledMap);
        mapProperties = tiledMap.getProperties();

        testTexture = new Texture("badlogic.jpg");
        testSprite = new Sprite(testTexture);
        testActor = new TestActor(testSprite);
        testActor.setSizeX(300f);
        testActor.setSizeY(300f);

        testActor2 = new TestActor(testSprite);
        testActor2.setSizeX(300f);
        testActor2.setSizeY(300f);

        testActor3 = new TestActor(testSprite3);
        testActor3.setSizeX(300f);
        testActor3.setSizeY(300f);
        testActor3.setPosition(-200f, -200f);
        testActor3.setMovement(0f);

        // below, the property "width" is the number of tiles, instead "tilewidth" is the width of the tiles in pixel
        // same goes for "height" and "tileheight"
        tiledMapWidth = mapProperties.get("width", Integer.class) * mapProperties.get("tilewidth", Integer.class);
        tiledMapHeight = mapProperties.get("height", Integer.class) * mapProperties.get("tileheight", Integer.class);

        //this gets instead the width and height of a single layer (in case that the map is single layered, they are the same)
        tiledLayerWidth = ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getWidth() * mapProperties.get("tilewidth", Integer.class);
        tiledLayerHeight = ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getHeight() * mapProperties.get("tileheight", Integer.class);

        // -----------> GRAPHICAL ELEMENTS AND PROPERTIES <--- END

        // -----------> CAMERA, VIEWPORT AND STAGE <--- BEGIN
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 1.5f;

        viewport = new ScreenViewport(camera);

        stage = new Stage(viewport);

        //THE FOLLOWING SETS THE CAMERA POSITION IN THE CENTER OF THE "MAP LAYER"
        mapCenter = new Vector3(tiledLayerWidth/2f, tiledLayerHeight/2f, 0);
        camera.position.set(mapCenter);
        testActor.setMovement(-2f);

        testSprite.setSize(600, 600);
        renderer.addSprite(testSprite);

        testSprite2 = new Sprite(testTexture);
        testSprite2.setSize(100, 50);
        testSprite2.setPosition(tiledLayerWidth/2f -500, tiledLayerHeight/2f);
        renderer.addSprite(testSprite2);

        //When rendering an int array containing layer indexes, the first element in the array is in the background
        // and the last is in the foreground
        layers = new int[]{
                tiledMap.getLayers().getIndex("dirt"),
                tiledMap.getLayers().getIndex("grass"),
                tiledMap.getLayers().getIndex("objects")
        };

        stage.addActor(testActor);
        stage.addActor(testActor2);
        stage.addActor(testActor3);

        actors = new Array<>();
        actors.add(testActor);
        actors.add(testActor2);
        actors.add(testActor3);

        // -----------> CAMERA, VIEWPORT AND STAGE <--- END

        // -----------> INPUT <--- BEGIN

        Gdx.input.setInputProcessor(stage);
        stage.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                testActor.setPosition(x, y);
                testSprite2.setPosition(testActor.getX() -500, testActor.getY());
                if (testSprite2.getScaleX() < 6)
                    testSprite2.setScale(testSprite2.getScaleX()*1.5f);

                renderer.zIndexRendering(actors);

                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if(keycode == Input.Keys.LEFT && camera.position.x > 0)
                    camera.translate(-50,0);
                if(keycode == Input.Keys.RIGHT)
                    camera.translate(50,0);
                if(keycode == Input.Keys.UP)
                    camera.translate(0,50);
                if(keycode == Input.Keys.DOWN)
                    camera.translate(0,-50);
                if(keycode == Input.Keys.NUM_1)
                    tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());

                /* THE FOLLOWING DOESN'T WORK UNLESS THERE'S A SECOND LAYER
                if(keycode == Input.Keys.NUM_2)
                    tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
                */

                //binds the zoom level between two chosen values
                if(keycode == Input.Keys.A && camera.zoom < 2f)
                    camera.zoom += 0.25f;
                if(keycode == Input.Keys.Q && camera.zoom > 0.25f)
                    camera.zoom -= 0.25f;

                //reset camera position to the center of the map
                if(keycode == Input.Keys.X)
                    camera.position.set(mapCenter);

                if(keycode == Input.Keys.Y)
                    System.out.println("x = " + camera.position.x + " y = " + camera.position.y + " zoom = " + camera.zoom);

                if(keycode == Input.Keys.N)
                    testActor.setPosition(testActor.getX(), testActor.getY()-100);

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

        renderer.getBatch().begin();
        testSprite2.draw(renderer.getBatch());
        testSprite3.draw(renderer.getBatch());
        //renderer.renderTileLayer((TiledMapTileLayer) tiledMap.getLayers().get("dirt"));
        //renderer.renderTileLayer((TiledMapTileLayer) tiledMap.getLayers().get("grass"));
        renderer.getBatch().end();

        stage.act();
        stage.draw();

        renderer.render(layers);

        renderer.zIndexRendering(actors);

    }

    //(the following is not used in the code, it's just a possible way of hiding sprites, changing the alpha)
    //DEPENDING ON WHERE THE ACTOR IS, SHOWS OR HIDES CERTAIN SPRITES
    public void hideSprites(){
        if (testActor.getX()>tiledLayerWidth/2f)
            testSprite.setAlpha(0f);
        //or: renderer.getSprites().get(0).setAlpha(0f);
        else
            testSprite.setAlpha(1f);

        //REFERRING DIRECTLY TO THE SPRITE IS THE SAME AS WRITING: renderer.getSprites().get(0);
        //LOOKS LIKE WHEN YOU CHANGE THE PROPERTIES OF A SPRITE, THEY'RE UPDATED IN THE ARRAY EVEN IF YOU'RE CHANGING THEM AFTER ADDING IT
    }

    @Override
    public void hide() {
        stage.dispose();
        tiledMap.dispose();
        testTexture.dispose();
        renderer.dispose();
        testTexture2.dispose();
    }

}

