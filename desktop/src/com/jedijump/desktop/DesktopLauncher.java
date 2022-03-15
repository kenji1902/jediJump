package com.jedijump.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jedijump.jediJump;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 320;
		config.height = 480;
		config.foregroundFPS = 60;
		config.backgroundFPS = 60;
		config.addIcon("jedisaurlogo_128.png", Files.FileType.Internal);
		config.addIcon("jedisaurlogo_64.png", Files.FileType.Internal);
		config.addIcon("jedisaurlogo_32.png", Files.FileType.Internal);

		new LwjglApplication(new jediJump(), config);


	}
}
