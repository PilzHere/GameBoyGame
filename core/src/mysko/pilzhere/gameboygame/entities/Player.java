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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import mysko.pilzhere.gameboygame.assets.AssetsManager;
import mysko.pilzhere.gameboygame.entities.blocks.Block;
import mysko.pilzhere.gameboygame.entities.blocks.DirtBlock;
import mysko.pilzhere.gameboygame.entities.resources.Coal;
import mysko.pilzhere.gameboygame.entities.resources.Iron;
import mysko.pilzhere.gameboygame.entities.resources.Mushroom;
import mysko.pilzhere.gameboygame.entities.resources.Wood;
import mysko.pilzhere.gameboygame.entities.tools.Ladder;
import mysko.pilzhere.gameboygame.physics.CollisionContactListener;
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
	
	private TextureRegion[] texRegsClimbing;
	private Animation<TextureRegion> animClimbing;
	private Sprite spriteClimbing;
	
	private Body body;
	
	public Vector2 cameraPos = new Vector2();
	
	private boolean facingLeft = true;
	private boolean facingDown = false;
	private boolean facingUp = false;
	
	private boolean groundedLeft = false;
	private boolean groundedRight = false;
	private boolean grounded = false;
	
//	Stats
	public int hp = 10;
	public boolean invincible = false;
	
	public byte xWoods = 0;
	public byte xCoal = 0;
	public byte xIron = 0;
	
//	Actions
	private boolean movingLR = false;
	private boolean digging = false;
	private boolean canClimb = false;
	
//	Not sure if to keep these features...
	private boolean extendedDigFeature = false;
	
	private Vector2 digRayEnd = new Vector2();
