package com.mygdx.game;


import static com.mygdx.game.Box2DExp.TYPE_BRICK;
import static com.mygdx.game.Box2DExp.TYPE_POLY;
import static com.mygdx.game.Box2DExp.TYPE_SMILE;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class DynamicBody2 {
    private float x, y;
    private float r;
    private float width, height;
    private Body body;
    private Fixture fixture;
    public int type;

    DynamicBody2(World world, Fixture f){
        type = TYPE_POLY;
        this.width = 2;
        this.height = 2;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(f.getBody().getPosition().x, f.getBody().getPosition().y);

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = f.getShape();
        fixtureDef.density = f.getDensity();
        fixtureDef.friction = f.getFriction();
        fixtureDef.restitution = f.getRestitution();

        fixture = body.createFixture(fixtureDef);
    }

    public float getX() {
        return body.getPosition().x-width/2;
    }

    public float getY() {
        return body.getPosition().y-height/2;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getAngle() {
        return body.getAngle() * MathUtils.radiansToDegrees;
    }

    public boolean hit(float tx, float ty) {
        return fixture.testPoint(tx, ty);
    }

    public void setImpulse(Vector2 p){
        body.applyLinearImpulse(p, body.getPosition(), true);
    }
}
