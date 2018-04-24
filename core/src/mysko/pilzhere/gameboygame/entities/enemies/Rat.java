/**
 * 
 */
package mysko.pilzhere.gameboygame.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.utils.Disposable;

import mysko.pilzhere.gameboygame.assets.AssetsManager;
import mysko.pilzhere.gameboygame.entities.Entity;
import mysko.pilzhere.gameboygame.entities.Player;
import mysko.pilzhere.gameboygame.physics.CollisionContactListener;
import mysko.pilzhere.gameboygame.screens.GameScreen;

/**
 * @author PilzHere
 *
 */
public class Rat extends Entity implements Disposable, RayCastCallback {
	private TextureRegion texRegIdleLeft;
	// private Animation<TextureRegion> animIdleLeft;
	private Sprite spriteIdleLeft;

	private TextureRegion[] texRegsWalkingLeft;
	private Animation<TextureRegion> animWalkingLeft;
	private Sprite spriteWalkingLeft;

	public boolean remove = false;
	public Body body;
	public int hp = 5;

	private boolean facingLeft = true;

	public Rat(GameScreen screen, float spawnPosX, float spawnPosY) {
		super.setScreen(screen);

		texRegIdleLeft = new TextureRegion(AssetsManager.MANAGER.get(AssetsManager.SPIDER, Texture.class), 1, 14, 30,
				10);
		spriteIdleLeft = new Sprite(texRegIdleLeft);

		createBody(spawnPosX, spawnPosY);

		setupSpriteWalkingLeft(AssetsManager.MANAGER.get(AssetsManager.SPIDER, Texture.class));
	}

	private void createBody(float bodySpawnX, float bodySpawnY) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.allowSleep = false;
		bodyDef.fixedRotation = true;
		bodyDef.position.set(bodySpawnX + 10, bodySpawnY + 6); // spawnPos

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(12 * 8 / GameScreen.PPM, 8 * 8 / GameScreen.PPM);

		FixtureDef fixtDef = new FixtureDef();
		fixtDef.filter.categoryBits = CollisionContactListener.ENEMY_COL;
		fixtDef.filter.maskBits = CollisionContactListener.ENEMY_COLLIDES_WITH;
		fixtDef.density = 100.0f;
		fixtDef.friction = 0.0f;
		fixtDef.restitution = 0.0f; // Bounciness
		fixtDef.shape = shape;

		body = super.getScreen().b2dWorld.createBody(bodyDef);
		body.createFixture(fixtDef);
		body.setUserData(this);
		body.setGravityScale(2);

