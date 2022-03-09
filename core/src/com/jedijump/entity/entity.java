package com.jedijump.entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.jedijump.states.Manager;

public abstract class entity {
       protected Manager manager;
       protected int density;
       protected Body body;
       protected Vector2 position;
       protected Vector2 size;
       protected SpriteBatch sprite;
       protected boolean isDestroyed = false;

       public entity(Manager manager){
           this.manager = manager;
       }
       public abstract void create(Vector2 position, Vector2 size, float density);
       public abstract void update(float delta);
       public abstract void render(SpriteBatch sprite);
       public void disposeBody(){
              if(!isDestroyed) {
                     manager.getWorld().destroyBody(body);
                     isDestroyed = true;
              }
       }

}
