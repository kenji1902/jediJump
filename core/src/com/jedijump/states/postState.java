package com.jedijump.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.jedijump.utility.constants;

public class postState extends State {
    static final int GAME_RETRY = 0;
    static final int GAME_QUIT = 1;
    private int state;
    Texture item, bg;
    TextureRegion retry, quit, bgRegion;
    Rectangle rect, retry_rect, quit_rect;
    ShapeRenderer sr, sr2;
    Vector3 coords, retry_coords, quit_coords;


    public postState(Manager manager) {
        super(manager);

        item = new Texture(Gdx.files.internal("items.png"));
        retry = new TextureRegion(item, 151, 30, 348, 353);
        quit = new TextureRegion(item, 128, 31, 363, 386);
        rect = new Rectangle(55, 150 , 64, 64);
        retry_rect = new Rectangle(65, 250, 192,96/2);
        quit_rect = new Rectangle(65, 200, 192,96/2);
        sr = new ShapeRenderer();
        sr2 = new ShapeRenderer();
        coords = new Vector3();
        retry_coords = new Vector3();
        quit_coords = new Vector3();

        bg = new Texture(Gdx.files.internal("postGameBG.png"));
        bgRegion = new TextureRegion(bg, 0, 0, constants.SCREENWIDTH, constants.SCREENHEIGHT);
    }

    @Override
    public void update(float delta) {
        switch (state) {
            case GAME_RETRY:
                manager.set(new PlayState(manager));
                break;

            case GAME_QUIT:
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

    private void bounds(Rectangle rect) {
        if (Gdx.input.justTouched()) {
            manager.getCamera().unproject(coords.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (rect.contains(coords.x, coords.y)) {

                manager.push(this);
                // System.out.println("paused");
                state = 0;
                return;
            }
        }
        if (Gdx.input.justTouched()) {
            manager.getCamera().unproject(retry_coords.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (retry_rect.contains(retry_coords.x, retry_coords.y)) {
                state = GAME_RETRY;
                return;
            }
        }
        if (Gdx.input.justTouched()) {
            manager.getCamera().unproject(quit_coords.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (quit_rect.contains(quit_coords.x, quit_coords.y)) {
                state = GAME_QUIT;
                return;

            }
        }
    }

    private void gameOver(){

    }

    private void drawobject(SpriteBatch batch){

        OrthographicCamera camera = manager.getCamera();
        rect.y = 150 + camera.position.y;
        batch.begin();
        batch.draw(retry,  rect.x, rect.y, rect.width, rect.height);
        batch.draw(quit, rect.y, rect.y, rect.width, rect.height);
        batch.end();

        if(state == 0){
            camera.setToOrtho(false);

            batch.begin();
            batch.draw(bgRegion, 0,0,constants.SCREENWIDTH, constants.SCREENHEIGHT);
            batch.draw(retry, retry_rect.x, retry_rect.y-50);

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
        if(state == 1){
            camera.setToOrtho(false);

            batch.begin();
            batch.draw(bgRegion, 0,0, constants.SCREENWIDTH, constants.SCREENHEIGHT);
            batch.draw(quit, quit_rect.x, quit_rect.y-50);
            batch.end();
        }
    }

    @Override
    public void dispose() {
    }
}
