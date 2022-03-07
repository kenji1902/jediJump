package com.jedijump.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.jedijump.utility.assets;

import java.awt.*;

public class MenuState extends State{
    Rectangle shape;
    Texture background, item;
    TextureRegion backgroundRegion, logo, mainMenu;

    public MenuState(Manager manager) {
        super(manager);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(SpriteBatch sprite) {
        shape = new Rectangle();
        shape.setRect();
        if(Gdx.input.isTouched()) {
            if (Gdx.input.getX() > 106 && Gdx.input.getX() < 213 && Gdx.input.getY() > 231 && Gdx.input.getY() < 257) {
                System.out.println("you clicked here at: " + Gdx.input.getX() + ", " + Gdx.input.getY());
                //manager.pop();
            }
        }

        background = new Texture(Gdx.files.internal("background.png"));
        backgroundRegion = new TextureRegion(background, 0, 0, 280, 450);

        item = new Texture(Gdx.files.internal("items.png"));
        mainMenu = new TextureRegion(item, 0, 224, 300, 110);
        logo = new TextureRegion(item, 0, 352, 274, 142);

        sprite.disableBlending();
        sprite.begin();
        sprite.draw(backgroundRegion,0, 0, 320, 480);
        sprite.end();

        sprite.enableBlending();
        sprite.begin();
        sprite.draw(logo, 160 - 274 / 2, 480 - 10 - 142, 274, 142);
        sprite.draw(mainMenu, 10, 200 - 110 / 2, 300, 110 );
        sprite.end();
    }

}
