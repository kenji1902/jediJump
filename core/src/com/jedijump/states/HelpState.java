package com.jedijump.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import java.awt.*;

public class HelpState extends State {

    Rectangle nextBounds;
    OrthographicCamera camera;
    Texture item, help;
    TextureRegion helpRegion, arrow;
    Vector3 touchPoint;

    int count = 0;

    public HelpState(Manager manager){
        super(manager);

        camera = new OrthographicCamera();
        camera.setToOrtho(false,320,480);

        nextBounds = new Rectangle(320 - 64, 0, 64, 64);
        touchPoint = new Vector3();

        help = new Texture(Gdx.files.internal("help1.png"));
        helpRegion = new TextureRegion(help, 0, 0, 320, 480);

        item = new Texture(Gdx.files.internal("items.png"));
        arrow = new TextureRegion(item, 0, 64, 64, 64);
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.justTouched()) {
            camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (nextBounds.contains(touchPoint.x, touchPoint.y)) {
                if(count==0){
                    help.dispose();
                    help = new Texture(Gdx.files.internal("help2.png"));
                    helpRegion = new TextureRegion(help, 0, 0, 320, 480);
                    count++;
                }
                else if (count == 1){
                    help.dispose();
                    help = new Texture(Gdx.files.internal("help1.png"));
                    helpRegion = new TextureRegion(help, 0, 0, 320, 480);
                    count++;
                }
                else if(count==2){
                    help.dispose();
                    help = new Texture(Gdx.files.internal("help2.png"));
                    helpRegion = new TextureRegion(help, 0, 0, 320, 480);
                    count++;
                }
                else if(count==3){
                    help.dispose();
                    help = new Texture(Gdx.files.internal("help1.png"));
                    helpRegion = new TextureRegion(help, 0, 0, 320, 480);
                    count++;
                }
                else if(count==4){
                    help.dispose();
                    manager.set(new MenuState(manager));
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch sprite) {
        sprite.disableBlending();
        sprite.begin();
        sprite.draw(helpRegion,0, 0, 320, 480);
        sprite.end();

        sprite.enableBlending();
        sprite.begin();
        sprite.draw(arrow, 320 , 0,-64,64);
        sprite.end();
    }

    @Override
    public void dispose() {
        help.dispose();
    }

}
