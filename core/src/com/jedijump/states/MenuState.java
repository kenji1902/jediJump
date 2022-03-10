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
    TextureRegion background, item;
    TextureRegion backgroundRegion, logo, mainMenu, soundOn,soundOff;
    OrthographicCamera camera;
    Box2DDebugRenderer b2dr;
    static Sound menuMusic;
    Sound clickSound;
    Vector3 touchPoint;
    ShapeRenderer sr;


    public MenuState(Manager manager) {
        super(manager);

        camera = new OrthographicCamera();
        b2dr = new Box2DDebugRenderer();

        background = new TextureRegion(new Texture(Gdx.files.internal("background.png")));
        backgroundRegion = new TextureRegion(background, 0, 0, 280, 450);

        item = manager.getItems();

        mainMenu = new TextureRegion(item, 0, 224, 300, 110);
        logo = new TextureRegion(item, 0, 352, 274, 142);

        shape = new Rectangle(0 - 117/2,-10,117,33);

        menuMusic = Gdx.audio.newSound(Gdx.files.internal("music.mp3"));

        clickSound = Gdx.audio.newSound(Gdx.files.internal("click.wav"));

        soundOn = new TextureRegion(item, 64, 0, 64, 64);
        soundOff = new TextureRegion(item, 0, 0, 64, 64);
        soundBounds = new Rectangle(-155, -160, 64, 64);
        touchPoint = new Vector3();
        sr = new ShapeRenderer();

        menuMusic.loop(0.2f);
    }

    @Override
    public void update(float delta) {
        cameraUpdate();
        Input(shape);
    }

    private void cameraUpdate(){
        Vector3 position = manager.getCamera().position;
        position.x = this.backgroundRegion.getRegionX() ;
        position.y = backgroundRegion.getRegionY();
        manager.getCamera().position.set(position);
        manager.getCamera().update();
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
        sprite.setProjectionMatrix(manager.getCamera().combined);
        sprite.disableBlending();
        sprite.begin();

        sprite.draw(backgroundRegion,manager.getCamera().position.x - 160, manager.getCamera().position.y - 240, 320, 480);
        sprite.end();

        sprite.enableBlending();
        sprite.begin();

        sprite.draw(logo, -135 , 80);
        sprite.draw(mainMenu, shape.x - 90, shape.y - 74);
        sprite.draw(Settings.soundEnabled ? soundOn :  soundOff,soundBounds.x,soundBounds.y-64,64,64);
        sprite.end();

    }

    @Override
    public void dispose(){
//        background.dispose();
//        item.dispose();
    }



}
