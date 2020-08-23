package com.tiledmapstest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tiledmapstest.TiledMapsTest;
import com.tiledmapstest.actors.TestActor;

public class MapScreen extends BaseScreen {

    TiledMap tiledMap;
    OrthographicCamera camera;
    TiledMapRenderer renderer;
    MapProperties mapProperties;
    float width, height, zoomFactor;
    int tiledMapWidth, tiledMapHeight, tiledLayerWidth, tiledLayerHeight;
    Vector3 mapCenter;
    Stage stage;
    Texture testTexture;
    Sprite testSprite;
    TestActor testActor;
    SpriteBatch spriteBatch;
    ScreenViewport viewport;

    public MapScreen(TiledMapsTest game) {
        super(game);
    }

    @Override
    public void show() {
        // -----------> GRAPHICAL ELEMENTS AND PROPERTIES <--- START

        spriteBatch = new SpriteBatch();

        tiledMap = new TmxMapLoader().load("androidTest.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap);
        mapProperties = tiledMap.getProperties();

        testTexture = new Texture("badlogic.jpg");
        testSprite = new Sprite(testTexture);
        testActor = new TestActor(testSprite);
        testActor.setSizeX(300f);
        testActor.setSizeY(300f);


        // below, the property "width" is the number of tiles, instead "tilewidth" is the width of the tiles in pixel
        // same goes for "height" and "tileheight"
        //NOTE:
        tiledMapWidth = mapProperties.get("width", Integer.class) * mapProperties.get("tilewidth", Integer.class);
        tiledMapHeight = mapProperties.get("height", Integer.class) * mapProperties.get("tileheight", Integer.class);

        //this gets instead the width and height of a single layer (in case that the map is single layered, they are the same)
        tiledLayerWidth = ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getWidth() * mapProperties.get("tilewidth", Integer.class);
        tiledLayerHeight = ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getHeight() * mapProperties.get("tileheight", Integer.class);

        //THE FOLLOWING SETS THE CAMERA POSITION TO THE CENTER OF THE MAP REGARDLESS OF THE ZOOM LEVEL
        mapCenter = new Vector3(tiledLayerWidth/2f, tiledLayerHeight/2f, 0);
        //camera.position.set(mapCenter);

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        // -----------> GRAPHICAL ELEMENTS AND PROPERTIES <--- END

        // -----------> CAMERA, VIEWPORT AND STAGE STUFF <--- BEGIN

        camera = new OrthographicCamera();
        //zoomFactor = 3f;
        camera.zoom = 1f;
        camera.setToOrtho(false, tiledLayerWidth, tiledLayerHeight);

        viewport = new ScreenViewport(camera);
        viewport.setScreenPosition(tiledLayerWidth/2, tiledLayerHeight/2);
        viewport.update((int) width, (int) height, false);

        camera.update();

        stage = new Stage(viewport);

        stage.addActor(testActor);

        //System.out.println(camera.position.x + " " + width*2 + " " + height*2 + " " + tiledLayerWidth + " " + tiledLayerHeight);

        // -----------> CAMERA, VIEWPORT AND STAGE STUFF <--- END

        // -----------> INPUT STUFF <--- BEGIN

        Gdx.input.setInputProcessor(stage);
        stage.addListener(new InputListener(){
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
                    System.out.println(camera.position.x + " " + camera.position.y + " " + camera.zoom);

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

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        testSprite.draw(spriteBatch);
        spriteBatch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void hide() {
        stage.dispose();
        tiledMap.dispose();
        testTexture.dispose();
        spriteBatch.dispose();

    }
}
