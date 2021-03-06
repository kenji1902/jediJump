package com.jedijump.entity;

import com.badlogic.gdx.physics.box2d.*;
import com.jedijump.utility.constants;

import javax.swing.text.html.parser.Entity;
import java.util.HashMap;

public class contactListener implements ContactListener {
    private int playerState = 0;
    private int springStick = 0;
    private int coinState = 0;
    private Body platform;
    private HashMap<Body, Body> springPlatform;
    public contactListener(){
        springPlatform = new HashMap<>();
    }

    private Body coin;
    @Override
    public void beginContact(Contact contact) {
        Fixture entityA = contact.getFixtureA();
        Fixture entityB = contact.getFixtureB();

        int springHit = compareEntity(entityA,entityB,"springHead","foot",constants.JEDISAUR_SPRING_HIT);
        if(springHit != -1)
            playerState = springHit;

        int coinHit = compareEntity(entityA,entityB,"body","coin",constants.COIN_HIT);
        if(coinHit != -1)
            coinState = coinHit;

        Body coinTemp = hitCoin(entityA,entityB,"body");
        if(coinTemp != null)
            coin = coinTemp;

        int springFoot = compareEntity(entityA,entityB,"springFoot","platform",constants.SPRING_ON_PLATFORM);
        if(springFoot != - 1)
            springStick = springFoot;


        Body tempSpringPlatform = hitPlatform(entityA,entityB,"springFoot");
        Body tempSpring = Spring(entityA,entityB);
        if(tempSpringPlatform != null && tempSpring != null)
            springPlatform.put(tempSpring,tempSpringPlatform);

        int birdState = compareEntity(entityA,entityB,"body","bird",constants.JEDISAUR_BIRD_HIT);
        if(birdState != -1)
            playerState = birdState;

        int birdHead = compareEntity(entityA,entityB,"jumpHead","foot",constants.JEDISAUR_BIRD_HEAD_HIT);
        if(birdHead != -1)
            playerState = birdHead;
        playerContact(entityA,entityB,constants.JEDISAUR_ON_GROUND);

    }


    @Override
    public void endContact(Contact contact) {
        Fixture entityA = contact.getFixtureA();
        Fixture entityB = contact.getFixtureB();

        int springHit = compareEntity(entityA,entityB,"springHead","foot",constants.JEDISAUR_ON_AIR);
        if(springHit != - 1)
            playerState = springHit;

        int coinHit = compareEntity(entityA,entityB,"body","coin",constants.COIN_OUT);
        if(coinHit != -1)
            coinState = coinHit;

        coin = hitCoin(entityA,entityB,"body");


        int springFoot = compareEntity(entityA,entityB,"springFoot","platform",constants.SPRING_ON_AIR);
        if(springFoot != - 1)
            springStick = springFoot;

        //springPlatform = hitPlatform(entityA,entityB,"springFoot");
//        Body tempSpringPlatform = hitPlatform(entityA,entityB,"springFoot");
//        Body tempSpring = Spring(entityA,entityB);
//        if(tempSpringPlatform == null && tempSpring == null)
//            springPlatform.put(tempSpring,tempSpringPlatform);

        int birdState = compareEntity(entityA,entityB,"body","bird",constants.JEDISAUR_ON_AIR);
        if(birdState != -1)
            playerState = birdState;

        int birdHead = compareEntity(entityA,entityB,"jumpHead","foot",constants.JEDISAUR_ON_AIR);
        if(birdHead != -1)
            playerState = birdHead;

        playerContact(entityA,entityB,constants.JEDISAUR_ON_AIR);

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private void playerContact(Fixture entityA, Fixture entityB, int state){

        Body tempPlatform = hitPlatform(entityA, entityB, "foot");
        if(tempPlatform != null)
            platform = tempPlatform;

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

    private Body hitPlatform(Fixture entityA, Fixture entityB, String obj){
        Body body = null;
        if((entityA.getUserData() != null && entityB.getUserData() != null) &&
                (entityA.getUserData().equals( obj) && entityB.getUserData().equals("platform"))
        ){
            body = entityB.getBody();
        }
        else if((entityA.getUserData() != null && entityB.getUserData() != null) &&
                (entityB.getUserData().equals( obj) && entityA.getUserData().equals("platform"))
        ){
            body = entityA.getBody();
        }

        return  body;
    }
    private Body Spring(Fixture entityA, Fixture entityB){
        Body body = null;
        if((entityA.getUserData() != null && entityB.getUserData() != null) &&
                (entityA.getUserData().equals( "springFoot") && entityB.getUserData().equals("platform"))
        ){
            body = entityA.getBody();
        }
        else if((entityA.getUserData() != null && entityB.getUserData() != null) &&
                (entityB.getUserData().equals( "springFoot") && entityA.getUserData().equals("platform"))
        ){
            body = entityB.getBody();
        }

        return  body;
    }

    private Body hitCoin(Fixture entityA, Fixture entityB, String obj){
        Body body = null;
        if((entityA.getUserData() != null && entityB.getUserData() != null) &&
                (entityA.getUserData().equals( obj) && entityB.getUserData().equals("coin"))
        ){
            body = entityB.getBody();
        }
        else if((entityA.getUserData() != null && entityB.getUserData() != null) &&
                (entityB.getUserData().equals( obj) && entityA.getUserData().equals("coin"))
        ){
            body = entityA.getBody();
        }

        return  body;
    }

    public int getPlayerState() {
        return playerState;
    }

    public int getSpringStick() {
        return springStick;
    }

    public int getCoinState() {
        return coinState;
    }

    public Body getCoin() {
        return coin;
    }

    public HashMap<Body, Body> getSpringPlatform() {
        return springPlatform;
    }

    public Body getPlatform() {
        return platform;
    }
}
