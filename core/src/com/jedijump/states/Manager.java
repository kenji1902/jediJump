package com.jedijump.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.jedijump.entity.contactListener;
import com.jedijump.utility.constants;

import java.util.Stack;


public class Manager {
    private final World world;
    private final OrthographicCamera camera;
    private final Stack<State> states;
    private contactListener cl;
    private Box2DDebugRenderer b2dr;

    public Manager(){
        cl = new contactListener();
        b2dr = new Box2DDebugRenderer();

        world = new World(new Vector2(0,-20),false);
        world.setContactListener(cl);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, constants.SCREENWIDTH,constants.SCREENHEIGHT);

        states = new Stack<State>();
    }
    public void push(State state){
        states.push(state);
        System.out.println(state + " has been pushed!");
//        for (State s: states) {
//            System.out.println(s);
//        }
    }
    public void pop(){
        states.pop();
        System.out.println(states + " has been popped!");
    }
    public void set(State state){
        System.out.println(state + "has been set");
        dispose();
        states.pop();
        states.push(state);

    }
    public void update(float delta){
        b2dr.render(world,camera.combined.scl(constants.PPM));
        world.step(1/60f,6,2);
        states.peek().update(delta);
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
