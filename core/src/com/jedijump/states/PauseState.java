package com.jedijump.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.jedijump.entity.character;
import com.jedijump.entity.platform;
import com.jedijump.utility.constants;

public class PauseState extends State{
    static final int GAME_READY = 0;
    static final int GAME_RUNNING = 1;
    static final int GAME_PAUSED = 2;
    static final int GAME_LEVEL_END = 3;
    static final int GAME_OVER = 4;
    private int state;
    Texture item, bg;
    TextureRegion pause, bgRegion;
    Rectangle rect;
    ShapeRenderer sr;
    Vector3 coords;

    public PauseState(Manager manager){
        super(manager);
        item = new Texture(Gdx.files.internal("items.png"));
        pause = new TextureRegion(item, 64, 64, 64, 64);
        rect = new Rectangle(55,
                150 ,
                64, 64);
        sr = new ShapeRenderer();
        coords = new Vector3();
        bg = new Texture(Gdx.files.internal("background.png"));
        bgRegion = new TextureRegion(bg, 0, 0, constants.SCREENWIDTH, constants.SCREENHEIGHT);
    }

    @Override
    public void update(float delta) {

        switch (state){
            case GAME_PAUSED:

                System.out.println("pushed");
                break;
            case GAME_RUNNING:
                manager.pop();
                break;
        }

    }

    @Override
    public void render(SpriteBatch sprite) {

        manager.getCamera().update();
        sprite.setProjectionMatrix(manager.getCamera().combined);

//        sr.setProjectionMatrix(manager.getCamera().combined);
//        sr.begin(ShapeRenderer.ShapeType.Filled);
//        sr.rect(rect.x, rect.y, rect.width, rect.height);
//        sr.setColor(Color.GREEN);
//        sr.end();
        bounds(rect);
        System.out.println(state);

        drawobject(sprite);
    }
    private void bounds(Rectangle rect){
        if(Gdx.input.justTouched()){
            manager.getCamera().unproject(coords.set(Gdx.input.getX(), Gdx.input.getY(),0));
            if(rect.contains(coords.x, coords.y)){
                manager.push(this);
                System.out.println("paused");
                state = 2;
                return;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            state = GAME_RUNNING;
            System.out.println("running");
        }

    }

    private void pause(){


    }

    private void drawobject(SpriteBatch batch){

        OrthographicCamera camera = manager.getCamera();
        rect.y = 150 + camera.position.y;
        batch.begin();
        batch.draw(pause,  rect.x, rect.y, rect.width, rect.height);
        batch.end();

        if(state == 2){
            camera.setToOrtho(false);

            batch.begin();
            batch.draw(bgRegion, 0,0,constants.SCREENWIDTH, constants.SCREENHEIGHT);
            batch.end();
        }
    }

    @Override
    public void dispose() {

    }
}
