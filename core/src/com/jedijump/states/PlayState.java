package com.jedijump.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    character character;
    platform plt, plt1, baseplt;
    bird bird;
    spring spr;
    coin coin;
    Texture item, bg;
    TextureRegion pause, bgRegion;
    Rectangle rect;
    Vector3 coords;
    PauseState ps;
    Array<platform> platforms;
    Array<bird> birds;
    Array<coin> coins;
    int y = -120;
    int birdY = 100;

    long lastScore;
    String scoreString;
    BitmapFont font;


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
//        bird.create(new Vector2(30,50),new Vector2(32,32),1);
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
        birds = new Array<bird>();

        coin = new coin(manager);
        coin.create(new Vector2(-42,20),new Vector2(18,14),1);

        coins = new Array<>();
        lastScore = 0;
        scoreString = "SCORE: 0";
        font = new BitmapFont(Gdx.files.internal("font.fnt"));


    }

    @Override
    public void update(float delta) {

        manager.getWorld().step(1/60f,6,2);
//        coinGenerator(delta);
        LevelGenerator(delta);
        for (platform p: platforms) {
            p.update(delta);
        }

//        birdGenerator(delta);
//        for(bird b: birds){
//            b.update(delta);
//        }


        spr.update(delta);
        character.update(delta);

        coin.update(delta);
//        for (coin c: coins) {
//            c.update(delta);
//        }

        if(coin.getScore() != lastScore){
            lastScore = coin.getScore();
            scoreString = "SCORE: "+ lastScore;
        }

    }

    @Override
    public void render(SpriteBatch sprite) {
        OrthographicCamera camera = manager.getCamera();
        manager.getCamera().update();
        sprite.setProjectionMatrix(manager.getCamera().combined);


        sprite.disableBlending();
        sprite.begin();
//            sprite.draw(bgRegion,camera.position.x - 160,camera.position.y - 240, constants.SCREENWIDTH, constants.SCREENHEIGHT);
        sprite.end();

        for (platform p: platforms) {
                p.render(sprite);
        }
        spr.render(sprite);

        drawobject(sprite);
        bounds(rect);

        ps.render(sprite);
        character.render(sprite);


        for(bird b: birds){
            b.render(sprite);
        }

        coin.render(sprite);


        sprite.begin();
        font.draw(sprite,scoreString,-150,220+camera.position.y);
        sprite.end();


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
    private float coinSpawnTime = 10;
    private float coinCounter = 0;

    public void birdGenerator(float deltatime){
        birdSpawnTime += deltatime;

        if(birdCounter < birdSpawnTime){
            bird = new bird(manager);
            bird.create(new Vector2(0, birdY), new Vector2(64,32), 0);
            //birdSpawnTime = TimeUtils.nanoTime();
            birds.add(bird);
            birdY+=700;
        }
        if(birdCounter >= birdSpawnTime-1){
            birdCounter = birdSpawnTime;
        }
    }

    public void coinGenerator(float deltatime){
//        counter += deltatime;
//
//        if(counter < MAX){
//            coin = new coin(manager);
//            coin.create(new Vector2(MathUtils.random(-manager.getCamera().position.y+constants.SCREENWIDTH/2, manager.getCamera().position.y+constants.SCREENWIDTH/2),   y), new Vector2(64,16), 0);
//
//            coins.add(coin);
//            coin.update(deltatime);
//            y+=100;
//        }
//        if(counter >= MAX-1){
//            counter = MAX;
//        }

        coinSpawnTime += deltatime;

        if(coinCounter < coinSpawnTime){
            coin = new coin(manager);
            coin.create(new Vector2(0, birdY), new Vector2(64,32), 0);
            //coinSpawnTime = TimeUtils.nanoTime();
            coins.add(coin);
            birdY+=700;
        }
        if(coinCounter >= coinSpawnTime-1){
            coinCounter = coinSpawnTime;
        }

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

        batch.enableBlending();
        batch.begin();
            batch.draw(pause,  rect.x, rect.y, rect.width, rect.height);
        batch.end();
    }


    @Override
    public void dispose() {
        character.disposeBody();
        baseplt.disposeBody();
        spr.disposeBody();
        for (platform plt: platforms) {
            plt.disposeBody();
        }
        for (bird b: birds){
            b.disposeBody();
        }

        for (coin coin: coins){
            coin.disposeBody();
        }
    }


}
