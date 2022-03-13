package com.jedijump.states.HighscoreMenuState;
import com.badlogic.gdx.Gdx;
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
import com.jedijump.states.State;
import com.jedijump.utility.constants;
import com.jedijump.utility.database;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HighscoreMenuState extends State{

    Rectangle shape;
    TextureRegion background, item;
    TextureRegion backgroundRegion, highscore_text;
    OrthographicCamera camera;
    Box2DDebugRenderer b2dr;
    private BitmapFont font;
    private database db;
    static Sound menuMusic;

    public HighscoreMenuState(Manager manager) {
        super(manager);

        camera = new OrthographicCamera();
        b2dr = new Box2DDebugRenderer();
        db = manager.getDatabase();

        font = new BitmapFont();
        background = new TextureRegion(new Texture(Gdx.files.internal("plainBG.png")));
        backgroundRegion = new TextureRegion(background, 0, 0, constants.SCREENWIDTH, constants.SCREENHEIGHT);

        item = manager.getItems();

        highscore_text = new TextureRegion(item, 1, 267, 265, 32);
        shape = new Rectangle(0 - 117/2,-10,117,33);
    }

    @Override
    public void update(float delta) {
        cameraUpdate();
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

        sprite.draw(backgroundRegion,manager.getCamera().position.x - 160, manager.getCamera().position.y - 240, 320, 480);
        sprite.end();

        sprite.enableBlending();
        sprite.begin();
        sprite.draw(highscore_text, shape.x - 68, shape.y+130,250,25);
        try{
            display_highscore();
        } catch(SQLException ignored){

        }
        //font.draw(sprite,"Happy Coding!",Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        sprite.end();
    }

    private void display_highscore() throws SQLException {
        String sql = "SELECT * FROM HIGHSCORE ORDER BY DISTANCE DESC LIMIT 5;";
        ResultSet result = db.queryResult(sql);
        while(result.next()){
            System.out.println(result.getInt("ID") + "|" + result.getInt("COOKIE") + "|" +
                    result.getInt("DISTANCE"));
        }
    }

    @Override
    public void dispose() {

    }
}
