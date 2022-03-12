package com.jedijump;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.jedijump.states.Manager;
import com.jedijump.states.MenuState;
import com.jedijump.states.PlayState;
import com.jedijump.utility.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class jediJump extends ApplicationAdapter {
	SpriteBatch batch;
	Manager manage;
	database db;
	@Override
	public void create () {
		batch = new SpriteBatch();
		manage = new Manager();
		manage.push(new PlayState(manage));
		manage.push(new MenuState(manage));

		db = new database("Highscore.db");
		db.query("CREATE TABLE HIGHSCORE" +
				"(ID INT PRIMARY KEY NOT NULL," +
				"COOKIE INT NOT NULL," +
				"DISTANCE INT NOT NULL);");
		db.queryUpdate("INSERT INTO HIGHSCORE(ID,COOKIE,DISTANCE) VALUES(1,'2','89');");
		ResultSet result =  db.queryResult(" SELECT * FROM HIGHSCORE;");

		try {
			System.out.println(result.getInt("DISTANCE"));
		} catch (SQLException e) {
			e.printStackTrace();
		}

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
