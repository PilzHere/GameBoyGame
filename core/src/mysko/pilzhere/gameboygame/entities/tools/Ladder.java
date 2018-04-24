/**
 * 
 */
package mysko.pilzhere.gameboygame.entities.tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Disposable;

import mysko.pilzhere.gameboygame.assets.AssetsManager;
import mysko.pilzhere.gameboygame.entities.Entity;
import mysko.pilzhere.gameboygame.physics.CollisionContactListener;
import mysko.pilzhere.gameboygame.screens.GameScreen;

/**
 * @author PilzHere
 *
 */
public class Ladder extends Entity implements Disposable {

	private TextureRegion texRegLadderVertical;
	private Sprite spriteLadderVertical;

	public boolean remove = false;
	public Body body;

	public Ladder(GameScreen screen, float spawnPosX, float spawnPosY) {
		super.setScreen(screen);

		texRegLadderVertical = new TextureRegion(AssetsManager.MANAGER.get(AssetsManager.ATLAS01, Texture.class), 110,
				2, 16, 16);
		spriteLadderVertical = new Sprite(texRegLadderVertical);

		createBody(spawnPosX, spawnPosY);
	}

	private void createBody(float bodySpawnX, float bodySpawnY) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.allowSleep = true;
		bodyDef.fixedRotation = true;
		bodyDef.position.set(bodySpawnX + 10, bodySpawnY + 6); // spawnPos

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(10 * 8 / GameScreen.PPM, 16 * 8 / GameScreen.PPM);

		FixtureDef fixtDef = new FixtureDef();
		fixtDef.filter.categoryBits = CollisionContactListener.LADDER_COL;
		fixtDef.filter.maskBits = CollisionContactListener.LADDER_COLLIDES_WITH;
		fixtDef.density = 1.0f;
		fixtDef.friction = 0.0f;
		fixtDef.restitution = 0.0f; // Bounciness
		fixtDef.isSensor = true;
		fixtDef.shape = shape;

		body = super.getScreen().b2dWorld.createBody(bodyDef);
		body.createFixture(fixtDef);
		body.setUserData(this);

		shape.dispose();
	}

	public void remove() {
		getScreen().blocks.removeValue(this, true);
	}

	@Override
	public void tick(float delta) {
		spriteLadderVertical.setPosition(
				body.getTransform().getPosition().x - spriteLadderVertical.getRegionWidth() / 2,
				body.getTransform().getPosition().y - spriteLadderVertical.getRegionHeight() / 2);
	}

	@Override
	public void render(float delta) {
		super.bodyCenter.set(new Vector3(body.getPosition().x, body.getPosition().y, 0));
		super.bodyDimensions
				.set(new Vector3(10 + super.bBExtra / GameScreen.PPM, 16 + super.bBExtra / GameScreen.PPM, 0));

		if (super.getScreen().isInFrustum(super.bodyCenter, super.bodyDimensions)) {
			super.render = true;
		} else {
			super.render = false;
		}

		if (super.render) {
			spriteLadderVertical.draw(super.getScreen().game.batch);
		}
	}

	@Override
	public void dispose() {

	}
}
