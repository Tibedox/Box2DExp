package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class Box2DExp extends ApplicationAdapter {
	// глобальные константы
	public static final float WORLD_WIDTH = 16, WORLD_HEIGHT = 9;
	public static final int TYPE_CIRCLE = 0, TYPE_BRICK = 1, TYPE_POLY = 2, TYPE_2POLY = 3;

	// системные объекты
	SpriteBatch batch;
	OrthographicCamera camera;
	Vector3 touch;
	World world;
	Box2DDebugRenderer debugRenderer;

	// ресурсы
	Texture imgLootAtlas;
	TextureRegion imgBoxLightGray;
	TextureRegion imgBoxMediumGray;
	TextureRegion imgBoxPurple;
	TextureRegion imgCircle;
	TextureRegion imgTriPurple;
	TextureRegion imgLoot;

	// наши объекты и переменные
	StaticBody floor;
	StaticBody wallLeft, wallRight;

	KinematicBody platform;

	Array<DynamicBody> balls = new Array<>();

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
		touch = new Vector3();
		world = new World(new Vector2(0, -9.8f), true);
		debugRenderer = new Box2DDebugRenderer();
		//debugRenderer.setDrawVelocities(true);

		imgLootAtlas = new Texture("atlasloot.png");
		imgBoxLightGray = new TextureRegion(imgLootAtlas, 0, 0, 256, 256);
		imgBoxMediumGray = new TextureRegion(imgLootAtlas, 256, 0, 256, 256);
		imgBoxPurple = new TextureRegion(imgLootAtlas, 256*2, 0, 256, 256);
		imgCircle = new TextureRegion(imgLootAtlas, 256*3, 0, 256, 256);
		imgTriPurple = new TextureRegion(imgLootAtlas, 256, 256, 256, 256);

		floor = new StaticBody(world, 8, 0.5f, 16, 1);
		wallLeft = new StaticBody(world, 0.5f, 5, 1, 8);
		wallRight = new StaticBody(world, 15.5f, 5, 1, 8);

		//platform = new KinematicBody(world, 0, 2.5f, 3, 1);

		for (int i = 0; i < 50; i++) {
			/*Polygon polygon = new Polygon(new float[]{-1, 0, 0, 1.5f, 1, 0});
			Polygon polygon2 = new Polygon(new float[]{-1, 0, 0, -1, 1, 0});
			balls.add(new DynamicBody(world, MathUtils.random(1f, 15f), i, polygon, polygon2));*/

			if(i%4 == 0) {
				Polygon polygon = new Polygon(new float[]{-1, 0, 0, 1, 1, 0});
				Polygon polygon2 = new Polygon(new float[]{-1, 0, 0, -1, 1, 0});
				balls.add(new DynamicBody(world, 8 + MathUtils.random(-1.01f, 1.01f), 9 + i/2, polygon, polygon2));
			}
			if(i%4 == 1) {
				balls.add(new DynamicBody(world, 8 + MathUtils.random(-0.01f, 0.01f), WORLD_HEIGHT + i*2, 0.4f));
			}
			if(i%4 == 2) {
				balls.add(new DynamicBody(world, 8 + MathUtils.random(-0.01f, 0.01f), WORLD_HEIGHT + i*2, 1, 0.5f));
			}
			if(i%4 == 3) {
				Polygon polygon = new Polygon(new float[]{-1, -1, 1, -1, 0, 1});
				balls.add(new DynamicBody(world, 8 + MathUtils.random(-0.01f, 0.01f), WORLD_HEIGHT + i*2, polygon));
			}
		}
	}

	@Override
	public void render () {
		// касания
		if(Gdx.input.justTouched()){
			touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touch);
			for (int i=0; i<balls.size; i++){
				if(balls.get(i).hit(touch.x, touch.y)){
					//if(balls.get(i).type == TYPE_2POLY) balls.get(i).setImpulse(new Vector2(0, 1));
					if(balls.get(i).click % 1 == 0 & balls.get(i).type == TYPE_2POLY){
						balls.add(new DynamicBody(world, balls.get(i).getBody().getFixtureList().get(0)));
						balls.add(new DynamicBody(world, balls.get(i).getBody().getFixtureList().get(1)));
						balls.get(balls.size-1).applySlashImpulse(3, true);
						balls.get(balls.size-2).applySlashImpulse(3, false);
						world.destroyBody(balls.get(i).getBody());
						balls.removeIndex(i);
					} else {
						balls.get(i).setImpulse(new Vector2(0, 3));
					}
				}
			}
		}

		// события
		//platform.move();

		// отрисовка
		world.step(1/60f, 6, 2);
		ScreenUtils.clear(0.2f, 0, 0.3f, 1);
		debugRenderer.render(world, camera.combined);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(imgBoxMediumGray, floor.getX(), floor.getY(), floor.getWidth(), floor.getHeight());
		batch.draw(imgBoxMediumGray, wallLeft.getX(), wallLeft.getY(), wallLeft.getWidth(), wallLeft.getHeight());
		batch.draw(imgBoxMediumGray, wallRight.getX(), wallRight.getY(), wallRight.getWidth(), wallRight.getHeight());
		/*batch.draw(imgBoxLightGray, platform.getX(), platform.getY(), platform.getWidth()/2, platform.getHeight()/2,
				platform.getWidth(), platform.getHeight(), 1, 1, platform.getAngle());*/
		for (DynamicBody b: balls) {
			if(b.type == TYPE_CIRCLE) imgLoot = imgCircle;
			else if(b.type == TYPE_BRICK) imgLoot = imgBoxPurple;
			else imgLoot = imgTriPurple;
			batch.draw(imgLoot, b.getX(), b.getY(), b.getWidth()/2, b.getHeight()/2, b.getWidth(), b.getHeight(), 1, 1, b.getAngle());
		}
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		imgLootAtlas.dispose();
		world.dispose();
		debugRenderer.dispose();
	}
}
