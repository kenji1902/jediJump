package com.jedijump.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.jedijump.states.Manager;
import com.jedijump.states.MenuState;
import com.jedijump.utility.animation;
import com.jedijump.utility.constants;

import java.util.HashMap;

public class spring extends entity{
    animation texture;
    public spring(Manager manager) {
        super(manager);
    }

    @Override
    public void create(Vector2 position, Vector2 size, float density) {
        this.position = position;
        this.size = size;

        this.position.x /= constants.PPM;
        this.position.y /= constants.PPM;

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(this.position);
        def.fixedRotation = true;
        body = manager.getWorld().createBody(def);

        this.size.x = this.size.x / constants.SCALE / constants.PPM;
        this.size.y = this.size.y / constants.SCALE / constants.PPM;

        // Body of the Spring
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(this.size.x, this.size.y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.shape = shape;
        fixtureDef.friction = constants.JEDISAUR_FRICTION;
        body.createFixture(fixtureDef).setUserData("springBody");

        // Head of Spring
        shape.setAsBox(this.size.x/1.2f,this.size.y / 4,new Vector2(0,this.size.y),0);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("springHead");

        shape.setAsBox(this.size.x/1.2f,this.size.y / 2,new Vector2(0,-this.size.y),0);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("springFoot");

        shape.dispose();

        TextureRegion platformTexture = manager.getItems();
        texture = new animation(platformTexture, 134, 17 ,18,14,1,0.5f,true);
        isGenerated = true;

    }

    @Override
    public void update(float delta) {
        if(!isDestroyed && isGenerated) {
            float minPosY = body.getPosition().y;
            if(minPosY > body.getPosition().y)
                minPosY = body.getPosition().y;
            texture.update(delta);
            //body.setTransform(this.position.x, minPosY, 0);
            springPlatform();
            deadZone();
        }
    }
    private void deadZone() {
        OrthographicCamera camera = manager.getCamera();
        float deadZone = camera.position.y - (camera.viewportHeight / 2);
        float springPos = body.getPosition().y * constants.PPM - (this.size.y * constants.PPM);

        if (springPos < deadZone && !isDestroyed) {
            disposeBody();
            texture.dispose();
            System.out.println("DEAD SPRING");
        }
    }
    private void springPlatform(){
        int springState = manager.getCl().getSpringStick();
        HashMap<Body,Body> springPlatform = manager.getCl().getSpringPlatform();
        if(springState == constants.SPRING_ON_PLATFORM && springPlatform.get(body) != null){
            body.setLinearVelocity(springPlatform.get(body).getLinearVelocity().x,body.getLinearVelocity().y);
        }
        else if(springState == constants.SPRING_ON_AIR){
            //body.setTransform(body.getPosition().x,body.getPosition().y,0);
            body.setLinearVelocity(body.getLinearVelocity().x,body.getLinearVelocity().y);
        }
    }
    @Override
    public void render(SpriteBatch sprite) {
        if(!isDestroyed && isGenerated) {
            sprite.enableBlending();
            sprite.begin();
            sprite.draw(texture.getFrame(),
                    body.getPosition().x * constants.PPM - ((float) texture.getFrame().getRegionWidth() / 2),
                    body.getPosition().y * constants.PPM - ((float) texture.getFrame().getRegionHeight() / 2));
            sprite.end();
        }
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }
}
