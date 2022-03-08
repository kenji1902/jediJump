package com.jedijump.entity;

import com.badlogic.gdx.physics.box2d.*;
import com.jedijump.utility.constants;

public class contactListener implements ContactListener {
    private int playerState = 0;
    private Body platform;
    @Override
    public void beginContact(Contact contact) {
        Fixture entityA = contact.getFixtureA();
        Fixture entityB = contact.getFixtureB();

        playerContact(entityA,entityB,constants.JEDISAUR_ON_GROUND);
    }


    @Override
    public void endContact(Contact contact) {
        Fixture entityA = contact.getFixtureA();
        Fixture entityB = contact.getFixtureB();

        playerContact(entityA,entityB,constants.JEDISAUR_ON_AIR);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private void playerContact(Fixture entityA, Fixture entityB, int state){
        if((entityA.getUserData() != null && entityA.getUserData().equals("foot")) ||
           (entityB.getUserData() != null && entityB.getUserData().equals("foot"))){
            playerState = state;
        }
        if((entityA.getUserData() != null && entityA.getUserData().equals("platform")))
            platform = entityA.getBody();
        else if(entityB.getUserData() != null && entityB.getUserData().equals("platform"))
            platform = entityB.getBody();

    }

    public int getPlayerState() {
        return playerState;
    }

    public Body getPlatform() {
        return platform;
    }
}
