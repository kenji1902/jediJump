package com.jedijump.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Stack;


public class Manager {
    private World world;
    private OrthographicCamera camera;
    private Stack<State> states;
    public Manager(){
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

    public void dispose() {
        states.peek().dispose();
    }

    public void disposeAll(){
        for (State s: states) {
            s.dispose();
        }
    }




}
