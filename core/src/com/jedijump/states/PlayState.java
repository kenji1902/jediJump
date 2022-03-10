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

    //spring spr;
    Texture item, bg;
    TextureRegion pause, bgRegion;
    Rectangle rect;
    Vector3 coords;
    PauseState ps;
    Array<platform> platforms;
    Array<bird> birds;
    Array<debri> debris;
    Array<spring> springs;
    int y = -120;
    int birdY = 100;

    long lastScore;
    String scoreString;
    BitmapFont font;
    int coinY = 50;

    Array<coin> coins;

    public PlayState(Manager manager) {
        super(manager);
        ps = new PauseState(manager);

        character = new character(manager);
        plt = new platform(manager);
        plt1 = new platform(manager);
        baseplt = new platform(manager);
       // spr = new spring(manager);

        baseplt.create(new Vector2(0, -240), new Vector2(constants.SCREENWIDTH, 1),0);
       // spr.create(new Vector2(p.getBody().getPosition().y,89),new Vector2(18,14),1);
        character.create(new Vector2(0,0),new Vector2(32,32),1);

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
        debris = new Array<debri>();
        springs = new Array<>();

        coins = new Array<>();
        lastScore = 0;
        scoreString = "SCORE: 0";
        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        manager.setScore(0);


    }

    @Override
    public void update(float delta) {

        manager.getWorld().step(1/60f,6,2);

        LevelGenerator(delta);
        for (platform p: platforms) {

            p.update(delta);
        }
       // springGenerator(delta);
        for(spring s: springs){
            s.update(delta);
        }

        birdGenerator(delta);
        for(bird b: birds){
            b.update(delta);
        }

        debrisGenerator(delta);
        for(debri d: debris){
            d.update(delta);
        }

        coinGenerator(delta);
        for (coin c: coins) {
            c.update(delta);
        }
        if(manager.getScore() != lastScore){
            lastScore = manager.getScore();
            scoreString = "SCORE: "+ lastScore;
        }


        //debri.update(delta);
       // spr.update(delta);
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

        for(spring s: springs){
            s.render(sprite);
        }
       // spr.render(sprite);



        ps.render(sprite);
        character.render(sprite);

        for(debri d: debris){
            d.render(sprite);
        }

        for(bird b: birds){
            b.render(sprite);
        }

        for(coin c: coins) {
            c.render(sprite);
        }
        sprite.begin();
        font.draw(sprite,scoreString,-140,220+camera.position.y);
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
    private float birdSpawnTime = 10;
    private float birdCounter = 0;
    public void birdGenerator(float deltatime){
        birdSpawnTime += deltatime;

        if(birdCounter < birdSpawnTime){
            bird bird = new bird(manager);
            bird.create(new Vector2(0, birdY), new Vector2(32,32), 0);
            //birdSpawnTime = TimeUtils.nanoTime();
            birds.add(bird);
            birdY+=700;
        }
        if(birdCounter >= birdSpawnTime-1){
            birdCounter = birdSpawnTime;
        }


    }
    private float debrisSpawn = 10;
    private float debrisCounter = 0;
    public void debrisGenerator(float deltatime){

        debrisCounter += deltatime;

        if(debrisCounter > debrisSpawn){
            debri debri = new debri(manager);
            //debri = new debri(manager);
            debri.create(new Vector2(character.getBody().getPosition().x * constants.PPM, manager.getCamera().position.y + 200), new Vector2(32,32),3);
            debris.add(debri);
            debrisCounter = 0;
            System.out.println(character.getBody().getPosition().x);
        }
    }

    private float springSpawn = 5;
    private float springCounter = 0;
    public void springGenerator(float deltatime){
        springCounter += deltatime;


        if(springSpawn>springCounter){
            spring spr = new spring(manager);
            spr.create(new Vector2(plt.getBody().localVector.x, plt.getBody().localVector.y), new Vector2(18,14),1);
            springs.add(spr);
            springCounter =0;
        }

    }

    private float coinSpawnTime = 5;
    private float coinCounter = 0;
    public void coinGenerator(float deltatime){
        coinCounter += deltatime;

        if(coinCounter < coinSpawnTime){
            coin coin= new coin(manager);
            coin.create(new Vector2(MathUtils.random((-constants.SCREENWIDTH/2)+constants.FORCEFIELD, (constants.SCREENWIDTH/2)-constants.FORCEFIELD), coinY), new Vector2(32,32), 0);
            //coinSpawnTime = TimeUtils.nanoTime();
            coins.add(coin);
            coinY+=MathUtils.random(300,700);
        }
        if(coinCounter >= coinSpawnTime-1){
            coinCounter = coinSpawnTime;
        }


    }



    @Override
    public void dispose() {
        character.disposeBody();
        baseplt.disposeBody();
        for(spring s: springs){
            s.disposeBody();
        }
        for (platform plt: platforms) {
            plt.disposeBody();
        }
        for (bird b: birds){
            b.disposeBody();
        }
       for(debri d: debris){
           d.disposeBody();
        }

        for (coin coin: coins) {
            coin.disposeBody();
        }
    }


}
