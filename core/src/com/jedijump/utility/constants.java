package com.jedijump.utility;

public class constants {
    public static final int PPM = 32;
    public static final float SCALE = 2.0f;
    public static final float JEDISAUR_VELOCITY_X = 11;
    public static final float JEDISAUR_VELOCITY_Y = 1000;
    public static final float JEDISAUR_FRICTION = 0.5f;
    public static final float JEDISAUR_JUMP_BOOST = 20f;

    // WINDOW
    public static final int SCREENWIDTH = 320;
    public static final int SCREENHEIGHT = 480;
    public static final float BOUNDARY = SCREENWIDTH/SCALE/PPM;
    public static final float FORCEFIELD = 0.5f;
    public static final float FORCEPUSH = 20;

    // STATES
    public static final int MENU_STATE = 0;
    public static final int PLAY_STATE = 1;

    // PLAYER STATES
    public static final int JEDISAUR_ON_GROUND = 0;
    public static final int JEDISAUR_ON_AIR = 1;
    public static final int JEDISAUR_BIRD_HIT = 2;
    public static final int JEDISAUR_SPRING_HIT = 4;
    public static final int JEDISAUR_COIN_HIT = 5;

    // PLATFORM STATES
    public static final int PLATFORM_STATIC = 0;
    public static final int PLATFORM_BREAK = 1;

    // SPRING STATES
    public static final int SPRING_ON_AIR = 0;
    public static final int SPRING_ON_PLATFORM = 1;

    // COIN
    public static final int COIN_SCORE = 10;

    // BIRD
    public static final float BIRD_SPEED = 5;

    //DEBRI
    public static final float DEBRI_SPEED = 3;

    // PLATFORM
    public static final float PLATFORM_SPEED = 3;

}