		shape.dispose();
	}

	private void setupSpriteWalkingLeft(Texture tex) {
		TextureRegion texRegWalkingLeft1 = new TextureRegion(tex, 1, 14, 30, 10);
		TextureRegion texRegWalkingLeft2 = new TextureRegion(tex, 32, 14, 30, 10);
		TextureRegion texRegWalkingLeft3 = new TextureRegion(tex, 63, 14, 30, 10);

		texRegsWalkingLeft = new TextureRegion[4];
		texRegsWalkingLeft[0] = texRegWalkingLeft1;
		texRegsWalkingLeft[1] = texRegWalkingLeft2;
		texRegsWalkingLeft[2] = texRegWalkingLeft3;
		texRegsWalkingLeft[3] = texRegWalkingLeft2;

		int index = 0;
		for (int i = 0; i < 2; i++) {
			texRegsWalkingLeft[index++] = texRegsWalkingLeft[i];
		}

		float frameDur = 1f / 4f;
		animWalkingLeft = new Animation<TextureRegion>(frameDur, texRegsWalkingLeft);

		spriteWalkingLeft = new Sprite(animWalkingLeft.getKeyFrame(0));
	}

	private boolean groundedLeft = false;
	private boolean groundedRight = false;

	private boolean rayGroundedLeft = false;
	private boolean rayGroundedRight = false;
	private float tempFractLeft; // used to detect if in air. 0 = in air.
	private float tempFractRight; // used to detect if in air. 0 = in air.

	private boolean rayLeft = false;
	private boolean rayRight = false;

	private Vector2 groundedLeftRayStart = new Vector2();
	private Vector2 groundedLeftRayEnd = new Vector2();
	private Vector2 groundedRightRayStart = new Vector2();
	private Vector2 groundedRightRayEnd = new Vector2();

	private Vector2 leftRayEnd = new Vector2();
	private Vector2 rightRayEnd = new Vector2();

	private Vector2 downLeft = new Vector2(-4, -4);
	private Vector2 downLeft2 = new Vector2(-4, -4.1f);
	private Vector2 downRight = new Vector2(4, -4);
	private Vector2 downRight2 = new Vector2(4, -4.1f);

	private Vector2 left = new Vector2(-10, 0);
	private Vector2 right = new Vector2(10, 0);

	private final int moveSpeed = 50;
	private Vector2 moveLeft = new Vector2();
	private Vector2 moveRight = new Vector2();

	private boolean movingLeft = true;

	private void aiTick() {
		tempFractLeft = 0;
		tempFractRight = 0;

		groundedLeftRayStart.set(body.getTransform().getPosition().add(downLeft));
		groundedLeftRayEnd.set(body.getTransform().getPosition().add(downLeft2));

		groundedRightRayStart.set(body.getTransform().getPosition().add(downRight));
		groundedRightRayEnd.set(body.getTransform().getPosition().add(downRight2));

		leftRayEnd.set(body.getTransform().getPosition().add(left));
		rightRayEnd.set(body.getTransform().getPosition().add(right));

		rayGroundedLeft = true;
		super.getScreen().b2dWorld.rayCast(this, groundedLeftRayStart, groundedLeftRayEnd);
		rayGroundedLeft = false;
		rayGroundedRight = true;
		super.getScreen().b2dWorld.rayCast(this, groundedRightRayStart, groundedRightRayEnd);
		rayGroundedRight = false;

		rayLeft = true;
		super.getScreen().b2dWorld.rayCast(this, body.getTransform().getPosition(), leftRayEnd);
		rayLeft = false;
		rayRight = true;
		super.getScreen().b2dWorld.rayCast(this, body.getTransform().getPosition(), rightRayEnd);
		rayRight = false;

		if (tempFractLeft != 0 && tempFractRight == 0) {
			movingLeft = true;
		} else if (tempFractLeft == 0 && tempFractRight != 0) {
			movingLeft = false;
		}

		moveLeft.set(-moveSpeed, body.getLinearVelocity().y);
		moveRight.set(moveSpeed, body.getLinearVelocity().y);

		if (movingLeft) {
			facingLeft = true;
			body.setLinearVelocity(moveLeft);
		} else {
			facingLeft = false;
			body.setLinearVelocity(moveRight);
		}
	}

	public void onHit() {
		this.hp--;

		if (hp <= 0) {
			if (!super.getScreen().b2dWorld.isLocked()) {
				super.getScreen().b2dWorld.destroyBody(this.body);
				super.getScreen().enemies.removeValue(this, true);
			}
		}

		movingLeft = !movingLeft;
	}

	public void onCollisionContinuos(Fixture fixture) {
		if (fixture.getFilterData().categoryBits == CollisionContactListener.PLAYER_COL) {
			((Player) fixture.getBody().getUserData()).hurt(1);
		}
	}

	public void remove() {
		getScreen().blocks.removeValue(this, true);
	}

	private final float animSpeed = 2.5f;
	private final float spriteOffsetY = 0.99f;
	float elapsedTime = 0;

	@Override
	public void tick(float delta) {
		elapsedTime += Gdx.graphics.getDeltaTime();

		aiTick();

		spriteWalkingLeft.setRegion(animWalkingLeft.getKeyFrame(elapsedTime * animSpeed, true));

		if (facingLeft) {
			spriteIdleLeft.setFlip(false, false);
			spriteWalkingLeft.setFlip(false, false);
		} else {
			spriteIdleLeft.setFlip(true, false);
			spriteWalkingLeft.setFlip(true, false);
		}

		spriteIdleLeft.setPosition(body.getTransform().getPosition().x - spriteIdleLeft.getRegionWidth() / 2,
				body.getTransform().getPosition().y + spriteOffsetY - spriteIdleLeft.getRegionHeight() / 2);
		spriteWalkingLeft.setPosition(body.getTransform().getPosition().x - spriteWalkingLeft.getRegionWidth() / 2,
				body.getTransform().getPosition().y + spriteOffsetY - spriteWalkingLeft.getRegionHeight() / 2);
	}

	@Override
	public void render(float delta) {
		super.bodyCenter.set(new Vector3(body.getPosition().x, body.getPosition().y, 0));
		super.bodyDimensions
				.set(new Vector3(12 + super.bBExtra / GameScreen.PPM, 8 + super.bBExtra / GameScreen.PPM, 0));

		if (super.getScreen().isInFrustum(super.bodyCenter, super.bodyDimensions)) {
			super.render = true;
		} else {
			super.render = false;
		}

		if (super.render) {
			if (body.getLinearVelocity().x == 0) {
				spriteIdleLeft.draw(super.getScreen().game.batch);
			} else {
				spriteWalkingLeft.draw(super.getScreen().game.batch);
			}
		}
	}

	@Override
	public void dispose() {

	}

	@Override
	public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
		if (!super.getScreen().b2dWorld.isLocked()) {
			if (rayGroundedLeft) {
				tempFractLeft = fraction; // if 0 = in air
				if (fixture.getFilterData().categoryBits == CollisionContactListener.TERRAIN_COL
						|| fixture.getFilterData().categoryBits == CollisionContactListener.BLOCK_COL) {
					groundedLeft = true;
				} else {
					groundedLeft = false;
				}
			} else if (rayGroundedRight) {
				tempFractRight = fraction; // if 0 = in air
				if (fixture.getFilterData().categoryBits == CollisionContactListener.TERRAIN_COL
						|| fixture.getFilterData().categoryBits == CollisionContactListener.BLOCK_COL) {
					groundedRight = true;
				} else {
					groundedRight = false;
				}
			} else if (rayLeft) {
				if (fixture.getFilterData().categoryBits == CollisionContactListener.TERRAIN_COL
						|| fixture.getFilterData().categoryBits == CollisionContactListener.BLOCK_COL) {
					movingLeft = false;
				}
			} else if (rayRight) {
				if (fixture.getFilterData().categoryBits == CollisionContactListener.TERRAIN_COL
						|| fixture.getFilterData().categoryBits == CollisionContactListener.BLOCK_COL) {
					movingLeft = true;
				}
			}
		}
		return 0;
	}
}