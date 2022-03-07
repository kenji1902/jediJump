package com.jedijump;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.jedijump.states.Manager;
import com.jedijump.states.MenuState;

import java.awt.*;

public class jediJump extends ApplicationAdapter {
	Texture img;
	Manager mng;
	MenuState menu;
	private SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		mng = new Manager();
		mng.push(new MenuState(mng));

		//img = new Texture("badlogic.jpg");

	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 0, 1);
		mng.render(batch);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
