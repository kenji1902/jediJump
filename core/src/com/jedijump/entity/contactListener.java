package com.jedijump.entity;

import com.badlogic.gdx.physics.box2d.*;
import com.jedijump.utility.constants;

import javax.swing.text.html.parser.Entity;

public class contactListener implements ContactListener {
    private int playerState = 0;
    private Body platform;
    @Override
    public void beginContact(Contact contact) {
        Fixture entityA = contact.getFixtureA();
        Fixture entityB = contact.getFixtureB();

        playerContact(entityA,entityB,constants.JEDISAUR_ON_GROUND);
        int springHit = compareEntity(entityA,entityB,"springHead","foot",constants.JEDISAUR_SPRING_HIT);
        if(springHit != -1)
            playerState = springHit;

        int coinHit = compareEntity(entityA,entityB,"body","coin",constants.JEDISAUR_COIN_HIT);
        if(coinHit != -1)
            playerState = coinHit;
    }


    @Override
    public void endContact(Contact contact) {
        Fixture entityA = contact.getFixtureA();
        Fixture entityB = contact.getFixtureB();

        playerContact(entityA,entityB,constants.JEDISAUR_ON_AIR);
        int springHit = compareEntity(entityA,entityB,"springHead","foot",constants.JEDISAUR_ON_AIR);
        if(springHit != - 1)
            playerState = springHit;

        int coinHit = compareEntity(entityA,entityB,"body","coin",constants.JEDISAUR_ON_AIR);
        if(coinHit != -1)
            playerState = coinHit;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private void playerContact(Fixture entityA, Fixture entityB, int state){

        Body tempPlatform = hitPlatform(entityA, entityB);
        if(tempPlatform != null)
            platform = tempPlatform;

        int birdState = compareEntity(entityA,entityB,"body","bird",constants.JEDISAUR_BIRD_HIT);
        if(birdState != -1)
            playerState = birdState;

        int tempPlayerState = compareEntity(entityA,entityB,"platform","foot",state);
        if(tempPlayerState != -1)
            playerState = tempPlayerState;
    }
    private int compareEntity(Fixture entityA, Fixture entityB, String aName, String bName, int state){
        if((entityA.getUserData() != null && entityB.getUserData() != null) &&
            ((entityA.getUserData().equals( aName) && entityB.getUserData().equals(bName)) ||
             (entityB.getUserData().equals( aName) && entityA.getUserData().equals(bName)))
        ){
            return state;
        }
        return -1;
    }

    private Body hitPlatform(Fixture entityA, Fixture entityB){
        Body body = null;
        if((entityA.getUserData() != null && entityB.getUserData() != null) &&
                (entityA.getUserData().equals( "foot") && entityB.getUserData().equals("platform"))
        ){
            body = entityB.getBody();
        }
        else if((entityA.getUserData() != null && entityB.getUserData() != null) &&
                (entityA.getUserData().equals( "foot") && entityB.getUserData().equals("platform"))
        ){
            body = entityB.getBody();
        }
        return  body;
    }

    public int getPlayerState() {
        return playerState;
    }

    public Body getPlatform() {
        return platform;
    }
}
