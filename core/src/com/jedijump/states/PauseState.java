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
    static final int GAME_QUIT = 5;
    private int state;
    Texture item, bg;
    TextureRegion pause, bgRegion, pauseMenu;
    Rectangle rect, resume_rect, quit_rect;
    ShapeRenderer sr,sr2;
    Vector3 coords, resume_coords, quit_coords;


    public PauseState(Manager manager){
        super(manager);

        item = new Texture(Gdx.files.internal("items.png"));
        pause = new TextureRegion(item, 64, 64, 64, 64);
        rect = new Rectangle(55, 150 , 64, 64);
        resume_rect = new Rectangle(65, 250, 192,96/2);
        quit_rect = new Rectangle(65, 200, 192,96/2);
        sr = new ShapeRenderer();
        sr2 = new ShapeRenderer();
        coords = new Vector3();
        resume_coords = new Vector3();
        quit_coords = new Vector3();

        bg = new Texture(Gdx.files.internal("pauseGameBG.png"));
        bgRegion = new TextureRegion(bg, 0, 0, constants.SCREENWIDTH, constants.SCREENHEIGHT);
        pauseMenu = new TextureRegion(item, 224, 128, 192, 96);
    }

    @Override
    public void update(float delta) {

        switch (state){

            case GAME_PAUSED:

                //System.out.println("pushed");

                break;
            case GAME_RUNNING:
                manager.pop();
                break;
            case GAME_QUIT:
                manager.pop();
                MenuState.menuMusic.pause();
                manager.set(new MenuState(manager));
                break;
        }

    }

    @Override
    public void render(SpriteBatch sprite) {

        manager.getCamera().update();
        sprite.setProjectionMatrix(manager.getCamera().combined);

        bounds(rect);
        //System.out.println(state);

        drawobject(sprite);
    }
    private void bounds(Rectangle rect){
        if(Gdx.input.justTouched()){
            manager.getCamera().unproject(coords.set(Gdx.input.getX(), Gdx.input.getY(),0));
            if(rect.contains(coords.x, coords.y)){

                manager.push(this);
               // System.out.println("paused");
                state = 2;
                return;
            }
        }
        if(Gdx.input.justTouched()){
            manager.getCamera().unproject(resume_coords.set(Gdx.input.getX(), Gdx.input.getY(),0));
            if(resume_rect.contains(resume_coords.x, resume_coords.y)){
                state = GAME_RUNNING;
                return;
            }
        }
        if(Gdx.input.justTouched()){
            manager.getCamera().unproject(quit_coords.set(Gdx.input.getX(), Gdx.input.getY(),0));
            if(quit_rect.contains(quit_coords.x, quit_coords.y));
                state = GAME_QUIT;
                return;
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
            batch.draw(pauseMenu, resume_rect.x, resume_rect.y-50);

            batch.end();
//            sr.setProjectionMatrix(manager.getCamera().combined);
//            sr.begin(ShapeRenderer.ShapeType.Filled);
//            sr.rect(resume_rect.x, resume_rect.y, resume_rect.width, resume_rect.height);
//            sr.setColor(Color.GREEN);
//            sr.end();
//            sr2.setProjectionMatrix(manager.getCamera().combined);
//            sr2.begin(ShapeRenderer.ShapeType.Filled);
//            sr2.rect(quit_rect.x, quit_rect.y, quit_rect.width, quit_rect.height);
//            sr2.setColor(Color.RED);
//            sr2.end();
        }
    }

    @Override
    public void dispose() {

    }
}
