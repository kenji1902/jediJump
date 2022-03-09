package com.jedijump.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.jedijump.states.Manager;
import com.jedijump.states.MenuState;
import com.jedijump.states.PauseState;
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
        shape.setAsBox(this.size.x/1.2f,this.size.y / 4,new Vector2( 0,-this.size.y),0);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("foot");



        shape.dispose();

        texture = new animation(new TextureRegion(new Texture(Gdx.files.internal("stand.png"))), 1 ,0.5f);
        maxPosY = body.getPosition().y;
    }
    @Override
    public void update(float delta) {
        if(!isDestroyed) {
            texture.update(delta);
            cameraUpdate();
            Input(delta);
            deadZone();
            springBoost(delta);
        }
    }
    private void deadZone(){
        OrthographicCamera camera = manager.getCamera();
        float deadZone =  camera.position.y - (camera.viewportHeight/2);
        float charPos = body.getPosition().y  * constants.PPM - (this.size.y * constants.PPM);

        if(charPos < deadZone){
            camera.setToOrtho(false);

           manager.set(new MenuState(manager));
        }
        if(manager.getCl().getPlayerState() == constants.JEDISAUR_BIRD_HIT){
            System.out.println("Dead");
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
    private boolean onPush = false;
    private void Input(float delta){

        //Control
        int horizontalForce = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            horizontalForce -= 1;
            onPush = false;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            horizontalForce += 1;
            onPush = false;
        }

        //Force Field
        //Right Field
        if( body.getPosition().x + size.x > constants.BOUNDARY - constants.FORCEFIELD){
            if(!Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                body.applyForceToCenter(-constants.FORCEPUSH * constants.JEDISAUR_VELOCITY_X, 0, false);

            if(body.getPosition().x + size.x > constants.BOUNDARY )
                body.setLinearVelocity(0, body.getLinearVelocity().y);

            onPush = true;

        }
        //Left Field
        if( body.getPosition().x - size.x < -constants.BOUNDARY + constants.FORCEFIELD) {
            if(!Gdx.input.isKeyPressed(Input.Keys.LEFT))
                body.applyForceToCenter(constants.FORCEPUSH * constants.JEDISAUR_VELOCITY_X, 0, false);

            if(body.getPosition().x - size.x < -constants.BOUNDARY )
                body.setLinearVelocity(0, body.getLinearVelocity().y);

            onPush = true;
        }

        // Jump and Double Jump
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && manager.getCl().getPlayerState() == constants.JEDISAUR_ON_GROUND){
            body.applyForceToCenter(0,constants.JEDISAUR_VELOCITY_Y,false);
            isDoubleJump = true;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && manager.getCl().getPlayerState() == constants.JEDISAUR_ON_AIR && isDoubleJump){
            body.applyForceToCenter(0,constants.JEDISAUR_VELOCITY_Y*0.4f,false);
            isDoubleJump = false;
        }

        if(!onPush)
            body.setLinearVelocity(horizontalForce * constants.JEDISAUR_VELOCITY_X,  body.getLinearVelocity().y);

    }
    private void springBoost(float delta){

        int  playerState = manager.getCl().getPlayerState();
        if(playerState == constants.JEDISAUR_SPRING_HIT){
            System.out.println(playerState);

            body.applyLinearImpulse(new Vector2(0,constants.JEDISAUR_JUMP_BOOST),body.getPosition(),false);
        }
    }

    @Override
    public void render(SpriteBatch sprite) {
        if(!isDestroyed) {
            sprite.setProjectionMatrix(manager.getCamera().combined);
            sprite.begin();
            sprite.draw(texture.getFrame(),
                    body.getPosition().x * constants.PPM - ((float)texture.getFrame().getRegionWidth()/2),
                    body.getPosition().y * constants.PPM - ((float)texture.getFrame().getRegionHeight()/2));
            sprite.end();
        }
    }

}