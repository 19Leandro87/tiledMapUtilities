package com.tiledmapstest;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import java.util.ArrayList;
import java.util.List;

public class OrthogonalTiledMapRendererWithObjects extends OrthogonalTiledMapRenderer {

    private Sprite sprite;
    private List<Sprite> sprites, spritesClone;
    private int drawSpritesAfterLayer = 1;

    public OrthogonalTiledMapRendererWithObjects(TiledMap map) {
        super(map);
        sprites = new ArrayList<Sprite>();
    }

    public void addSprite(Sprite sprite){
        sprites.add(sprite);
    }

    public void removeAllSprites(){
        sprites.clear();
    }

    //CLONES ALL THE FRAMES IN THE ORIGINAL LIST INTO A CLONED TEMP LIST
    public void cloneList(){
        spritesClone = new ArrayList<Sprite>(sprites);
    }

    //REPLACES THE ORIGINAL LIST (eventually modified or wiped etc.) WITH THE CLONED TEMP LIST
    public void clonedListToOriginal(){
        sprites = new ArrayList<Sprite>(spritesClone);
    }

    @Override
    public void render() {
        beginRender();
        int currentLayer = 0;
        for (MapLayer layer : map.getLayers()) {
            if (layer.isVisible()) {
                if (layer instanceof TiledMapTileLayer) {
                    renderTileLayer((TiledMapTileLayer)layer);
                    currentLayer++;
                    if(currentLayer == drawSpritesAfterLayer){
                        for(Sprite sprite : sprites)
                            sprite.draw(this.batch);
                    }
                } else {
                    for (MapObject object : layer.getObjects()) {
                        renderObject(object);
                    }
                }
            }
        }
        endRender();
    }


    //THIS IS THE METHOD THAT RENDERS OBJECTS
    @Override
    public void renderObject(MapObject object) {
        if(object instanceof TextureMapObject) {
            TextureMapObject textureObj = (TextureMapObject) object;
            batch.draw(textureObj.getTextureRegion(), textureObj.getX(), textureObj.getY());
        }
    }
}