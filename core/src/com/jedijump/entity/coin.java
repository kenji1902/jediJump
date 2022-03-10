package com.jedijump.entity;

import com.badlogic.gdx.Gdx;
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

public class coin extends entity{
    private animation texture;
    private long score;
    public coin(Manager manager) {
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

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(this.size.x, this.size.y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        fixtureDef.friction = 0;

        body.createFixture(fixtureDef).setUserData("coin");
        shape.dispose();

        TextureRegion platformTexture = new TextureRegion(new Texture(Gdx.files.internal("items.png")));
        texture = new animation(platformTexture, 128, 32 ,96,32,3,0.5f,false);
        isGenerated = true;
    }

    @Override
    public void update(float delta) {
        if(!isDestroyed && isGenerated){
            texture.update(delta);
            coinAdd();
        }
    }

    private void coinAdd(){
        int playerState = manager.getCl().getPlayerState();
        if(playerState == constants.JEDISAUR_COIN_HIT){
            score += constants.COIN_SCORE;
            if(!isDestroyed)
                disposeBody();
        }
    }

    @Override
    public void render(SpriteBatch sprite) {
        if(!isDestroyed && isGenerated){
            sprite.begin();
            sprite.draw(texture.getFrame(),
                    body.getPosition().x * constants.PPM - ((float) texture.getFrame().getRegionWidth() / 2),
                    body.getPosition().y * constants.PPM - ((float) texture.getFrame().getRegionHeight() / 2));
            sprite.end();
        }
    }

    public long getScore() {
        return score;
    }
}
