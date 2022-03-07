package com.jedijump.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.jedijump.utility.*;

import java.awt.*;

public class MenuState extends State{
    Rectangle shape;
    Texture background, item;
    TextureRegion backgroundRegion, logo, mainMenu;
    ShapeRenderer sr;

    public MenuState(Manager manager) {
        super(manager);
        background = new Texture(Gdx.files.internal("background.png"));
        backgroundRegion = new TextureRegion(background, 0, 0, 280, 450);

        item = new Texture(Gdx.files.internal("items.png"));
        mainMenu = new TextureRegion(item, 0, 224, 300, 110);
        logo = new TextureRegion(item, 0, 352, 274, 142);

        shape = new Rectangle(102 / constants.PPM, 222 / constants.PPM, (int)(117 / constants.SCALE/ constants.PPM),(int)(33/constants.SCALE/ constants.PPM));
        sr = new ShapeRenderer();
    }

    @Override
    public void update(float delta) {
        Input(shape);
    }

    @Override
    public void render(SpriteBatch sprite) {

        sprite.disableBlending();
        sprite.begin();

        sprite.draw(backgroundRegion,0, 0, 320, 480);
        sprite.end();

        sprite.enableBlending();
        sprite.begin();
        sprite.draw(logo, 160 - 274 / 2, 480 - 10 - 142, 274, 142);
        sprite.draw(mainMenu, 10, 200 - 110 / 2, 300, 110 );
        sprite.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.GREEN);
        sr.rect(shape.x, shape.y, shape.width, shape.height);
        sr.end();


    }

    private void Input(Rectangle rect){

        if(Gdx.input.isTouched()) {
            if (Gdx.input.getX() >= rect.x && Gdx.input.getY() >= rect.y && Gdx.input.getX() < (rect.x + rect.width)  && Gdx.input.getY() <= (rect.y + rect.height)) {
                System.out.println("you clicked here at: " + Gdx.input.getX() + ", " + Gdx.input.getY());
                //manager.pop();
            }
        }
    }

    @Override
    public void dispose(){
        background.dispose();
        item.dispose();
    }


}
