package com.jedijump;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.jedijump.entity.character;
import com.jedijump.states.Manager;
import com.jedijump.utility.constants;

public class jediJump extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	character character;
	Manager manage;

	OrthographicCamera camera;
	World world;
	private Box2DDebugRenderer b2dr;

	@Override
	public void create () {
		world = new World(new Vector2(0,-20),false);
		camera = new OrthographicCamera();
		batch = new SpriteBatch();
		camera.setToOrtho(false,constants.SCREENWIDTH,constants.SCREENHEIGHT);
		b2dr = new Box2DDebugRenderer();


		manage = new Manager(world,camera);

		character = new character(manage);
		character.create(new Vector2(0,0),new Vector2(32,32),0);
	}

	@Override
	public void render () {

		ScreenUtils.clear(0, 0, 0, 1);
		b2dr.render(world,camera.combined.scl(constants.PPM));



		character.update(Gdx.graphics.getDeltaTime());
		character.render(batch);


	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		b2dr.dispose();
	}
}
