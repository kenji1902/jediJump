package com.jedijump.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.jedijump.entity.*;
import com.jedijump.utility.constants;

public class PlayState extends State{
    character character;
    platform plt, plt1, baseplt;
    bird bird;
    spring spr;
    Texture item, bg;
    coin coin;
    TextureRegion pause, bgRegion;
    Rectangle rect;
    ShapeRenderer sr;
    Vector3 coords;
    PauseState ps;


    public PlayState(Manager manager) {
        super(manager);
        character = new character(manager);
        plt = new platform(manager);
        plt1 = new platform(manager);
        baseplt = new platform(manager);
        bird = new bird(manager);
        spr = new spring(manager);
        coin = new coin(manager);

        coin.create(new Vector2(0,-70),new Vector2(32,32),1);
        character.create(new Vector2(0,0),new Vector2(32,32),1);
        plt.setFixed(true);
        plt.create(new Vector2(0,-36),new Vector2(64,16),0);
        ps = new PauseState(manager);
        plt1.create(new Vector2(-20,82),new Vector2(64,16),0);
        baseplt.create(new Vector2(0, -240), new Vector2(constants.SCREENWIDTH, 1),0);
        bird.create(new Vector2(30,50),new Vector2(32,32),1);
        spr.create(new Vector2(-42,89),new Vector2(18,14),1);

        item = new Texture(Gdx.files.internal("items.png"));
        pause = new TextureRegion(item, 64, 64, 64, 64);

        bg = new Texture(Gdx.files.internal("background.png"));
        bgRegion = new TextureRegion(bg, 0, 0, 280, 450);
        rect = new Rectangle(55,
                 150 ,
                64, 64);
        sr = new ShapeRenderer();
        coords = new Vector3();



    }

    @Override
    public void update(float delta) {
        manager.getWorld().step(1/60f,6,2);
        bird.update(delta);
        spr.update(delta);
        character.update(delta);
        coin.update(delta);
        plt.update(delta);
//        System.out.println(rect.x +" " + rect.y + " " + rect.width + " " + rect.height);
//        System.out.println(pause.getRegionX() + " " + pause.getRegionY());


        plt1.update(delta);
//        if(plt1 != null && plt1.isDestroyed()){
//            System.out.println("Destroyed");
//            plt1 = null;
//        }
    }

    @Override
    public void render(SpriteBatch sprite) {
        manager.getCamera().update();
        sprite.setProjectionMatrix(manager.getCamera().combined);
        spr.render(sprite);
        coin.render(sprite);

        sr.setProjectionMatrix(manager.getCamera().combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.rect(rect.x, rect.y, rect.width, rect.height);
            sr.setColor(Color.GREEN);
        sr.end();

        drawobject(sprite);

        bounds(rect);
        ps.render(sprite);
        character.render(sprite);

        bird.render(sprite);
        plt.render(sprite);
        if(plt1 != null)
            plt1.render(sprite);

    }

    public void LevelGenerator(){

    }

    private void bounds(Rectangle rect){
        if(Gdx.input.isTouched() ){
            manager.getCamera().unproject(coords.set(Gdx.input.getX(), Gdx.input.getY(),0));

            if(rect.contains(coords.x, coords.y)){
            }
        }
    }

    private void drawobject(SpriteBatch batch){

        OrthographicCamera camera = manager.getCamera();
        rect.y = 150 + camera.position.y;
        batch.begin();
            batch.draw(pause,  rect.x, rect.y, rect.width, rect.height);
        batch.end();
    }


    @Override
    public void dispose() {
        character.disposeBody();
        plt.disposeBody();
        plt1.disposeBody();
        baseplt.disposeBody();
        bird.disposeBody();
        spr.disposeBody();
    }


}
