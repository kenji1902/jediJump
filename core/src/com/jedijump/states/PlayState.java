package com.jedijump.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.jedijump.entity.character;
import com.jedijump.entity.platform;
import com.jedijump.utility.constants;

public class PlayState extends State{
    character character;
    platform plt, plt1, base;

    public PlayState(Manager manager) {
        super(manager);
        character = new character(manager);
        plt = new platform(manager);
        plt1 = new platform(manager);
        base = new platform(manager);

        character.create(new Vector2(0,0),new Vector2(32,32),1);
        plt.create(new Vector2(0,-36),new Vector2(64,16),0);
        plt1.create(new Vector2(-20,82),new Vector2(64,16),0);
        base.create(new Vector2(0,-240),new Vector2(constants.SCREENWIDTH,1),1);
    }

    @Override
    public void update(float delta) {
        character.update(delta);
        plt.update(delta);
        if(plt1 != null && !plt1.isDestroyed())
            plt1.update(delta);
        if(plt1 != null && plt1.isDestroyed()){
            System.out.println("Destroyed");
            plt1 = null;
        }
    }

    @Override
    public void render(SpriteBatch sprite) {
        character.render(sprite);
        plt.render(sprite);
        if(plt1 != null)
            plt1.render(sprite);
    }

    @Override
    public void dispose() {

    }


}
