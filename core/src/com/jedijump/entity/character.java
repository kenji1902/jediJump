package com.jedijump.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.jedijump.states.Manager;
import com.jedijump.utility.animation;
import com.jedijump.utility.constants;

public class character extends entity{
    animation texture;
    public character(Manager manager) {
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

        // Body of the Character
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(this.size.x, this.size.y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.shape = shape;
        fixtureDef.friction = constants.JEDISAUR_FRICTION;
        body.createFixture(fixtureDef).setUserData("body");

        // Foot of the Character
        shape.setAsBox(this.size.x/1.2f,this.size.y / 4,new Vector2(0,this.position.x - this.size.y),0);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("foot");



        shape.dispose();

        texture = new animation(new TextureRegion(new Texture(Gdx.files.internal("stand.png"))), 1 ,0.5f);
        maxPosY = body.getPosition().y;


    }

    @Override
    public void update(float delta) {
        Vector2 posxy = body.getPosition();
        if(Gdx.input.isTouched()){
            //System.out.println(posxy.x + " < " + manager.getCamera().viewportHeight);
            //System.out.println(Gdx.input.getX() + );
        }


        texture.update(delta);
        cameraUpdate();
        Input(delta);
        if(body.getPosition().x < -5){
            body.setLinearVelocity(new Vector2(50 * 10,body.getLinearVelocity().y));
        }
        if(body.getPosition().x > 5){
            body.setLinearVelocity(new Vector2(50 * -10,body.getLinearVelocity().y));
        }
    }

    private float maxPosY;
    private void cameraUpdate(){

        if(maxPosY < body.getPosition().y)
            maxPosY = body.getPosition().y;

        Vector3 position = manager.getCamera().position;
        position.x = this.position.x * constants.PPM;
        position.y = maxPosY * constants.PPM;
        manager.getCamera().position.set(position);
        manager.getCamera().update();
    }

    private boolean isDoubleJump = false;
    private void Input(float delta){
        int horizontalForce = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            horizontalForce -= 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            horizontalForce += 1;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && manager.getCl().getPlayerState() == constants.JEDISAUR_ON_GROUND){
            body.applyForceToCenter(0,constants.JEDISAUR_VELOCITY_Y,false);
            isDoubleJump = true;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && manager.getCl().getPlayerState() == constants.JEDISAUR_ON_AIR && isDoubleJump){
            body.applyForceToCenter(0,constants.JEDISAUR_VELOCITY_Y*0.4f,false);
            isDoubleJump = false;
        }



       body.setLinearVelocity(horizontalForce * 10,body.getLinearVelocity().y);
    }

    private void dead(){




//        if(posxy.y < manager.getCamera().viewportHeight){
//
//        }
    }


    @Override
    public void render(SpriteBatch spriteBatch) {

        sprite = spriteBatch;
        sprite.setProjectionMatrix(manager.getCamera().combined);
        sprite.begin();
//            sprite.draw(texture.getFrame(),
//                    body.getPosition().x * constants.PPM - ((float)texture.getFrame().getRegionWidth()/2),
//                    body.getPosition().y * constants.PPM - ((float)texture.getFrame().getRegionHeight()/2));
        sprite.end();
    }
}
