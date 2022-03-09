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
import com.jedijump.states.MenuState;
import com.jedijump.states.PlayState;
import com.jedijump.utility.constants;

public class jediJump extends ApplicationAdapter {
	SpriteBatch batch;
	Manager manage;

	@Override
	public void create () {
		batch = new SpriteBatch();
		manage = new Manager();
		manage.push(new MenuState(manage));
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 0);
		float delta = Gdx.graphics.getDeltaTime();
		manage.update(delta);
		manage.render(batch);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		manage.disposeAll();
	}
}