//	private boolean facingUp; 
	
	public Player(GameScreen screen, float spawnPosX, float spawnPosY) {
		super.setScreen(screen);
		
		texRegIdleLeft = new TextureRegion(AssetsManager.MANAGER.get(AssetsManager.PLAYER, Texture.class), 11, 1, 8, 12);
		spriteIdleLeft = new Sprite(texRegIdleLeft);
		
		setupSpriteWalkingLeft(AssetsManager.MANAGER.get(AssetsManager.PLAYER, Texture.class));
		setupSpriteDiggingLeft(AssetsManager.MANAGER.get(AssetsManager.PLAYER, Texture.class));
		setupSpriteDiggingDown(AssetsManager.MANAGER.get(AssetsManager.PLAYER, Texture.class));
		setupSpriteDiggingUp(AssetsManager.MANAGER.get(AssetsManager.PLAYER, Texture.class));
		setupSpriteClimbing(AssetsManager.MANAGER.get(AssetsManager.PLAYER, Texture.class));
		
//		Body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.allowSleep = false;
		bodyDef.fixedRotation = true;
		bodyDef.position.set(spawnPosX, spawnPosY); // spawnPos

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(7.25f * 8 / GameScreen.PPM, 10 * 8 / GameScreen.PPM);

		FixtureDef fixtDef = new FixtureDef();
		fixtDef.filter.categoryBits = CollisionContactListener.PLAYER_COL;
		fixtDef.filter.maskBits = CollisionContactListener.PLAYER_COLLIDES_WITH;
		fixtDef.density = 1.0f;
		fixtDef.friction = 0.0f;
		fixtDef.restitution = 0.0f; // Bounciness
		fixtDef.shape = shape;

		body = screen.b2dWorld.createBody(bodyDef);

		body.createFixture(fixtDef);
		body.setUserData(this);
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
	
	private void setupSpriteClimbing(Texture tex) {
		TextureRegion texRegClimbing1 = new TextureRegion(tex, 1, 14, 9, 12);
		TextureRegion texRegClimbing2 = new TextureRegion(tex, 11, 14, 9, 12);
		
		texRegsClimbing = new TextureRegion[2];
		texRegsClimbing[0] = texRegClimbing1;
		texRegsClimbing[1] = texRegClimbing2;
		
		int index = 0;
		for (int i = 0; i < 2; i++) {
			texRegsClimbing[index++] = texRegsClimbing[i];
		}

		float frameDur = 1f / 4f;
		animClimbing = new Animation<TextureRegion>(frameDur, texRegsClimbing);
		
		spriteClimbing = new Sprite(animClimbing.getKeyFrame(0));
	}
	
	public void hurt(int damage) {
		if (!invincible) {
			if (hp <= 0) {
				 hp = 0;
			 } else {
				 hp = hp - damage;
				 invincible = true;
			 }
		}
	}
	
	private Vector2 ladderPos = new Vector2();
	
	private boolean UPKeyHasBeenPressed = false;
	
	private Vector2 rayDown = new Vector2(0, -10);
	private Vector2 rayUp = new Vector2(0, 16);
	private Vector2 rayLeft = new Vector2(-10, 0);
	private Vector2 rayRight = new Vector2(10, 0);
	
	private final int extraWidthDig = 18;
	private void handleInput(float delta) {
		UPKeyHasBeenPressed = false;
		
//		TEST
//		if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
//			 
//		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
			hp = MathUtils.random(10);
			if (hp == 0) {
				hp = 1;
			}
		}
//		TEST END
		
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			if (!digging) {
				facingLeft = false;
				body.setLinearVelocity(new Vector2(60, body.getLinearVelocity().y));
				movingLR = true;	
			} else {
				facingLeft = false;
				body.setLinearVelocity(new Vector2(60, body.getLinearVelocity().y));
			}
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			if (!digging) {
				facingLeft = true;
				body.setLinearVelocity(new Vector2(-60, body.getLinearVelocity().y));
				movingLR = true;	
			} else {
				facingLeft = true;
				body.setLinearVelocity(new Vector2(-60, body.getLinearVelocity().y));
			}
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			UPKeyHasBeenPressed = true;
			if (!facingDown) {
				facingUp = true;
			}
			
			if (canClimb) {
				body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 50));
			}
		} else {
			facingUp = false;
		}
		
		if (!UPKeyHasBeenPressed) {
			if (ladders.size != 0) {
				body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, -3));
			}
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			if (!facingUp) {
				facingDown = true;
			}
			
			if (canClimb) {
				body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, -50));
			}
		} else {
			facingDown = false;
		}
		
		if (!Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			body.setLinearVelocity(new Vector2(0, body.getLinearVelocity().y));
			movingLR = false;
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			if (grounded /*&& !digging*/ && (tempFractLeft != 0 || tempFractRight != 0)) {
				body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 85));
				groundedLeft = false;
				groundedRight = false;
			}
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
			movingLR = false;
			if (!digging && grounded) {
				digging = true;
				
				elapsedTime = 0;
				
				if (!facingLeft) {
					if (facingDown) {
						digRayEnd.set(new Vector2(body.getTransform().getPosition().add(rayDown)));
					} else if (facingUp){
						digRayEnd.set(new Vector2(body.getTransform().getPosition().add(rayUp)));
					} else {
						if (extendedDigFeature) {
							if (groundedLeft && tempFractRight == 0) {
								digRayEnd.set(new Vector2(body.getTransform().getPosition().add(new Vector2(extraWidthDig, 0))));
							} else {
								digRayEnd.set(new Vector2(body.getTransform().getPosition().add(rayRight)));
							}
						} else {
							digRayEnd.set(new Vector2(body.getTransform().getPosition().add(rayRight)));
						}
						
					}
				} else {
					if (facingDown) {
						digRayEnd.set(new Vector2(body.getTransform().getPosition().add(rayDown)));
					} else if (facingUp) {
						digRayEnd.set(new Vector2(body.getTransform().getPosition().add(rayUp)));
					} else {
						if (extendedDigFeature) {
							if (groundedRight && tempFractLeft == 0) {
								digRayEnd.set(new Vector2(body.getTransform().getPosition().add(new Vector2(-extraWidthDig, 0))));
							} else {
								digRayEnd.set(new Vector2(body.getTransform().getPosition().add(rayLeft)));	
							}
						} else {
							digRayEnd.set(new Vector2(body.getTransform().getPosition().add(rayLeft)));
						}
					}
				}
				rayDig = true;
				super.getScreen().b2dWorld.rayCast(this, body.getTransform().getPosition(), digRayEnd);
				rayDig = false;
			}
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
			if (tempFractLeft != 0 && tempFractRight != 0) {
				Vector2 tempDown = new Vector2(body.getTransform().getPosition().add(new Vector2(0, -6)));
				rayPlaceTool = true;
				super.getScreen().b2dWorld.rayCast(this, body.getTransform().getPosition(), tempDown);
				rayPlaceTool = false;
				
				if (!canClimb) {
					super.getScreen().placedTools.add(new Ladder(super.getScreen(), ladderPos.x, ladderPos.y));
					System.out.println("Placed ladder!");
				}
			}
			
		}
	}
	
	private Array<Ladder> ladders = new Array<Ladder>();
	public void onCollisionBegin(Fixture fixture) {
		if (fixture.getFilterData().categoryBits == CollisionContactListener.LADDER_COL) {
			ladders.add((Ladder) fixture.getBody().getUserData());
			canClimb = true;
		} else if (fixture.getFilterData().categoryBits == CollisionContactListener.RESOURCE_COL) {
			if (fixture.getBody().getUserData() instanceof Wood) {
				if (xWoods >= 0 && xWoods < 127) {
					xWoods = (byte) (xWoods + 1);
				}
				((Wood)fixture.getBody().getUserData()).remove = true;
			} else if (fixture.getBody().getUserData() instanceof Coal) {
				if (xCoal >= 0 && xCoal < 127) {
					xCoal = (byte) (xCoal + 1);
				}
				((Coal)fixture.getBody().getUserData()).remove = true;
			} else if (fixture.getBody().getUserData() instanceof Iron) {
				if (xIron >= 0 && xIron < 127) {
					xIron = (byte) (xIron + 1);
				}
				((Iron)fixture.getBody().getUserData()).remove = true;
			} else if (fixture.getBody().getUserData() instanceof Mushroom) {
				if (hp >= 0 && hp < 10) {
					hp++;
				}
				((Mushroom)fixture.getBody().getUserData()).remove = true;
			}
		}
	}
	
	public void onCollisionEnd(Fixture fixture) {
		if (fixture.getFilterData().categoryBits == CollisionContactListener.LADDER_COL) {
			ladders.removeValue((Ladder) fixture.getBody().getUserData(), true);
		}
	}
	
	public void onCollisionContinuos(Fixture fixture) {
		
	}
	
	private float elapsedTime = 0;
	private final float animPlaySpeed1 = 2.5f;
	private final float animPlaySpeed2 = 4.2f;
	private final float animPlaySpeed3 = 1.75f;
	
	private Vector2 groundedLeftRayStart = new Vector2();
	private Vector2 groundedLeftRayEnd = new Vector2();
	private Vector2 groundedRightRayStart = new Vector2();
	private Vector2 groundedRightRayEnd = new Vector2();
	
	private double invincibleTime = 1000;
	private double invincibleTimeEnd;
	private boolean invinsibleTimeSet = false;
	
	@Override
	public void tick(float delta) {
		elapsedTime += Gdx.graphics.getDeltaTime();
		tempFractLeft = 0;
		tempFractRight = 0;
		
		if (xWoods < 0) {
			xWoods = 0;
		}
		
		if (xCoal < 0) {
			xCoal = 0;
		}
		
		if (xIron < 0) {
			xIron = 0;
		}
		
		if (invincible) {
			if (!invinsibleTimeSet) {
				invincibleTimeEnd = System.currentTimeMillis() + invincibleTime;
				invinsibleTimeSet = true;
			} else {
				if (System.currentTimeMillis() >= invincibleTimeEnd) {
					invinsibleTimeSet = false;
					invincible = false;
				}
			}
		} else {
			invinsibleTimeSet = false;
		}
		
		groundedLeftRayStart.set(body.getTransform().getPosition().add(new Vector2(-3.625f, -5f)));
		groundedLeftRayEnd.set(body.getTransform().getPosition().add(new Vector2(-3.625f, -5.1f)));
		
		groundedRightRayStart.set(body.getTransform().getPosition().add(new Vector2(3.625f, -5f)));
		groundedRightRayEnd.set(body.getTransform().getPosition().add(new Vector2(3.625f, -5.1f)));
		
		rayGroundedLeft = true;
		super.getScreen().b2dWorld.rayCast(this, groundedLeftRayStart, groundedLeftRayEnd);
		rayGroundedLeft = false;
		rayGroundedRight = true;
		super.getScreen().b2dWorld.rayCast(this, groundedRightRayStart, groundedRightRayEnd);
		rayGroundedRight = false;
		
//		System.err.println("L = " + groundedLeft + "    R = " + groundedRight + "    Grounded = " + grounded);
		
//		System.out.println("L: " +  tempFractLeft);
//		System.out.println("R: " +  tempFractRight);
		
		if (groundedLeft || groundedRight) {
			grounded = true;
		} else {
			grounded = false;
		}
		
		if (ladders.size == 0) {
			canClimb = false;
		}
		
		handleInput(delta);
		
//		if (digging) {
//			body.setLinearVelocity(new Vector2(0, body.getLinearVelocity().y));
//		}
		
		spriteWalkingLeft.setRegion(animWalkingLeft.getKeyFrame(elapsedTime * animPlaySpeed1, true));
		spriteDiggingLeft.setRegion(animDiggingLeft.getKeyFrame(elapsedTime * animPlaySpeed2, false));
		spriteDiggingDown.setRegion(animDiggingDown.getKeyFrame(elapsedTime * animPlaySpeed2, false));
		spriteDiggingUp.setRegion(animDiggingUp.getKeyFrame(elapsedTime * animPlaySpeed2, false));
		spriteClimbing.setRegion(animClimbing.getKeyFrame(elapsedTime * (animPlaySpeed3), true));
		
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
		spriteClimbing.setPosition(body.getTransform().getPosition().x - spriteClimbing.getRegionWidth() / 2, body.getTransform().getPosition().y + 0.99f - spriteClimbing.getRegionHeight() / 2);
		
		if (digging && animDiggingLeft.isAnimationFinished(elapsedTime * animPlaySpeed2)) {
			digging = false;
		} else if (digging && animDiggingDown.isAnimationFinished(elapsedTime * animPlaySpeed2)) {
			digging = false;
		} else if (digging && animDiggingUp.isAnimationFinished(elapsedTime * animPlaySpeed2)) {
			digging = false;
		}
		
		cameraPos = body.getPosition();
	}
	
	private boolean rayDig = false;
	private boolean rayPlaceTool = false;
	private boolean rayGroundedLeft = false;
	private boolean rayGroundedRight = false;
	private float tempFractLeft; // used to detect if in air. 0 = in air.
	private float tempFractRight; // used to detect if in air. 0 = in air.
	@Override
	public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
		if (!super.getScreen().b2dWorld.isLocked()) {
			if (rayDig) {
				if (fixture.getFilterData().categoryBits == CollisionContactListener.BLOCK_COL) {
					Object tempObj = fixture.getBody().getUserData();
					
					((Block) tempObj).currentHp = ((Block) tempObj).currentHp - 1;
					((Block) tempObj).damageBlock();
					
					if (((Block) tempObj).currentHp <= 0) {
						super.getScreen().b2dWorld.destroyBody(fixture.getBody());
						if (((Block) tempObj).containsResource) {
							((Block) tempObj).spawnResource();
						}
						super.getScreen().blocks.removeValue((Entity) tempObj, true);
						
						for (Entity entity : super.getScreen().blocks) {
							((Block) entity).checkForNeighbours();
							((Block) entity).updateSprite(AssetsManager.MANAGER.get(AssetsManager.ATLAS01, Texture.class));
						}
					}
				}
//				System.out.println(super.getScreen().entities.size);
			} else if (rayGroundedLeft) {
				tempFractLeft = fraction; // if 0 = in air
				if (fixture.getFilterData().categoryBits == CollisionContactListener.TERRAIN_COL || fixture.getFilterData().categoryBits == CollisionContactListener.BLOCK_COL) {
					groundedLeft = true;
				} else {
					groundedLeft = false;
				}
			} else if (rayGroundedRight) {
				tempFractRight = fraction; // if 0 = in air
				if (fixture.getFilterData().categoryBits == CollisionContactListener.TERRAIN_COL || fixture.getFilterData().categoryBits == CollisionContactListener.BLOCK_COL) {
					groundedRight = true;
				} else {
					groundedRight = false;
				}
			} else if (rayPlaceTool) { // LADDER
				if (fixture.getFilterData().categoryBits == CollisionContactListener.TERRAIN_COL || fixture.getFilterData().categoryBits == CollisionContactListener.BLOCK_COL) {
					ladderPos.set(new Vector2(fixture.getBody().getPosition().x - 10, fixture.getBody().getPosition().y + 10));
//					super.getScreen().tools.add(new Ladder(super.getScreen(), fixture.getBody().getPosition().x - 10, fixture.getBody().getPosition().y + 10));
				}
			}
		}
		return 0;
	}
	
	private long newFlickerTime;
	private long flickerTime = 60*2L;
	private boolean newFlickerTimeSet = false;
	@Override
	public void render(float delta) {
		super.bodyCenter.set(new Vector3(body.getPosition().x, body.getPosition().y, 0));
		super.bodyDimensions.set(new Vector3(7.25f + super.bBExtra / GameScreen.PPM, 10 + super.bBExtra / GameScreen.PPM, 0));
		
		if (super.getScreen().isInFrustum(super.bodyCenter, super.bodyDimensions)) {
			super.render = true;
		} else {
			super.render = false;
		}
		
		if (invincible) {
			if (!newFlickerTimeSet) {
				newFlickerTime = System.currentTimeMillis() + flickerTime;
				newFlickerTimeSet = true;
			} else {
				if (System.currentTimeMillis() < newFlickerTime) {
					super.render = false;
				} else {
					super.render = true;
					newFlickerTimeSet = false;
				}
			}
		}
		
		if (super.render) {
			if (movingLR) {
				if (canClimb) {
					spriteClimbing.draw(super.getScreen().game.batch);
				} else {
					spriteWalkingLeft.draw(super.getScreen().game.batch);
				}
			} else if (!movingLR && digging){
				if (facingDown) {
					spriteDiggingDown.draw(super.getScreen().game.batch);
				} else if (facingUp) {
					spriteDiggingUp.draw(super.getScreen().game.batch);
				} else {
					spriteDiggingLeft.draw(super.getScreen().game.batch);
				}
			} else if (canClimb) {
				spriteClimbing.draw(super.getScreen().game.batch);
			} else {
				spriteIdleLeft.draw(super.getScreen().game.batch);
			}
		}
		
//		if (currentFlickerTime >= flickerTimeSwitch) {
//			currentFlickerTime = 0;
//		}
	}
	
	@Override
	public void drawRays() {
		super.getScreen().drawRays(groundedLeftRayStart, groundedLeftRayEnd);
		super.getScreen().drawRays(groundedRightRayStart, groundedRightRayEnd);
		
		super.getScreen().drawRays(body.getTransform().getPosition(), digRayEnd);
	}

	@Override
	public void dispose() {
//		So far nothing to dispose...
	}
}
