package com.jedijump.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.jedijump.utility.*;
import com.badlogic.gdx.audio.Sound;

import java.awt.*;

public class MenuState extends State{
    Rectangle shape, soundBounds;
    Texture background, item;
    TextureRegion backgroundRegion, logo, mainMenu, soundOn,soundOff;
    OrthographicCamera camera;
    Box2DDebugRenderer b2dr;
    static Sound menuMusic;
    Sound clickSound;
    Vector3 touchPoint;


    public MenuState(Manager manager) {
        super(manager);

        camera = new OrthographicCamera();
        b2dr = new Box2DDebugRenderer();

        background = new Texture(Gdx.files.internal("background.png"));
        backgroundRegion = new TextureRegion(background, 0, 0, 280, 450);

        item = new Texture(Gdx.files.internal("items.png"));
        mainMenu = new TextureRegion(item, 0, 224, 300, 110);
        logo = new TextureRegion(item, 0, 352, 274, 142);

        shape = new Rectangle(102,222,117,33);

        menuMusic = Gdx.audio.newSound(Gdx.files.internal("music.mp3"));

        clickSound = Gdx.audio.newSound(Gdx.files.internal("click.wav"));

        soundOn = new TextureRegion(item, 64, 0, 64, 64);
        soundOff = new TextureRegion(item, 0, 0, 64, 64);
        soundBounds = new Rectangle(0, 64, 64, 64);
        touchPoint = new Vector3();

        menuMusic.loop(0.2f);
    }

    @Override
    public void update(float delta) {
        Input(shape);
    }

    public void Input(Rectangle rect){
        if(Gdx.input.justTouched()) {
            manager.getCamera().unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            System.out.println("you clicked here at: " + Gdx.input.getX() + ", " + Gdx.input.getY());

            //Play Menu
            if (rect.contains(touchPoint.x, touchPoint.y)) {
                clickSound.play();
                manager.set(new PlayState(manager));
            }

            //Help menu
            if (rect.contains(touchPoint.x, touchPoint.y+76)) {
                clickSound.play();
                manager.set(new HelpState(manager));
            }

            //sound menu
            if (soundBounds.contains(touchPoint.x, touchPoint.y+75)) {
                clickSound.play();
                Settings.soundEnabled = !Settings.soundEnabled;
                if(Settings.soundEnabled) {
                    menuMusic.loop(0.2f);
                }
                else {
                    menuMusic.pause();
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch sprite) {

        sprite.disableBlending();
        sprite.begin();

        sprite.draw(backgroundRegion,0, 0, 320, 480);
        sprite.end();

        sprite.enableBlending();
        sprite.begin();
        sprite.draw(logo, 160 - 274 / 2 , 480 - 10 - 142);
        sprite.draw(mainMenu, 10, 200 - 110 / 2, 300, 110 );
        sprite.draw(Settings.soundEnabled ? soundOn :  soundOff,0,0,64,64);
        sprite.end();
    }

    @Override
    public void dispose(){
//        background.dispose();
//        item.dispose();
    }



}
