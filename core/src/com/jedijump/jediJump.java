package com.jedijump;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.jedijump.entity.character;
import com.jedijump.entity.platform;
import com.jedijump.states.Manager;
import com.jedijump.utility.constants;

public class jediJump extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	character character;
	platform plt;
	Manager manage;


	private Box2DDebugRenderer b2dr;

	@Override
	public void create () {

		b2dr = new Box2DDebugRenderer();
		batch = new SpriteBatch();


		manage = new Manager();

		character = new character(manage);
		plt = new platform(manage);

		character.create(new Vector2(0,0),new Vector2(32,32),1);
		plt.create(new Vector2(0,-36),new Vector2(80,32),0);
	}

	@Override
	public void render () {

		ScreenUtils.clear(0, 0, 0, 1);
		b2dr.render(manage.getWorld(),manage.getCamera().combined.scl(constants.PPM));

		float delta = Gdx.graphics.getDeltaTime();
		manage.getWorld().step(1/60f,6,2);

		character.update(delta);
		character.render(batch);

		plt.update(delta);
		plt.render(batch);

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		b2dr.dispose();
	}
}
