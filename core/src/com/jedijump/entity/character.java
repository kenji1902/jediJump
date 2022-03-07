package com.jedijump.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.jedijump.states.Manager;

public class character extends entity{
    character(Manager manager) {
        super(manager);
    }

    @Override
    public void create() {
        BodyDef def = new BodyDef();

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(SpriteBatch sprite) {

    }
}
