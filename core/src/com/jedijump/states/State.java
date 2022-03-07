package com.jedijump.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class State {
    protected Manager manager;
    protected State(Manager manager){
        this.manager = manager;
    }
    public abstract void update(float delta);
    public abstract void render(SpriteBatch sprite);
    public abstract void dispose();
}
