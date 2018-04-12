/**
 * 
 */
package mysko.pilzhere.gameboygame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.utils.Disposable;

import mysko.pilzhere.gameboygame.assets.AssetsManager;
import mysko.pilzhere.gameboygame.entities.blocks.DirtBlock;
import mysko.pilzhere.gameboygame.screens.GameScreen;

/**
 * @author PilzHere
 *
 */
public class Player extends Entity implements Disposable, RayCastCallback {

	private TextureRegion texRegIdleLeft;
	private Sprite spriteIdleLeft;
	
	private TextureRegion[] texRegsWalkingLeft;
	private Animation<TextureRegion> animWalkingLeft;
	private Sprite spriteWalkingLeft;
	
	private TextureRegion[] texRegsDiggingLeft;
	private Animation<TextureRegion> animDiggingLeft;
	private Sprite spriteDiggingLeft;
	
	private TextureRegion[] texRegsDiggingDown;
	private Animation<TextureRegion> animDiggingDown;
	private Sprite spriteDiggingDown;
	
	private TextureRegion[] texRegsDiggingUp;
	private Animation<TextureRegion> animDiggingUp;
	private Sprite spriteDiggingUp;
	
	private Body body;
	
	public Vector2 cameraPos = new Vector2();
	
	private boolean facingLeft = true;
	private boolean facingDown = false;
	private boolean facingUp = false;
	
//	Actions
	private boolean movingLR = false;
	private boolean digging = false;
	
	private Vector2 rayEnd = new Vector2();
//	private boolean facingUp; 
	
	public Player(GameScreen screen) {
		super.setScreen(screen);
		
		texRegIdleLeft = new TextureRegion(AssetsManager.MANAGER.get(AssetsManager.PLAYER, Texture.class), 11, 1, 8, 12);
		spriteIdleLeft = new Sprite(texRegIdleLeft);
		
		setupSpriteWalkingLeft(AssetsManager.MANAGER.get(AssetsManager.PLAYER, Texture.class));
		setupSpriteDiggingLeft(AssetsManager.MANAGER.get(AssetsManager.PLAYER, Texture.class));
		setupSpriteDiggingDown(AssetsManager.MANAGER.get(AssetsManager.PLAYER, Texture.class));
		setupSpriteDiggingUp(AssetsManager.MANAGER.get(AssetsManager.PLAYER, Texture.class));
		
//		Body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.allowSleep = false;
		bodyDef.fixedRotation = true;
		bodyDef.position.set(10 * 16, 44 * 16); // spawnPos

		PolygonShape shape = new PolygonShape();
//		CircleShape shape = new CircleShape();
		shape.setAsBox(7.25f * 8 / super.getScreen().PPM, 10 * 8 / super.getScreen().PPM);
//		shape.setRadius(8 * 8 / GameScreen.PPM);

		FixtureDef fixtDef = new FixtureDef();
		fixtDef.filter.categoryBits = screen.PLAYER_COL;
		fixtDef.filter.maskBits = screen.PLAYER_COLLIDES_WITH;
		fixtDef.density = 1.0f;
		fixtDef.friction = 0.0f;
		fixtDef.restitution = 0.0f; // Bounciness
		fixtDef.shape = shape;

		body = screen.b2dWorld.createBody(bodyDef);

		body.createFixture(fixtDef);
//		body.setUserData(super.id);
		body.setGravityScale(2);

		shape.dispose();
	}
	
	private void setupSpriteWalkingLeft(Texture tex) {
		TextureRegion texRegWalkingLeft1 = new TextureRegion(tex, 20, 1, 8, 12);
		TextureRegion texRegWalkingLeft2 = new TextureRegion(tex, 11, 1, 8, 12);
		TextureRegion texRegWalkingLeft3 = new TextureRegion(tex, 29, 1, 8, 12);
		
		texRegsWalkingLeft = new TextureRegion[3];
		texRegsWalkingLeft[0] = texRegWalkingLeft1;
		texRegsWalkingLeft[1] = texRegWalkingLeft3;
		texRegsWalkingLeft[2] = texRegWalkingLeft2;
		
		int index = 0;
		for (int i = 0; i < 2; i++) {
			texRegsWalkingLeft[index++] = texRegsWalkingLeft[i];
		}

		float frameDur = 1f / 4f;
		animWalkingLeft = new Animation<TextureRegion>(frameDur, texRegsWalkingLeft);
		
		spriteWalkingLeft = new Sprite(animWalkingLeft.getKeyFrame(0));
	}
	
