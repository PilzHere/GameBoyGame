/**
 * 
 */
package mysko.pilzhere.gameboygame.entities.resources;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Disposable;

import mysko.pilzhere.gameboygame.assets.AssetsManager;
import mysko.pilzhere.gameboygame.physics.CollisionContactListener;
import mysko.pilzhere.gameboygame.screens.GameScreen;

/**
 * @author PilzHere
 *
 */
public class Coal extends Resource implements Disposable {

	public Coal(GameScreen screen, float spawnPosX, float spawnPosY) {
		super(screen, spawnPosX, spawnPosY);

		createBody(spawnPosX, spawnPosY);

		super.texRegMain = new TextureRegion(AssetsManager.MANAGER.get(AssetsManager.GUI, Texture.class), 12, 11, 6, 6);
		super.spriteMain = new Sprite(texRegMain);
	}

	private void createBody(float bodySpawnX, float bodySpawnY) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.allowSleep = true;
		bodyDef.fixedRotation = true;
		bodyDef.position.set(bodySpawnX, bodySpawnY); // spawnPos

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(4 * 8 / GameScreen.PPM, 4 * 8 / GameScreen.PPM);

		FixtureDef fixtDef = new FixtureDef();
		fixtDef.filter.categoryBits = CollisionContactListener.RESOURCE_COL;
		fixtDef.filter.maskBits = CollisionContactListener.RESOURCE_COLLIDES_WITH;
		fixtDef.density = 1.0f;
		fixtDef.friction = 0.65f;
		fixtDef.restitution = 0.0f; // Bounciness
		fixtDef.shape = shape;

		super.body = super.getScreen().b2dWorld.createBody(bodyDef);
		super.body.createFixture(fixtDef);
		super.body.setUserData(this);
		super.body.setGravityScale(2);

		shape.dispose();
		
		super.body.setLinearVelocity(new Vector2(MathUtils.random(-30, 30), MathUtils.random(0, 55)));
		
		
	}

	@Override
	public void tick(float delta) {
		super.tick(delta);

		super.spriteMain.setPosition(super.body.getTransform().getPosition().x - super.spriteMain.getRegionWidth() / 2,
				super.body.getTransform().getPosition().y - 0.99f - super.spriteMain.getRegionHeight() / 2);
		
		
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		super.bodyCenter.set(new Vector3(super.body.getPosition().x, super.body.getPosition().y, 0));
		super.bodyDimensions.set(new Vector3(4 + super.bBExtra / GameScreen.PPM, 4 + super.bBExtra / GameScreen.PPM, 0));
		if (super.getScreen().isInFrustum(super.bodyCenter, super.bodyDimensions)) {
			super.render = true;
		} else {
			super.render = false;
		}

		if (super.render) {
			super.spriteMain.draw(super.getScreen().game.batch);
		}
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
