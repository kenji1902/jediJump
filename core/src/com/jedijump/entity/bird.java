package com.jedijump.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.jedijump.states.Manager;
import com.jedijump.utility.animation;
import com.jedijump.utility.constants;

public class bird extends entity{
    private animation texture;
    public bird(Manager manager) {
        super(manager);
    }

    @Override
    public void create(Vector2 position, Vector2 size, float density) {
        this.position = position;
        this.size = size;

        this.position.x /= constants.PPM;
        this.position.y /= constants.PPM;

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.KinematicBody;
        def.position.set(this.position);
        def.fixedRotation = true;
        body = manager.getWorld().createBody(def);

        this.size.x = this.size.x / constants.SCALE / constants.PPM;
        this.size.y = this.size.y / constants.SCALE / constants.PPM;

        // Body of the Character
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(this.size.x, this.size.y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        fixtureDef.friction = constants.JEDISAUR_FRICTION;
        body.createFixture(fixtureDef).setUserData("bird");

        shape.setAsBox(this.size.x,this.size.y / 4,new Vector2( 0,this.size.y),0);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = false;
        fixtureDef.friction = 0;
        body.createFixture(fixtureDef).setUserData("jumpHead");

        shape.dispose();

        TextureRegion birdTexture = manager.getItems();
        texture =  new animation(birdTexture, 0, 160 ,64,32,2,0.5f,false);
        //birdPosx = body.getPosition().x;
        isGenerated = true;
        birdSpeed = MathUtils.random(constants.BIRD_MIN_SPEED, constants.BIRD_MAX_SPEED);
    }

    @Override
    public void update(float delta) {
        if(!isDestroyed && isGenerated) {
            birdMovement(delta);
            texture.update(delta);
            deadZone();
        }
    }

    private int direction = 1;
    private float birdSpeed;
    private void birdMovement(float delta){
        if(!isDestroyed) {
            body.setLinearVelocity(birdSpeed * direction, 0);

            if (body.getPosition().x + size.x > constants.BOUNDARY) {
                if(!texture.isFlipped())
                    texture.flip();
                direction = -1;
            } else if (body.getPosition().x - size.x < -constants.BOUNDARY) {
                if(texture.isFlipped())
                    texture.flip();
                direction = 1;
            }
        }
    }

    private void deadZone() {
        OrthographicCamera camera = manager.getCamera();
        float deadZone = camera.position.y - (camera.viewportHeight / 2);
        float birdPos = body.getPosition().y * constants.PPM - (this.size.y * constants.PPM);

        if (birdPos < deadZone && !isDestroyed) {
            disposeBody();
            texture.dispose();

            System.out.println("DEAD BIRD");
        }

    }
    @Override
    public void render(SpriteBatch sprite) {
        if(!isDestroyed && isGenerated) {
            sprite.enableBlending();
            sprite.begin();
            sprite.draw(texture.getFrame(),
                    body.getPosition().x * constants.PPM - ((float) texture.getFrame().getRegionWidth() / 2),
                    body.getPosition().y * constants.PPM - ((float) texture.getFrame().getRegionHeight() / 2)
            );
            sprite.end();
        }
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }
}