	private void setupSpriteDiggingLeft(Texture tex) {
		TextureRegion texRegDiggingLeft1 = new TextureRegion(tex, 38, 1, 13, 12);
		TextureRegion texRegDiggingLeft2 = new TextureRegion(tex, 52, 1, 13, 12);
		TextureRegion texRegDiggingLeft3 = new TextureRegion(tex, 66, 1, 13, 12);
		
		texRegsDiggingLeft = new TextureRegion[3];
		texRegsDiggingLeft[0] = texRegDiggingLeft1;
		texRegsDiggingLeft[1] = texRegDiggingLeft2;
		texRegsDiggingLeft[2] = texRegDiggingLeft3;
		
		int index = 0;
		for (int i = 0; i < 2; i++) {
			texRegsDiggingLeft[index++] = texRegsDiggingLeft[i];
		}

		float frameDur = 1f / 4f;
		animDiggingLeft = new Animation<TextureRegion>(frameDur, texRegsDiggingLeft);
		
		spriteDiggingLeft = new Sprite(animDiggingLeft.getKeyFrame(0));
	}
	
	private void setupSpriteDiggingDown(Texture tex) {
		TextureRegion texRegDiggingDown1 = new TextureRegion(tex, 38, 14, 13, 11);
		TextureRegion texRegDiggingDown2 = new TextureRegion(tex, 52, 14, 13, 11);
		TextureRegion texRegDiggingDown3 = new TextureRegion(tex, 66, 14, 13, 11);
		
		texRegsDiggingDown = new TextureRegion[3];
		texRegsDiggingDown[0] = texRegDiggingDown1;
		texRegsDiggingDown[1] = texRegDiggingDown2;
		texRegsDiggingDown[2] = texRegDiggingDown3;
		
		int index = 0;
		for (int i = 0; i < 2; i++) {
			texRegsDiggingDown[index++] = texRegsDiggingDown[i];
		}

		float frameDur = 1f / 4f;
		animDiggingDown = new Animation<TextureRegion>(frameDur, texRegsDiggingDown);
		
		spriteDiggingDown = new Sprite(animDiggingDown.getKeyFrame(0));
	}
	
	private void setupSpriteDiggingUp(Texture tex) {
		TextureRegion texRegDiggingUp1 = new TextureRegion(tex, 38, 26, 14, 16);
		TextureRegion texRegDiggingUp2 = new TextureRegion(tex, 52, 26, 14, 16);
		TextureRegion texRegDiggingUp3 = new TextureRegion(tex, 66, 26, 14, 16);
		
		texRegsDiggingUp = new TextureRegion[3];
		texRegsDiggingUp[0] = texRegDiggingUp1;
		texRegsDiggingUp[1] = texRegDiggingUp2;
		texRegsDiggingUp[2] = texRegDiggingUp3;
		
		int index = 0;
		for (int i = 0; i < 2; i++) {
			texRegsDiggingUp[index++] = texRegsDiggingUp[i];
		}

		float frameDur = 1f / 4f;
		animDiggingUp = new Animation<TextureRegion>(frameDur, texRegsDiggingUp);
		
		spriteDiggingUp = new Sprite(animDiggingUp.getKeyFrame(0));
	}
	
	private void handleInput(float delta) {		
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			if (!digging) {
				facingLeft = false;
				body.setLinearVelocity(new Vector2(60, body.getLinearVelocity().y));
				movingLR = true;	
			}
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			if (!digging) {
				facingLeft = true;
				body.setLinearVelocity(new Vector2(-60, body.getLinearVelocity().y));
				movingLR = true;	
			}
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			if (!facingUp)
			facingDown = true;
		} else {
			facingDown = false;
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			if (!facingDown)
			facingUp = true;
		} else {
			facingUp = false;
		}
		
