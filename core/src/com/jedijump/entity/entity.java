package com.jedijump.entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jedijump.states.Manager;

public abstract class entity {
       private Manager manager;
       entity(Manager manager){
           this.manager = manager;
       }
       public abstract void create();
       public abstract void update(float delta);
       public abstract void render(SpriteBatch sprite);
}
