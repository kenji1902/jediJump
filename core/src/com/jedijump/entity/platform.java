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
    private animation texture, pebbles;
    private Random rand;
    private int platformState;
    private boolean isFixed = false;
    private boolean isMoving = false;
    private boolean isFloor = false;
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
        TextureRegion floorTexture = manager.getItems2();

        if(!isFloor) {
            int probability = MathUtils.random(0,20);
            if(probability < constants.PLATFORM_BREAK_PROBABILITY - (manager.getDifficultyMultiplier() * 1.4f))
                probability = 0;
            else
                probability = 1;
            platformState = isFixed? 0 : probability;
            TextureRegion platformTexture = manager.getItems();

            if (platformState == constants.PLATFORM_STATIC)
                texture = new animation(platformTexture, 64, 160, 64, 16, 1, 0.5f, true);
            else
                texture = new animation(platformTexture, 64, 160, 64, 64, 4, 0.5f, true);

            pebbles = new animation(floorTexture,337,409,45,105,3,0.5f,true);
        }
        else {
            texture = new animation(floorTexture, 0,430,320,82,1,1,false);
        }
        isMoving =  MathUtils.randomBoolean();
        isGenerated = true;
    }

    @Override
    public void update(float delta) {
        if(!isDestroyed && isGenerated) {
            updateAnimation(delta);
            if(isMoving)
                platformMovement(delta);
        }

    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        if(!isDestroyed && isGenerated) {
            sprite = spriteBatch;
            sprite.enableBlending();
            sprite.begin();
                sprite.draw(texture.getFrame(),
                        body.getPosition().x * constants.PPM - ((float) texture.getFrame().getRegionWidth() / 2),
                        body.getPosition().y * constants.PPM - ((float) texture.getFrame().getRegionHeight() / 2));
                if(!isFloor)
                    sprite.draw(pebbles.getFrame(),
                            body.getPosition().x * constants.PPM - ((float) pebbles.getFrame().getRegionWidth() / 2),
                            (body.getPosition().y - (this.size.y + 0.5f) ) * constants.PPM - ((float) pebbles.getFrame().getRegionHeight() / 2));

            sprite.end();
        }
    }

    private int direction = 1;
    private void platformMovement(float delta){
        if(!isDestroyed) {
            body.setLinearVelocity(constants.PLATFORM_SPEED * direction *(manager.getDifficultyMultiplier()/2), 0);

            if (body.getPosition().x + size.x > constants.BOUNDARY - constants.FORCEFIELD) {
                direction = -1;
            } else if (body.getPosition().x - size.x < -constants.BOUNDARY + constants.FORCEFIELD) {
                direction = 1;
            }
        }
    }


    private void updateAnimation(float delta){
        if(platformState == constants.PLATFORM_BREAK
                && manager.getCl().getPlayerState() == constants.JEDISAUR_ON_GROUND
                && body == manager.getCl().getPlatform()
                && texture.getCurrFrame() < texture.getFrameCount()-1
            ) {
            texture.update(delta);
            if(texture.getCurrFrame() == texture.getFrameCount()-1 && !isDestroyed){
                texture.dispose();
                pebbles.dispose();
                disposeBody();
            }
        }
        if(platformState == constants.PLATFORM_STATIC)
            texture.update(delta);
        pebbles.update(delta);
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

    public void setFloor(boolean isFloor){
        this.isFloor = isFloor;
    }
}
