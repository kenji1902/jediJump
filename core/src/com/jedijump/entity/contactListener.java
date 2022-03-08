package com.jedijump.entity;

import com.badlogic.gdx.physics.box2d.*;
import com.jedijump.utility.constants;

public class contactListener implements ContactListener {
    int playerState = 0;
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
    }

    public int getPlayerState() {
        return playerState;
    }
}
