package com.jedijump.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.jedijump.entity.character;
import com.jedijump.entity.platform;
import com.jedijump.utility.constants;

public class PlayState extends State{
    character character;
    platform plt, plt1, baseplt;
    Texture item, bg;
    TextureRegion pause, bgRegion;
    Rectangle rect;
    ShapeRenderer sr;
    Vector3 coords;

    public PlayState(Manager manager) {
        super(manager);
        character = new character(manager);
        plt = new platform(manager);
        plt1 = new platform(manager);
        baseplt = new platform(manager);
        character.create(new Vector2(0,0),new Vector2(32,32),1);
        plt.create(new Vector2(0,-36),new Vector2(64,16),0);
        plt1.create(new Vector2(-20,82),new Vector2(64,16),0);
        baseplt.create(new Vector2(0, -64), new Vector2(constants.SCREENWIDTH, 1),0);
        item = new Texture(Gdx.files.internal("items.png"));
        pause = new TextureRegion(item, 64, 64, 64, 64);

        bg = new Texture(Gdx.files.internal("background.png"));
        bgRegion = new TextureRegion(bg, 0, 0, 280, 450);
        rect = new Rectangle(   55,
                 150 ,
                64, 64);
        sr = new ShapeRenderer();
        coords = new Vector3();



    }

    @Override
    public void update(float delta) {
        character.update(delta);
        plt.update(delta);
        System.out.println(rect.x +" " + rect.y + " " + rect.width + " " + rect.height);
        System.out.println(pause.getRegionX() + " " + pause.getRegionY());

        if(plt1 != null && !plt1.isDestroyed())
            plt1.update(delta);
        if(plt1 != null && plt1.isDestroyed()){
            System.out.println("Destroyed");
            plt1 = null;
        }
    }

    @Override
    public void render(SpriteBatch sprite) {

        manager.getCamera().update();
        sprite.setProjectionMatrix(manager.getCamera().combined);

        sr.begin(ShapeRenderer.ShapeType.Filled);
//
//        sr.rect(rect.x ,
//                rect.y ,
//                rect.width ,
//                rect.height)  ;


        sr.setColor(Color.GREEN);
        sr.end();

        bounds(rect);
        drawobject(sprite);
       // System.out.println(pause.getRegionX() + " " + pause.getRegionY());
        character.render(sprite);


        plt.render(sprite);
        if(plt1 != null)
            plt1.render(sprite);
    }

    private void pause(){

    }

    private void bounds(Rectangle rect){
        if(Gdx.input.isTouched() ){
            manager.getCamera().unproject(coords.set(Gdx.input.getX(), Gdx.input.getY(),0));

            if(rect.contains(coords.x, coords.y)){
                System.out.println(coords.x + "," + coords.y);
            }

        }


    }

    private void drawobject(SpriteBatch batch){
//        batch.enableBlending();
//        batch.begin();
//        batch.draw(bgRegion,manager.getCamera().position.x - (bgRegion.getRegionWidth() /2), manager.getCamera().position.y - (bgRegion.getRegionHeight() /2));
//        batch.end();
        batch.enableBlending();
        batch.begin();
        batch.draw(pause, rect.x, rect.y, rect.width, rect.height);
       // batch.draw(pause, manager.getCamera().position.x + rect.x, manager.getCamera().position.y + rect.y, rect.width, rect.height);
        //manager.getCamera().position.x + (pause.getRegionWidth()/2) + 55,
        //                manager.getCamera().position.y + (pause.getRegionHeight()*2) + constants.PPM
        batch.end();
    }


    @Override
    public void dispose() {

    }


}
