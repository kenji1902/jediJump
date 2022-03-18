package com.jedijump.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
    platform baseplt;

    //spring spr;
    Texture item, bg;
    TextureRegion pause, bgRegion;
    Rectangle rect;
    Vector3 coords;
    PauseState ps;

    ArrayList<platform> platforms;
    ArrayList<bird> birds;
    ArrayList<debri> debris;
    ArrayList<spring> springs;
    ArrayList<coin> coins;

    int platformY = -120;
    int birdY = 100;
    int coinY = 50;

    long lastScore;
    float lastDistance;
    String scoreString, distanceString;
    BitmapFont font;

    Sound coinSound;


    public PlayState(Manager manager) {
        super(manager);
        ps = new PauseState(manager);
        character = new character(manager);
        baseplt = new platform(manager);
       // spr = new spring(manager);

        baseplt.setFloor(true);
        baseplt.setFixed(true);
        baseplt.create(new Vector2(0, -206), new Vector2(constants.SCREENWIDTH, 1),0);
       // spr.create(new Vector2(p.getBody().getPosition().y,89),new Vector2(18,14),1);
        character.create(new Vector2(0,0),new Vector2(20,32),1.6f);

        item = new Texture(Gdx.files.internal("items.png"));
        pause = new TextureRegion(item, 64, 64, 64, 64);

        bg = new Texture(Gdx.files.internal("background.png"));
        bgRegion = new TextureRegion(bg, 0, 0, 280, 450);
        rect = new Rectangle(55,
                 150 ,
                64, 64);

        coords = new Vector3();

        platforms = new ArrayList<>();
        birds = new ArrayList<bird>();
        debris = new ArrayList<debri>();
        springs = new ArrayList<>();

        coins = new ArrayList<>();
        lastScore = 0;
        scoreString = "SCORE: 0";
        lastDistance = 0;
        distanceString = "DISTANCE: 0";
        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        manager.setScore(0);
        manager.setDistance(0);
        coinSound = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));
    }

    @Override
    public void update(float delta) {
        OrthographicCamera camera = manager.getCamera();
        manager.getWorld().step(1/60f,6,2);
        character.update(delta);

        platformGenerator(delta);

        for(platform p : platforms)
            p.update(delta);
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
            coinSound.play();
        }
        manager.setDistance(camera.position.y);
        distanceString = "DISTANCE: " + (manager.getDistance());


        //debri.update(delta);
       // spr.update(delta);
//        System.out.println(
//                "Platform: "+platforms.size()+
//                " Springs: "+springs.size()+
//                " Debris: "+debris.size()+
//                " Coins: "+coins.size()+
//                " Bird: "+birds.size());

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

        font.draw(sprite,distanceString,-140,190+camera.position.y);
        sprite.end();
        baseplt.render(sprite);
        character.render(sprite);
    }

    private float counter = 0;
    public void platformGenerator(float deltatime){

        if((float) platformY/constants.PPM < character.getWorldHeight() ) {
            counter += deltatime;
            if (counter < constants.MAX_LEVEL_HEIGHT) {
//                System.out.println(platforms.size());
//                System.out.println(platformY/constants.PPM+ "<" + character.getWorldHeight());
                platform plt = new platform(manager);
                plt.create(new Vector2(MathUtils.random(-constants.SCREENWIDTH / 2, constants.SCREENWIDTH / 2), platformY), new Vector2(64, 16), 0);
                springGenerator(deltatime,plt.getBody().getPosition());
                platforms.add(plt);
                platformY += MathUtils.random(80,150);
            }
            if (counter >= constants.MAX_LEVEL_HEIGHT - 1) {
                counter = constants.MAX_LEVEL_HEIGHT;
            }
        }
        // Delete destroyed bodies
        for(int i = 0 ; i < manager.deletedPlatform.size(); i++){
            platforms.remove(manager.deletedPlatform.peek());
            manager.deletedPlatform.pop();
        }
        for(int i = 0 ; i < manager.deletedSprings.size(); i++){
            springs.remove(manager.deletedSprings.peek());
            manager.deletedSprings.pop();
        }

    }

    private float springCounter = 0;
    public void springGenerator(float deltatime,Vector2 postion){
        springCounter += deltatime;
        //System.out.println(springCounter + " > " + springSpawn);
        if(springCounter>constants.SPRING_SPAWN_TIME){
            spring spr = new spring(manager);
            postion.x = postion.x * constants.PPM;
            postion.y = (postion.y * constants.PPM) + ((32/2) );
            spr.create(postion, new Vector2(18,14),1);
            springs.add(spr);
            springCounter =0;
        }

    }

    private float birdCounter = 0;
    public void birdGenerator(float deltatime){
        if((float) birdY/constants.PPM < character.getWorldHeight() ) {
            birdCounter += deltatime;
            if (birdCounter < constants.MAX_LEVEL_HEIGHT) {
                bird bird = new bird(manager);
                bird.create(new Vector2(0, birdY), new Vector2(32, 32), 0);
                //birdSpawnTime = TimeUtils.nanoTime();
                birds.add(bird);
                birdY += MathUtils.random(500,810);
            }
            if (birdCounter >= constants.MAX_LEVEL_HEIGHT - 1) {
                birdCounter = constants.MAX_LEVEL_HEIGHT;
            }
        }
        // Delete destroyed bodies
        for(int i = 0 ; i < manager.deletedBird.size(); i++){
            birds.remove(manager.deletedBird.peek());
            manager.deletedBird.pop();
        }


    }
    private float debrisCounter = 0;
    public void debrisGenerator(float deltatime){

        debrisCounter += deltatime;

        if(debrisCounter > constants.DEBRIS_SPAWN_TIME){
            debri debri = new debri(manager);
            //debri = new debri(manager);
            debri.create(new Vector2(character.getBody().getPosition().x * constants.PPM, manager.getCamera().position.y + 200), new Vector2(32,32),constants.DEBRI_SPEED* manager.getDifficultyMultiplier());
            debris.add(debri);
            debrisCounter = 0;
//            System.out.println(character.getBody().getPosition().x);
        }
        for(int i = 0 ; i < manager.deletedDebris.size(); i++){
            debris.remove(manager.deletedDebris.peek());
            manager.deletedDebris.pop();
        }
    }




    private float coinCounter = 0;
    public void coinGenerator(float deltatime){
        if((float) coinY/constants.PPM < character.getWorldHeight() ) {

            coinCounter += deltatime;

            if (coinCounter < constants.MAX_LEVEL_HEIGHT) {
                coin coin = new coin(manager);
                coin.create(new Vector2(MathUtils.random((-constants.SCREENWIDTH / 2) + constants.FORCEFIELD, (constants.SCREENWIDTH / 2) - constants.FORCEFIELD), coinY), new Vector2(32, 32), 0);
                coins.add(coin);
                coinY += MathUtils.random(100, 450);
            }
            if (coinCounter >= constants.MAX_LEVEL_HEIGHT - 1) {
                coinCounter = constants.MAX_LEVEL_HEIGHT;
            }

        }
        for(int i = 0 ; i < manager.deletedCoins.size(); i++){
            coins.remove(manager.deletedCoins.peek());
            manager.deletedCoins.pop();
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
