package com.mygdx.game;


import static com.mygdx.game.Box2DExp.*;

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

public class DynamicBody {
    private float x, y;
    private float r;
    private float width, height;
    private Body body;
    private Fixture fixture, fixture2;
    public int type;
    public int click;

    DynamicBody(World world, float x, float y, float r){
        type = TYPE_CIRCLE;
        this.x = x;
        this.y = y;
        this.r = r;
        width = height = r*2;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(r);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.7f; // плотность
        fixtureDef.friction = 0.4f; // трение
        fixtureDef.restitution = 0.6f; // упругость

        fixture = body.createFixture(fixtureDef);

        shape.dispose();
    }

    DynamicBody(World world, float x, float y, float width, float height){
        type = TYPE_BRICK;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2, height/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.7f; // плотность
        fixtureDef.friction = 0.4f; // трение
        fixtureDef.restitution = 0.5f; // упругость

        fixture = body.createFixture(fixtureDef);

        shape.dispose();
    }

    DynamicBody(World world, float x, float y, Polygon p){
        type = TYPE_POLY;
        this.x = x;
        this.y = y;
        this.width = p.getBoundingRectangle().getWidth()/2.5f;
        this.height = p.getBoundingRectangle().getHeight()/2.5f;
        for(int i=0; i<p.getVertices().length; i++){
            p.getVertices()[i] /= 2.5f;
        }

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.set(p.getVertices());

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.7f; // плотность
        fixtureDef.friction = 0.4f; // трение
        fixtureDef.restitution = 0.5f; // упругость

        fixture = body.createFixture(fixtureDef);

        shape.dispose();
    }

    DynamicBody(World world, float x, float y, Polygon p1, Polygon p2){
        type = TYPE_2POLY;
        this.x = x;
        this.y = y;

        for(int i=0; i<p1.getVertices().length; i++){
            p1.getVertices()[i] /= 2f;
        }
        for(int i=0; i<p2.getVertices().length; i++){
            p2.getVertices()[i] /= 2f;
        }

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.set(p1.getVertices());
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.7f; // плотность
        fixtureDef.friction = 0.4f; // трение
        fixtureDef.restitution = 0.4f; // упругость

        PolygonShape shape2 = new PolygonShape();
        shape2.set(p2.getVertices());
        FixtureDef fixtureDef2 = new FixtureDef();
        fixtureDef2.shape = shape2;
        fixtureDef2.density = 0.7f; // плотность
        fixtureDef2.friction = 0.4f; // трение
        fixtureDef2.restitution = 0.4f; // упругость
        fixture = body.createFixture(fixtureDef);
        fixture2 = body.createFixture(fixtureDef2);

        shape.dispose();
        shape2.dispose();

        body.setTransform(body.getPosition(), MathUtils.random(0, 360)*MathUtils.degreesToRadians);
    }

    DynamicBody (World world, Fixture f){
        type = TYPE_POLY;
        this.width = 0.2f;
        this.height = 0.2f;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(f.getBody().getPosition().x, f.getBody().getPosition().y);
        bodyDef.angle = f.getBody().getAngle();

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = f.getShape();
        fixtureDef.density = f.getDensity();
        fixtureDef.friction = f.getFriction();
        fixtureDef.restitution = f.getRestitution();

        fixture = body.createFixture(fixtureDef);
    }

    public void applySlashImpulse(float force, boolean direction) {
        float angle = body.getAngle()+90*MathUtils.degreesToRadians;// хз зачем +90!
        if(direction) angle += 180*MathUtils.degreesToRadians;
        Vector2 impulse = new Vector2(force * MathUtils.cos(angle), force * MathUtils.sin(angle));
        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
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

    public Body getBody() {
        return body;
    }

    public boolean hit(float tx, float ty) {
        for(Fixture f: body.getFixtureList()) {
            if(f.testPoint(tx, ty)) {
                click++;
                return f.testPoint(tx, ty);
            }
        }
        return false;
    }

    public void setImpulse(Vector2 p){
        body.applyLinearImpulse(p, body.getWorldCenter(), true);
    }
}
