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
import com.badlogic.gdx.utils.TimeUtils;
import com.jedijump.entity.*;
import com.jedijump.utility.constants;

import java.util.ArrayList;
import java.util.Map;

public class PlayState extends State{
    debri debri;
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
    int birdY = 100;

    public PlayState(Manager manager) {
        super(manager);
        character = new character(manager);
        plt = new platform(manager);
        plt1 = new platform(manager);
        baseplt = new platform(manager);
        debri = new debri(manager);
        bird = new bird(manager);
        spr = new spring(manager);
        character.create(new Vector2(0,0),new Vector2(32,32),1);
        ps = new PauseState(manager);
        baseplt.create(new Vector2(0, -240), new Vector2(constants.SCREENWIDTH, 1),0);
//        bird.create(new Vector2(30,50),new Vector2(32,32),1);
        spr.create(new Vector2(-42,89),new Vector2(18,14),1);
        debri.create(new Vector2(30,240),new Vector2(32,32),3);
        item = new Texture(Gdx.files.internal("items.png"));
        pause = new TextureRegion(item, 64, 64, 64, 64);

        bg = new Texture(Gdx.files.internal("background.png"));
        bgRegion = new TextureRegion(bg, 0, 0, 280, 450);
        rect = new Rectangle(55,
                 150 ,
                64, 64);
        coords = new Vector3();
        platforms = new Array<>();
        birds = new Array<bird>();

    }

    @Override
    public void update(float delta) {

        manager.getWorld().step(1/60f,6,2);

        LevelGenerator(delta);
        for (platform p: platforms) {
            p.update(delta);
        }

        birdGenerator(delta);
        for(bird b: birds){
            b.update(delta);
        }


        debri.update(delta);
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
        debri.render(sprite);

        ps.render(sprite);
        character.render(sprite);
        
        for(bird b: birds){
            b.render(sprite);
        }

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
    private float birdSpawnTime = 5;
    private float birdCounter = 0;
    public void birdGenerator(float deltatime){
        birdCounter += deltatime;

        if(birdCounter < birdSpawnTime){
            bird = new bird(manager);
            bird.create(new Vector2(0, birdY), new Vector2(32,32), 0);
            birds.add(bird);
            birdY+=500;
        }
        if(birdCounter>=birdSpawnTime-1){
            birdCounter = birdSpawnTime;
        }


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
