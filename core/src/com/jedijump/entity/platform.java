package com.jedijump.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.jedijump.states.Manager;
import com.jedijump.utility.animation;
import com.jedijump.utility.constants;

import java.util.Random;

public class platform extends entity{
    private animation texture;
    private Random rand;
    private int platformState;
    private boolean isFixed = false;
    private boolean isMoving = false;
    public platform(Manager manager) {
        super(manager);
        rand = new Random();
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

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(this.size.x, this.size.y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.shape = shape;
        fixtureDef.friction = 0;

        body.createFixture(fixtureDef).setUserData("platform");
        shape.dispose();

        platformState = isFixed? 0 : MathUtils.random(0,1);
        TextureRegion platformTexture = new TextureRegion(new Texture(Gdx.files.internal("items.png")));
        if(platformState == constants.PLATFORM_STATIC)
            texture = new animation(platformTexture, 64, 160 ,64,16,1,0.5f,true);
        else
            texture = new animation(platformTexture, 64, 160 ,64,64,4,0.5f,true);

        isMoving =  MathUtils.randomBoolean();;
    }

    @Override
    public void update(float delta) {
        if(!isDestroyed) {
            updateAnimation(delta);
            if(isMoving)
                platformMovement(delta);
        }
    }

    @Override
    public void render(SpriteBatch sprite) {
        if(!isDestroyed) {
            sprite.enableBlending();
            sprite.begin();
                sprite.draw(texture.getFrame(),
                        body.getPosition().x * constants.PPM - ((float) texture.getFrame().getRegionWidth() / 2),
                        body.getPosition().y * constants.PPM - ((float) texture.getFrame().getRegionHeight() / 2));
            sprite.end();
        }
    }

    private int direction = 1;
    private void platformMovement(float delta){

        body.setLinearVelocity( constants.PLATFORM_SPEED * direction,0);

        if(body.getPosition().x + size.x > constants.BOUNDARY - constants.FORCEFIELD ){
            direction = -1;
        }else if(body.getPosition().x - size.x < -constants.BOUNDARY + constants.FORCEFIELD) {
            direction = 1;
        }
    }


    private void updateAnimation(float delta){
        if(platformState == constants.PLATFORM_BREAK
                && manager.getCl().getPlayerState() == constants.JEDISAUR_ON_GROUND
                && body == manager.getCl().getPlatform()
                && texture.getCurrFrame() < texture.getFrameCount()-1
            ) {
                texture.update(delta);
            if(texture.getCurrFrame() == texture.getFrameCount()-1 && !isDestroyed) {
                texture.dispose();
                disposeBody();
            }
        }
        if(platformState == constants.PLATFORM_STATIC)
            texture.update(delta);
        OrthographicCamera camera = manager.getCamera();
        float deadZone = camera.position.y - (camera.viewportHeight / 2);
        float platformPos = body.getPosition().y * constants.PPM - (this.size.y * constants.PPM);

        if (platformPos < deadZone && !isDestroyed) {
            texture.dispose();
            disposeBody();
        }

    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void setFixed(boolean fixed) {
        isFixed = fixed;
    }
}
