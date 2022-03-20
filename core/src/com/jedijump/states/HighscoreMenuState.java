package com.jedijump.states;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.jedijump.states.Manager;
import com.jedijump.states.MenuState;
import com.jedijump.states.Settings;
import com.jedijump.states.State;
import com.jedijump.utility.constants;
import com.jedijump.utility.database;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HighscoreMenuState extends State{

    Rectangle shape, nextBounds;
    TextureRegion background, item, arrow;
    Texture item_2;
    TextureRegion backgroundRegion, highscore_text;
    OrthographicCamera camera;
    Vector3 touchPoint;
    Box2DDebugRenderer b2dr;
    private BitmapFont font;
    private database db;
    static Sound menuMusic;

    public HighscoreMenuState(Manager manager) {
        super(manager);

        camera = new OrthographicCamera();
        b2dr = new Box2DDebugRenderer();
        db = manager.getDatabase();
        item_2 = new Texture(Gdx.files.internal("items.png"));
        arrow = new TextureRegion(item_2, 0, 64, 64, 64);
        arrow.flip(true,false);
        touchPoint = new Vector3();
        nextBounds = new Rectangle(320 - 64, 0, 64, 64);

        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        font.getData().scale(0.01f);

        background = new TextureRegion(new Texture(Gdx.files.internal("highscorebg.png")));
        backgroundRegion = new TextureRegion(background, 0, 0, constants.SCREENWIDTH, constants.SCREENHEIGHT);

        item = manager.getItems();

        highscore_text = new TextureRegion(item, 1, 267, 265, 32);
        shape = new Rectangle(0 - 117/2,-10,117,33);
    }

    @Override
    public void update(float delta) {
        cameraUpdate();
        if (Gdx.input.justTouched() || Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            MenuState.menuMusic.stop();
            manager.set(new MenuState(manager));
        }

    }

    private void cameraUpdate(){
        Vector3 position = manager.getCamera().position;
        position.x = this.backgroundRegion.getRegionX() ;
        position.y = backgroundRegion.getRegionY();
        manager.getCamera().position.set(position);
        manager.getCamera().update();
    }

    @Override
    public void render(SpriteBatch sprite) {
        sprite.setProjectionMatrix(manager.getCamera().combined);
        sprite.disableBlending();
        sprite.begin();

        sprite.draw(backgroundRegion, manager.getCamera().position.x - 160, manager.getCamera().position.y - 240, 320, 480);
        sprite.end();

        sprite.enableBlending();
        sprite.begin();
        sprite.draw(highscore_text, shape.x - 68, shape.y + 170, 250, 25);
        sprite.end();

        sprite.begin();
        font.draw(sprite, "ID", -144, camera.position.y + 140);
        sprite.end();

        sprite.begin();
        font.draw(sprite, "Coin", -80, camera.position.y + 140);
        sprite.end();

        sprite.begin();
        font.draw(sprite, "Distance", 5, camera.position.y + 140);
        sprite.end();

        sprite.begin();
        sprite.draw(arrow, nextBounds.x - 350, nextBounds.y + 180, -64, 64);
        sprite.end();

        try {
            display_highscore(sprite);
        } catch (SQLException ignored) {

        }

    }

    private void display_highscore(SpriteBatch sprite) throws SQLException {
        String sql = "SELECT * FROM HIGHSCORE ORDER BY DISTANCE DESC LIMIT 5;";
        ResultSet result = db.queryResult(sql);
        int y_offset = 0;
        while(result.next()){
            String id = String.valueOf(result.getInt("ID"));
            String cookie = String.valueOf(result.getInt("COOKIE"));
            String distance = String.format("%.2f",result.getFloat("DISTANCE"));
            System.out.println(distance);
            sprite.begin();
            font.draw(sprite, id + "   " + cookie,-144,(camera.position.y+90)-y_offset);
            font.draw(sprite, distance, 7, (camera.position.y+90)-y_offset);
            sprite.end();
            y_offset += 60;
        }
    }

    @Override
    public void dispose() {

    }
}
