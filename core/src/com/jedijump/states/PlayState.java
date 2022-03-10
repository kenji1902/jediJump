package com.jedijump.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.jedijump.entity.bird;
import com.jedijump.entity.character;
import com.jedijump.entity.platform;
import com.jedijump.entity.spring;
import com.jedijump.utility.constants;

import java.util.ArrayList;
import java.util.Map;

public class PlayState extends State{
    character character;
    platform plt, plt1, baseplt;
    bird bird;
    spring spr;
    Texture item, bg;
    TextureRegion pause, bgRegion;
    Rectangle rect;
    Vector3 coords;
    PauseState ps;
    Array<platform> platforms;
    Array<bird> birds;
    int y = -120;

    public PlayState(Manager manager) {
        super(manager);
        character = new character(manager);
        plt = new platform(manager);
        plt1 = new platform(manager);
        baseplt = new platform(manager);
        bird = new bird(manager);
        spr = new spring(manager);
        character.create(new Vector2(0,0),new Vector2(32,32),1);
        ps = new PauseState(manager);
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
        coords = new Vector3();
        platforms = new Array<>();
        //birds = new Array<>();

    }

    @Override
    public void update(float delta) {

        manager.getWorld().step(1/60f,6,2);

        LevelGenerator(delta);
        for (platform p: platforms) {
            p.update(delta);
        }


        bird.update(delta);
        //birdGenerator(delta);
        spr.update(delta);
        character.update(delta);
    }

    @Override
    public void render(SpriteBatch sprite) {
        OrthographicCamera camera = manager.getCamera();
        manager.getCamera().update();
        sprite.setProjectionMatrix(manager.getCamera().combined);


        sprite.disableBlending();
        sprite.begin();
            sprite.draw(bgRegion,camera.position.x - 160,camera.position.y - 240, constants.SCREENWIDTH, constants.SCREENHEIGHT);
        sprite.end();



        for (platform p: platforms) {
            p.render(sprite);
        }

        spr.render(sprite);
        ps.render(sprite);
        character.render(sprite);
        bird.render(sprite);
    }

    private float MAX = 5;
    private float counter = 0;
    public void LevelGenerator(float deltatime){
        counter += deltatime;
        if(counter < MAX){
            platform plt = new platform(manager);
            plt.create(new Vector2(MathUtils.random(-constants.SCREENWIDTH/2, constants.SCREENWIDTH/2),   y), new Vector2(64,16), 0);
            platforms.add(plt);
            y+=100;
        }
        if(counter >= MAX-1){
            counter = MAX;
        }
    }

    public void birdGenerator(float deltatime){
//        counter += deltatime;
//        if(counter < MAX){
//            bird = new bird(manager);
//            bird.create(new Vector2(0,MathUtils.random(-constants.SCREENHEIGHT/2, constants.SCREENHEIGHT/2)), new Vector2(64,32), 0);
//            birds.add(bird);
//            bird.update(deltatime);
//        }
//        if(counter >= MAX-1){
//            counter = MAX;
//        }

    }

    @Override
    public void dispose() {
        character.disposeBody();
        baseplt.disposeBody();
        bird.disposeBody();
        spr.disposeBody();
        for (platform plt: platforms) {
            plt.disposeBody();
        }
    }


}
