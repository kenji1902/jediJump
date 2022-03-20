package com.jedijump.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.jedijump.utility.constants;
import com.jedijump.utility.database;

import java.text.DecimalFormat;

public class postState extends State {
    static final int GAME_GAMEOVER = 0;
    static final int GAME_RETRY = 1;
    static final int GAME_QUIT = 2;
    private int state;
    private database db;
    private BitmapFont font;
    Texture item, bg;
    TextureRegion retry, quit, bgRegion;
    Rectangle rect, retry_rect, quit_rect;
    ShapeRenderer sr, sr2;
    Vector3 coords, retry_coords, quit_coords;
    DecimalFormat df = new DecimalFormat();

    Sound deathSound;
    public postState(Manager manager) {
        super(manager);

        item = new Texture(Gdx.files.internal("items.png"));
        retry = new TextureRegion(item, 348, 353, 151, 30);
        quit = new TextureRegion(item, 363, 386, 128, 31);
        rect = new Rectangle(55, 150 , 64, 64);
        retry_rect = new Rectangle(65 - 160, 0, 192,96/2);
        quit_rect = new Rectangle(65 - 160, -50, 192,96/2);
        coords = new Vector3();
        retry_coords = new Vector3();
        quit_coords = new Vector3();
        sr = new ShapeRenderer();
        sr2 = new ShapeRenderer();
        bg = new Texture(Gdx.files.internal("postGameBG.png"));
        bgRegion = new TextureRegion(bg, 0, 0, constants.SCREENWIDTH, constants.SCREENHEIGHT);
        deathSound = Gdx.audio.newSound(Gdx.files.internal("deathSound.wav"));
        deathSound.play(0.2f);
        db = manager.getDatabase();
        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        font.getData().scale(0.01f);

        df.setMaximumFractionDigits(2);
        float distance = manager.getDistance();
        int score = manager.getScore();
        insert_query(score, distance);
    }

    @Override
    public void update(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
            state = GAME_RETRY;

        switch (state) {
            case GAME_GAMEOVER:
                break;

            case GAME_RETRY:

                manager.set(new PlayState(manager));
                break;

            case GAME_QUIT:
                MenuState.menuMusic.stop();
                manager.pop();
                //manager.push(new PlayState(manager));
                manager.push(new MenuState(manager));
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
//        sr.begin(ShapeRenderer.ShapeType.Filled);
//        sr.rect(retry_rect.x, retry_rect.y, retry_rect.width, retry_rect.height);
//        sr.setColor(Color.GREEN);
//        sr.end();
//        sr2.begin(ShapeRenderer.ShapeType.Filled);
//        sr2.rect(quit_rect.x, quit_rect.y, quit_rect.width, quit_rect.height);
//        sr2.setColor(Color.RED);
//        sr2.end();
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


        if(state == 0){
            //camera.setToOrtho(false);

            batch.begin();
            batch.draw(bgRegion, manager.getCamera().position.x - 160,manager.getCamera().position.y -240,constants.SCREENWIDTH, constants.SCREENHEIGHT);
            batch.end();

            //batch.enableBlending();
            batch.begin();

            font.draw(batch, "Coins ", 40, manager.getCamera().position.y + 190);
            font.draw(batch, ""+manager.getScore(), 40, manager.getCamera().position.y + 150);
            font.draw(batch, "Distance ", -130, manager.getCamera().position.y + 190);
            font.draw(batch, ""+df.format(manager.getDistance()), -130, manager.getCamera().position.y + 150);
            retry_rect.y = 10 + manager.getCamera().position.y;
            quit_rect.y = -40 + manager.getCamera().position.y;
            batch.draw(retry,  retry_rect.x, retry_rect.y, retry_rect.width, retry_rect.height);
            batch.draw(quit, quit_rect.x, quit_rect.y, quit_rect.width, quit_rect.height);
            batch.end();



        }
    }

    public void insert_query(int score, float distance){
        db.queryUpdate(String.format("INSERT INTO HIGHSCORE(COOKIE, DISTANCE) VALUES(%d, %f)", score, distance));
    }

    @Override
    public void dispose() {


    }
}
