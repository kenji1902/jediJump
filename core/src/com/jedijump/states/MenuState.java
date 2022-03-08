package com.jedijump.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.jedijump.utility.*;

import java.awt.*;

public class MenuState extends State{
    Rectangle shape;
    Texture background, item;
    TextureRegion backgroundRegion, logo, mainMenu;
    OrthographicCamera camera;
    Box2DDebugRenderer b2dr;

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
    }

    @Override
    public void update(float delta) {
        Input(shape);
    }

    @Override
    public void render(SpriteBatch sprite) {
        OrthographicCamera camera = manager.getCamera();

        sprite.disableBlending();
        sprite.begin();

        sprite.draw(backgroundRegion,0, 0, 320, 480);
        sprite.end();

        sprite.enableBlending();
        sprite.begin();
        sprite.draw(logo, camera.position.x - (logo.getRegionWidth() / 2) , camera.position.y + 220 - 10 - (logo.getRegionWidth()/2));
        sprite.draw(mainMenu, 10, 200 - 110 / 2, 300, 110 );
        sprite.end();

    }
    private void Input(Rectangle rect){
        if(Gdx.input.isTouched()) {
            if (Gdx.input.getX() >= rect.x && Gdx.input.getY() >= rect.y && Gdx.input.getX() < (rect.x + rect.width)  && Gdx.input.getY() <= (rect.y + rect.height)) {
                System.out.println("you clicked here at: " + Gdx.input.getX() + ", " + Gdx.input.getY());
                manager.set(new PlayState(manager));
            }
        }
    }

    @Override
    public void dispose(){
        background.dispose();
        item.dispose();
    }


}
