package com.tiledmapstest;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

public class OrthogonalTiledMapRendererWithObjects extends OrthogonalTiledMapRenderer {

    private Sprite sprite;
    private List<Sprite> sprites, spritesClone;
    private int drawSpritesAfterLayer = 1, tmpActorIndex;

    public OrthogonalTiledMapRendererWithObjects(TiledMap map) {
        super(map);
        sprites = new ArrayList<Sprite>();
    }

    // -----> JUST SOME TEST SPRITES METHODS IDEAS <----- BEGIN
    public int getDrawSpritesAfterLayer() {
        return drawSpritesAfterLayer;
    }

    public void setDrawSpritesAfterLayer(int drawSpritesAfterLayer) {
        this.drawSpritesAfterLayer = drawSpritesAfterLayer;
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

    public List<Sprite> getSprites() {
        return sprites;
    }

    public List<Sprite> getSpritesClone() {
        return spritesClone;
    }

    // -----> JUST SOME TEST SPRITES METHODS IDEAS <----- END

    // -----> The following is a possible render() override, to insert sprites after certain Tiled layers (not currently used)

  /*  @Override
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
    }*/


    //THIS IS THE METHOD THAT RENDERS OBJECTS
    @Override
    public void renderObject(MapObject object) {
        if(object instanceof TextureMapObject) {
            TextureMapObject textureObj = (TextureMapObject) object;
            batch.draw(textureObj.getTextureRegion(), textureObj.getX(), textureObj.getY());
        }
    }

    // THE FOLLOWING IS USED TO CHANGE THE ZINDEX OF A SELECTION OF ACTORS DEPENDING ON THEIR POSITION ON THE MAP
    public void zIndexRendering (Array<Actor> actors){
        for (int i = 0; i < actors.size; i++){
            for (int k = i+1; k < actors.size; k++) {
                //if a given actor is positioned lower in the map as compared to another one in the array, but its ZIndex is also lower
                //(ergo is rendered behind this second one), render it in front of the second one instead (swapping the ZIndexes) --OR--
                //if a given actor is higher than another one, but its ZIndex is also higher, render the 2nd in front instead (swapping the ZIndexes)
                if (actors.get(i).getY() < actors.get(k).getY() && actors.get(i).getZIndex() < actors.get(k).getZIndex() ||
                        actors.get(i).getY() > actors.get(k).getY() && actors.get(i).getZIndex() > actors.get(k).getZIndex()) {
                    tmpActorIndex = actors.get(i).getZIndex();
                    actors.get(i).setZIndex(actors.get(k).getZIndex());
                    actors.get(k).setZIndex(tmpActorIndex);
                }
            }
        }
    }

    //THE FOLLOWING SHOULDN'T BE USEFUL SINCE BY DEFAULT LIBGDX ALREADY ASSIGNS A ZINDEX TO THE NEWLY CREATED ACTORS (1st 0, 2nd 1, 3rd 2, etc.)
    public void zIndexInit (Array<Actor> actors){
        for (int i = 0; i < actors.size; i++)
            actors.get(i).setZIndex(i);
    }
}