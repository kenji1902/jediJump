package com.jedijump.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
        fixtureDef.friction = constants.JEDISAUR_FRICTION;
        body.createFixture(fixtureDef).setUserData("bird");

        shape.dispose();

        texture = new animation(new TextureRegion(new Texture(Gdx.files.internal("stand.png"))), 1 ,0.5f);
        birdPosx = body.getPosition().x * constants.PPM;
    }

    @Override
    public void update(float delta) {
        birdMovement(delta);
    }
    private float birdPosx;
    private int direction = 1;
    float max = 0;
    float min = 0;
    private void birdMovement(float delta){

        birdPosx += constants.BIRD_SPEED * direction;
        body.setLinearVelocity(constants.BIRD_SPEED * direction,0);
        if(min > birdPosx)
            min = birdPosx;
        if(max < birdPosx)
            max = birdPosx;

        System.out.println(min + " "+ max);

        if(birdPosx > constants.SCREENWIDTH - constants.BIRD_SPEED){
            direction = -1;
        }else if(birdPosx < -constants.SCREENWIDTH + constants.BIRD_SPEED) {
            direction = 1;
        }
    }
    @Override
    public void render(SpriteBatch sprite) {

    }
}
