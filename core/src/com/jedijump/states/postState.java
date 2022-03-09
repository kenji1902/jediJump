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
    static final int GAME_GAMEOVER = 0;
    static final int GAME_RETRY = 1;
    static final int GAME_QUIT = 2;
    private int state;
    Texture item, bg;
    TextureRegion retry, quit, bgRegion;
    Rectangle rect, retry_rect, quit_rect;
    ShapeRenderer sr, sr2;
    Vector3 coords, retry_coords, quit_coords;


    public postState(Manager manager) {
        super(manager);

        item = new Texture(Gdx.files.internal("items.png"));
        retry = new TextureRegion(item, 348, 353, 151, 30);
        quit = new TextureRegion(item, 363, 386, 128, 31);
        rect = new Rectangle(55, 150 , 64, 64);
        retry_rect = new Rectangle(65, 250, 192,96/2);
        quit_rect = new Rectangle(65, 200, 192,96/2);
        coords = new Vector3();
        retry_coords = new Vector3();
        quit_coords = new Vector3();

        bg = new Texture(Gdx.files.internal("postGameBG.png"));
        bgRegion = new TextureRegion(bg, 0, 0, constants.SCREENWIDTH, constants.SCREENHEIGHT);
    }

    @Override
    public void update(float delta) {
        switch (state) {
            case GAME_GAMEOVER:
                break;

            case GAME_RETRY:

                manager.set(new PlayState(manager));
                break;

            case GAME_QUIT:
                MenuState.menuMusic.stop();
                manager.set(new MenuState(manager));
                break;
        }
    }

    @Override
    public void render(SpriteBatch sprite) {
        manager.getCamera().update();
        sprite.setProjectionMatrix(manager.getCamera().combined);

        gameOver();
        //System.out.println(state);

        drawobject(sprite);
    }


    private void gameOver(){
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

    private void drawobject(SpriteBatch batch){

        OrthographicCamera camera = manager.getCamera();
        rect.y = 150 + camera.position.y;
        batch.begin();
        batch.draw(retry,  rect.x, rect.y, rect.width, rect.height);
        batch.draw(quit, rect.x, rect.y, rect.width, rect.height);
        batch.end();

        if(state == 0){
            camera.setToOrtho(false);

            batch.begin();
            batch.draw(bgRegion, 0,0,constants.SCREENWIDTH, constants.SCREENHEIGHT);
            batch.draw(retry, retry_rect.x + 25, retry_rect.y-10);
            batch.draw(quit, quit_rect.x + 35, quit_rect.y - 5);

            batch.end();
        }
    }

    @Override
    public void dispose() {

    }
}