		if (!Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			body.setLinearVelocity(new Vector2(0, body.getLinearVelocity().y));
			movingLR = false;
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 85));
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
			movingLR = false;
			if (!digging) {
				digging = true;
				
				elapsedTime = 0;
				
				if (!facingLeft) {
					if (facingDown) {
						rayEnd.set(new Vector2(body.getTransform().getPosition().add(new Vector2(0, -6))));
					} else if (facingUp){
						rayEnd.set(new Vector2(body.getTransform().getPosition().add(new Vector2(0, 12))));
					} else {
						rayEnd.set(new Vector2(body.getTransform().getPosition().add(new Vector2(6, 0))));
					}
				} else {
					if (facingDown) {
						rayEnd.set(new Vector2(body.getTransform().getPosition().add(new Vector2(0, -6))));
					} else if (facingUp){
						rayEnd.set(new Vector2(body.getTransform().getPosition().add(new Vector2(0, 12))));
					} else {
						rayEnd.set(new Vector2(body.getTransform().getPosition().add(new Vector2(-6, 0))));
					}
				}
				super.getScreen().b2dWorld.rayCast(this, body.getTransform().getPosition(), rayEnd);
			}
		}
	}
	
	private float elapsedTime = 0;
	private final float animPlaySpeed1 = 2.5f;
	private final float animPlaySpeed2 = 4.2f;
	@Override
	public void tick(float delta) {
		elapsedTime += Gdx.graphics.getDeltaTime();
		
		handleInput(delta);
		
		if (digging) {
			body.setLinearVelocity(new Vector2(0, body.getLinearVelocity().y));
		}
		
		spriteWalkingLeft.setRegion(animWalkingLeft.getKeyFrame(elapsedTime * animPlaySpeed1, true));
		spriteDiggingLeft.setRegion(animDiggingLeft.getKeyFrame(elapsedTime * animPlaySpeed2, false));
		spriteDiggingDown.setRegion(animDiggingDown.getKeyFrame(elapsedTime * animPlaySpeed2, false));
		spriteDiggingUp.setRegion(animDiggingUp.getKeyFrame(elapsedTime * animPlaySpeed2, false));
		
		if (facingLeft) {
			spriteIdleLeft.setFlip(false, false);
			spriteWalkingLeft.setFlip(false, false);
			spriteDiggingLeft.setFlip(false, false);
			spriteDiggingDown.setFlip(false, false);
			spriteDiggingUp.setFlip(false, false);
		} else {
			spriteIdleLeft.setFlip(true, false);
			spriteWalkingLeft.setFlip(true, false);
			spriteDiggingLeft.setFlip(true, false);
			spriteDiggingDown.setFlip(true, false);
			spriteDiggingUp.setFlip(true, false);
		}
		
		spriteIdleLeft.setPosition(body.getTransform().getPosition().x - spriteIdleLeft.getRegionWidth() / 2, body.getTransform().getPosition().y + 0.99f - spriteIdleLeft.getRegionHeight() / 2);
		spriteWalkingLeft.setPosition(body.getTransform().getPosition().x - spriteWalkingLeft.getRegionWidth() / 2, body.getTransform().getPosition().y + 0.99f - spriteWalkingLeft.getRegionHeight() / 2);
		spriteDiggingLeft.setPosition(body.getTransform().getPosition().x - spriteDiggingLeft.getRegionWidth() / 2, body.getTransform().getPosition().y + 0.99f - spriteDiggingLeft.getRegionHeight() / 2);
		spriteDiggingDown.setPosition(body.getTransform().getPosition().x - spriteDiggingDown.getRegionWidth() / 2, body.getTransform().getPosition().y - 1 + 0.99f - spriteDiggingDown.getRegionHeight() / 2);
		spriteDiggingUp.setPosition(body.getTransform().getPosition().x - spriteDiggingUp.getRegionWidth() / 2, body.getTransform().getPosition().y + 2 + 0.99f - spriteDiggingUp.getRegionHeight() / 2);
		
		if (digging && animDiggingLeft.isAnimationFinished(elapsedTime * animPlaySpeed2)) {
			digging = false;
		} else if (digging && animDiggingDown.isAnimationFinished(elapsedTime * animPlaySpeed2)) {
			digging = false;
		} else if (digging && animDiggingUp.isAnimationFinished(elapsedTime * animPlaySpeed2)) {
			digging = false;
		}
		
		cameraPos = body.getPosition();
	}
	
	@Override
	public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
		if (!super.getScreen().b2dWorld.isLocked()) {
			if (fixture.getFilterData().categoryBits == super.getScreen().BLOCK_COL) {
				Object tempObj = fixture.getBody().getUserData();
				
				((DirtBlock) tempObj).hp = ((DirtBlock) tempObj).hp - 1;
				((DirtBlock) tempObj).damageBlock();
				
				if (((DirtBlock) tempObj).hp <= 0) {
					super.getScreen().b2dWorld.destroyBody(fixture.getBody());
					super.getScreen().entities.removeValue((Entity) tempObj, true);
					
					for (Entity entity : super.getScreen().entities) {
						((DirtBlock) entity).checkForNeighbours();
						((DirtBlock) entity).updateSprite(AssetsManager.MANAGER.get(AssetsManager.ATLAS01, Texture.class));
					}
				}
			}
//			System.out.println(super.getScreen().entities.size);
		}
		return 0;
	}
	
	@Override
	public void render(float delta) {
//		
		if (movingLR) {
			spriteWalkingLeft.draw(super.getScreen().game.batch);
		} else if (digging){
			if (facingDown) {
				spriteDiggingDown.draw(super.getScreen().game.batch);
			} else if (facingUp) {
				spriteDiggingUp.draw(super.getScreen().game.batch);
			} else {
				spriteDiggingLeft.draw(super.getScreen().game.batch);
			}
		} else {
			spriteIdleLeft.draw(super.getScreen().game.batch);
		}
		
		
	}

	@Override
	public void dispose() {
//		So far nothing to dispose...
	}
}
