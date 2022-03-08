package com.jedijump.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.jedijump.entity.contactListener;
import com.jedijump.utility.constants;

import java.util.Stack;


public class Manager {
    private final World world;
    private final OrthographicCamera camera;
    private final Stack<State> states;
    private contactListener cl;
    public Manager(){
        cl = new contactListener();

        world = new World(new Vector2(0,-20),false);
        world.setContactListener(cl);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, constants.SCREENWIDTH,constants.SCREENHEIGHT);

        states = new Stack<State>();
    }
    public void push(State state){
        states.push(state);
    }
    public void pop(){
        states.pop();
    }
    public void set(State state){
        states.pop();
        states.push(state);
    }
    public void update(float delta){
        states.peek().update(delta);
    }

    public void render(SpriteBatch sprite){
        states.peek().render(sprite);
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
}
