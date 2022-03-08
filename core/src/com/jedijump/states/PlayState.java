package com.jedijump.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.jedijump.entity.character;
import com.jedijump.entity.platform;

public class PlayState extends State{
    character character;
    platform plt;
    public PlayState(Manager manager) {
        super(manager);
        character = new character(manager);
        plt = new platform(manager);

        character.create(new Vector2(0,0),new Vector2(32,32),1);
        plt.create(new Vector2(0,-36),new Vector2(80,32),0);
    }

    @Override
    public void update(float delta) {
        character.update(delta);
        plt.update(delta);
    }

    @Override
    public void render(SpriteBatch sprite) {
        character.render(sprite);
        plt.render(sprite);
    }

    @Override
    public void dispose() {

    }
}
