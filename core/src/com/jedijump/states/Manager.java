package com.jedijump.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.jedijump.entity.*;
import com.jedijump.utility.constants;
import com.jedijump.utility.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Stack;


public class Manager {
    private final World world;
    private final OrthographicCamera camera;
    private final Stack<State> states;
    private contactListener cl;
    private Box2DDebugRenderer b2dr;
    private TextureRegion items;
    private int score = 0;
    private float distance = 0;
    public Stack<platform> deletedPlatform;
    public Stack<spring> deletedSprings;
    public Stack<debri> deletedDebris;
    public Stack<coin> deletedCoins;
    public Stack<bird> deletedBird;
    private float difficultyMultiplier = 1;
    private database db;


    public Manager(){
        db = new database("highscore.db");
        cl = new contactListener();
        b2dr = new Box2DDebugRenderer();

        world = new World(new Vector2(0,-20),false);
        world.setContactListener(cl);

        camera = new OrthographicCamera();


        camera.setToOrtho(false, constants.SCREENWIDTH,constants.SCREENHEIGHT);
        if(constants.DEBUG_MODE)
            camera.zoom = constants.ZOOM;

        items = new TextureRegion(new Texture(Gdx.files.internal("items.png")));

        states = new Stack<State>();

        deletedPlatform = new Stack<>();
        deletedSprings = new Stack<>();
        deletedDebris = new Stack<>();
        deletedCoins = new Stack<>();
        deletedBird = new Stack<>();

        db.query("CREATE TABLE HIGHSCORE" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "COOKIE INT NOT NULL," +
                "DISTANCE FLOAT NOT NULL);");
    }
    public void push(State state){
        states.push(state);
    }
    public void pop(){
        dispose();
        states.pop();
    }
    public void set(State state){
        dispose();
        states.pop();
        states.push(state);
    }
    public void update(float delta){
        b2dr.render(world,camera.combined.scl(constants.PPM));
        states.peek().update(delta);
        //System.out.println(states);
    }

    public void render(SpriteBatch sprite){

        states.peek().render(sprite);
    }

    public void dispose(){
        states.peek().dispose();
    }
    public void disposeAll(){
        for(State s : states)
            s.dispose();
        world.dispose();

    }

    public TextureRegion getItems() {
        return items;
    }

    public World getWorld() {
        return world;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public contactListener getCl() {
        return cl;
    }

    public int getScore() {

        return score;
    }

    public void setScore(int score) {

        this.score = score;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getDifficultyMultiplier() {
        return difficultyMultiplier;
    }

    public void setDifficultyMultiplier(int difficultyMultiplier) {
        this.difficultyMultiplier = difficultyMultiplier;
    }

    public database getDatabase(){
        return db;
    }
}
