package com.jedijump;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.jedijump.states.Manager;
import com.jedijump.states.MenuState;
import com.jedijump.utility.constants;

import java.awt.*;

public class jediJump extends ApplicationAdapter {
	Texture img;
	Manager mng;
	MenuState menu;
	World world;
	OrthographicCamera camera;
	private SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		world = new World(new Vector2(0,-20), false);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, constants.SCREENWIDTH, constants.SCREENHEIGHT);
		mng = new Manager(world, camera);
		mng.push(new MenuState(mng));

		//img = new Texture("badlogic.jpg");

	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 0, 1);
		camera.combined.scl(constants.PPM);
		mng.update(Gdx.graphics.getDeltaTime());
		mng.render(batch);


	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		mng.disposeAll();
	}


}
