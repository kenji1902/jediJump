package com.jedijump.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.jedijump.states.postState;
import com.jedijump.utility.animation;
import com.jedijump.utility.constants;

public class character extends entity{
    animation side;
    animation jump;
    animation stand;
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
        if(constants.DEBUG_MODE)
            fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("body");

        // Foot of the Character
        shape.setAsBox(this.size.x/1.2f,this.size.y / 4,new Vector2( 0,-this.size.y),0);
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("foot");



        shape.dispose();
        TextureRegion platformTexture = manager.getItems();
                //new TextureRegion(new Texture(Gdx.files.internal("items.png")));

        side = new animation(platformTexture,32,128,64,32,2,0.5f,false);
        jump = new animation(platformTexture,96,128,64,32,2,0.5f,false);
        stand = new animation(platformTexture,0,128,32,32,1,0.5f,false);

        maxPosY = body.getPosition().y;
        isGenerated = true;
    }
    @Override
    public void update(float delta) {
        if(!isDestroyed && isGenerated) {
            if(manager.getCl().getPlayerState() != constants.JEDISAUR_ON_AIR && ((Gdx.input.isKeyPressed(Input.Keys.LEFT)) || (Gdx.input.isKeyPressed(Input.Keys.RIGHT))))
                side.update(delta);
            else if(manager.getCl().getPlayerState() != constants.JEDISAUR_ON_AIR)
                stand.update(delta);
            else
                jump.update(delta);


            cameraUpdate();
            Input(delta);
            characterPlatform();
            springBoost(delta);
            deadZone();

        }
    }
    private void characterPlatform(){
        int playerState = manager.getCl().getPlayerState();
        Body platform = manager.getCl().getPlatform();
        if(playerState == constants.JEDISAUR_ON_GROUND && platform != null ){
            body.setLinearVelocity(body.getLinearVelocity().x + platform.getLinearVelocity().x,body.getLinearVelocity().y);
        }

    }
    private void deadZone(){
        if(!isDestroyed) {
            OrthographicCamera camera = manager.getCamera();
            float deadZone = camera.position.y - (camera.viewportHeight / 2);
            float charPos = body.getPosition().y * constants.PPM - (this.size.y * constants.PPM );
            if (charPos < deadZone && !isDestroyed && !constants.DEBUG_MODE){
                System.out.println("DEAD FROM FALLING");
                disposeBody();
            }
            if (manager.getCl().getPlayerState() == constants.JEDISAUR_BIRD_HIT && !isDestroyed && !constants.DEBUG_MODE) {
                disposeBody();
                System.out.println("DEAD FROM BIRD");
            }
            if(isDestroyed) {
                manager.setDistance(manager.getCamera().position.y);
                manager.set(new postState(manager));
            }


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
    private boolean isLeft = false;
    private void Input(float delta){

        //Control
        int horizontalForce = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            horizontalForce -= 1;
            onPush = false;
            if(!isLeft) {
                side.flip();
                stand.flip();
                isLeft = true;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            horizontalForce += 1;
            onPush = false;
            if(isLeft) {
                side.flip();
                stand.flip();
                isLeft = false;
            }
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
        if(constants.DEBUG_MODE && Gdx.input.isKeyJustPressed(Input.Keys.UP)/* && manager.getCl().getPlayerState() == constants.JEDISAUR_ON_GROUND*/){
            body.applyForceToCenter(0,constants.JEDISAUR_VELOCITY_Y,false);
            isDoubleJump = true;
        }
        if(!constants.DEBUG_MODE && Gdx.input.isKeyJustPressed(Input.Keys.UP) && manager.getCl().getPlayerState() == constants.JEDISAUR_ON_GROUND){
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
            body.applyLinearImpulse(new Vector2(0,constants.JEDISAUR_JUMP_BOOST),body.getPosition(),false);
        }
        if(playerState == constants.JEDISAUR_BIRD_HEAD_HIT){
            body.applyLinearImpulse(new Vector2(0,constants.JEDISAUR_JUMP_BOOST*constants.JEDISAUR_ENTITY_JUMP_BOOST),body.getPosition(),false);
        }
    }

    @Override
    public void render(SpriteBatch sprite) {
        if(!isDestroyed && isGenerated) {
            sprite.setProjectionMatrix(manager.getCamera().combined);
            sprite.enableBlending();
            sprite.begin();
;
            if(manager.getCl().getPlayerState() != constants.JEDISAUR_ON_AIR && ((Gdx.input.isKeyPressed(Input.Keys.LEFT)) || (Gdx.input.isKeyPressed(Input.Keys.RIGHT))))
                sprite.draw(side.getFrame(),
                        body.getPosition().x * constants.PPM - ((float)side.getFrame().getRegionWidth()/2),
                        body.getPosition().y * constants.PPM - ((float)side.getFrame().getRegionHeight()/2));
            else if(manager.getCl().getPlayerState() != constants.JEDISAUR_ON_AIR)
                sprite.draw(stand.getFrame(),
                        body.getPosition().x * constants.PPM - ((float)stand.getFrame().getRegionWidth()/2),
                        body.getPosition().y * constants.PPM - ((float)stand.getFrame().getRegionHeight()/2));
            else
                sprite.draw(jump.getFrame(),
                        body.getPosition().x * constants.PPM - ((float)jump.getFrame().getRegionWidth()/2),
                        body.getPosition().y * constants.PPM - ((float)jump.getFrame().getRegionHeight()/2));

            sprite.end();
        }
    }

    public float getWorldHeight() {
        return maxPosY + (constants.SCREENHEIGHT/32f);
    }
}