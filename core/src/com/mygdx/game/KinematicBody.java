package com.mygdx.game;

import static com.mygdx.game.Box2DExp.*;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class KinematicBody {
    private float x, y;
    private float width, height;
    private Body body;
    private float vx = 0, vy = 0;
    private float va = 0;

    KinematicBody(World world, float x, float y, float width, float height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2, height/2);

        body.createFixture(shape, 0);
        shape.dispose();

        body.setLinearVelocity(vx, vy);
        body.setAngularVelocity(va);
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

    public  float getAngle() {
        return body.getAngle() * MathUtils.radiansToDegrees;
    }

    public void move() {
        x = body.getPosition().x;
        if(x > WORLD_WIDTH | x <0) {
            vx = -vx;
            va = -va;
            body.setLinearVelocity(vx, vy);
            body.setAngularVelocity(va);
        }
    }
}
