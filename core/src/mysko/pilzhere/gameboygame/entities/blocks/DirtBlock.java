/**
 * 
 */
package mysko.pilzhere.gameboygame.entities.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.utils.Disposable;

import mysko.pilzhere.gameboygame.assets.AssetsManager;
import mysko.pilzhere.gameboygame.entities.Entity;
import mysko.pilzhere.gameboygame.screens.GameScreen;

/**
 * @author PilzHere
 *
 */
public class DirtBlock extends Entity implements Disposable, RayCastCallback{

	private Body body;
	
	public int hp = 3;
	
	private int texNr;
	
	public boolean nU = false;
	public boolean nR = false;
	public boolean nD = false;
	public boolean nL = false;
	
	private TextureRegion texReg;
	private TextureRegion texRegU;
	private TextureRegion texRegD;
	private TextureRegion texRegL;
	private TextureRegion texRegR;
	
	private TextureRegion tempTexRegBreak;
	
	private Sprite sprite;
	private Sprite spriteU;
	private Sprite spriteD;
	private Sprite spriteL;
	private Sprite spriteR;
	
	private Sprite spriteBreak;
	
//	private int spriteBreakRot;
	
	public DirtBlock(GameScreen screen, float x, float y) {
		super.setScreen(screen);
		super.id = screen.entities.size;
		
		texNr = MathUtils.random(3);
//		spriteBreakRot = MathUtils.random(3);
		
//		System.out.println(super.id);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.allowSleep = true;
		bodyDef.fixedRotation = true;
		bodyDef.position.set(x + 10, y + 6); // spawnPos

//		PolygonShape shape = new PolygonShape(); OLD
//		shape.setAsBox(16 * 8 / GameScreen.PPM, 16 * 8 / GameScreen.PPM);
		
		ChainShape shape = new ChainShape();
		float[] vertices = new float[] {-8, 8,
										8, 8,
										8, -8,
										-8, -8,
										-8, 8}; // this way player box body does not randomly get stuck.
		shape.createChain(vertices);

		FixtureDef fixtDef = new FixtureDef();
		fixtDef.filter.categoryBits = screen.BLOCK_COL;
		fixtDef.filter.maskBits = screen.BLOCK_COLLIDES_WITH;
		fixtDef.density = 1.0f;
		fixtDef.friction = 0.0f;
		fixtDef.restitution = 0.0f; // Bounciness
		fixtDef.shape = shape;
		
		body = screen.b2dWorld.createBody(bodyDef);

		body.createFixture(fixtDef);
		
		body.setUserData(this);

		shape.dispose();
		
//		checkForNeighbours();
	}
	
	Vector2 rayEndUp = new Vector2();
	Vector2 rayEndRight = new Vector2();
	Vector2 rayEndDown = new Vector2();
	Vector2 rayEndLeft = new Vector2();
	boolean hit = false;
	public void checkForNeighbours() {	
		rayEndUp.set(body.getTransform().getPosition().add(new Vector2(0, 16)));
		rayEndRight.set(body.getTransform().getPosition().add(new Vector2(16, 0)));
		rayEndDown.set(body.getTransform().getPosition().add(new Vector2(0, -16)));
		rayEndLeft.set(body.getTransform().getPosition().add(new Vector2(-16, 0)));
		
		super.getScreen().b2dWorld.rayCast(this, body.getTransform().getPosition(), rayEndUp);
		if (hit) {
//			System.out.println("I has neighbour UP!");
			nU = true;
		} else {
			nU = false;
		}
		hit = false;
		
		super.getScreen().b2dWorld.rayCast(this, body.getTransform().getPosition(), rayEndRight);
		if (hit) {
//			System.out.println("I has neighbour RIGHT!");
			nR = true;
		} else {
			nR = false;
		}
		hit = false;
		
		super.getScreen().b2dWorld.rayCast(this, body.getTransform().getPosition(), rayEndDown);
		if (hit) {
//			System.out.println("I has neighbour DOWN!");
			nD = true;
		} else {
			nD = false;
		}
		hit = false;
		
		super.getScreen().b2dWorld.rayCast(this, body.getTransform().getPosition(), rayEndLeft);
		if (hit) {
//			System.out.println("I has neighbour LEFT!");
			nL = true;
		} else {
			nL = false;
		}
		hit = false;
		
//		updateSprite(AssetsManager.MANAGER.get(AssetsManager.ATLAS01, Texture.class));
	}
	
	public void damageBlock() {
		if (hp < 3) {
			if (spriteBreak == null) {
				spriteBreak = new Sprite();
			}
				
			if (hp == 2) {
				tempTexRegBreak = null;
				tempTexRegBreak = new TextureRegion(AssetsManager.MANAGER.get(AssetsManager.ATLAS01, Texture.class), 2, 38, 16, 16);
				spriteBreak.setRegion(tempTexRegBreak);
				spriteBreak.setSize(spriteBreak.getRegionWidth(), spriteBreak.getRegionHeight());
			} else if (hp == 1) {
				tempTexRegBreak = null;
				tempTexRegBreak = new TextureRegion(AssetsManager.MANAGER.get(AssetsManager.ATLAS01, Texture.class), 2, 56, 16, 16);
				spriteBreak.setRegion(tempTexRegBreak);
				spriteBreak.setSize(spriteBreak.getRegionWidth(), spriteBreak.getRegionHeight());
			}
		}
	}
	
//	private float rotateSpriteBreak(int r) {
//		float rot = 0;
//		if (r == 1) {
//			rot = 90;
//		} else if (r == 2) {
//			rot = 180;
//		} else if (r == 3) {
//			rot = 270;
//		}
//		return rot;
//	}
	
	public void updateSprite(Texture tex) {		
		if (texNr == 0) {
			texReg = new TextureRegion(tex, 20, 56, 16, 16);
		} else if (texNr == 1) {
			texReg = new TextureRegion(tex, 56, 56, 16, 16);
		} else if (texNr == 2) {
			texReg = new TextureRegion(tex, 38, 74, 16, 16);
		} else if (texNr == 3) {
			texReg = new TextureRegion(tex, 56, 74, 16, 16);
		}
		
		texRegU = new TextureRegion(tex, 38, 20, 16, 16);
		texRegD = new TextureRegion(tex, 56, 20, 16, 16);
		texRegL = new TextureRegion(tex, 38, 36, 16, 16);
		texRegR = new TextureRegion(tex, 56, 36, 16, 16);
		
		sprite = new Sprite();
		sprite.setRegion(texReg);
		sprite.setSize(sprite.getRegionWidth(), sprite.getRegionHeight());
		
		if (!nU) {
			spriteU = new Sprite();
			spriteU.setRegion(texRegU);
			spriteU.setSize(spriteU.getRegionWidth(), spriteU.getRegionHeight());
		} else {
			if (spriteU != null) {
				spriteU = null;
			}
		}
		
		if (!nD) {
			spriteD = new Sprite();
			spriteD.setRegion(texRegD);
			spriteD.setSize(spriteD.getRegionWidth(), spriteD.getRegionHeight());
		} else {
			if (spriteD != null) {
				spriteD = null;
			}
		}
		
		if (!nL) {
			spriteL = new Sprite();
			spriteL.setRegion(texRegL);
			spriteL.setSize(spriteL.getRegionWidth(), spriteL.getRegionHeight());
		} else {
			if (spriteL != null) {
				spriteL = null;
			}
		}
		
		if (!nR) {
			spriteR = new Sprite();
			spriteR.setRegion(texRegR);
			spriteR.setSize(spriteR.getRegionWidth(), spriteR.getRegionHeight());
		} else {
			if (spriteR != null) {
				spriteR = null;
			}
		}
	}
	
	@Override
	public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
		if (fixture.getFilterData().categoryBits == super.getScreen().BLOCK_COL && fixture.getBody() != this.body) {
			hit = true;
		}
		return -1;
	}
	
	@Override
	public void tick(float delta) {		
		sprite.setPosition(body.getTransform().getPosition().x - sprite.getRegionWidth() / 2, body.getTransform().getPosition().y - sprite.getRegionHeight() / 2);
		
		if (spriteU != null)
			spriteU.setPosition(body.getTransform().getPosition().x - spriteU.getRegionWidth() / 2, body.getTransform().getPosition().y - spriteU.getRegionHeight() / 2);
		if (spriteD != null)
			spriteD.setPosition(body.getTransform().getPosition().x - spriteD.getRegionWidth() / 2, body.getTransform().getPosition().y - spriteD.getRegionHeight() / 2);
		if (spriteL != null)
			spriteL.setPosition(body.getTransform().getPosition().x - spriteL.getRegionWidth() / 2, body.getTransform().getPosition().y - spriteL.getRegionHeight() / 2);
		if (spriteR != null)
			spriteR.setPosition(body.getTransform().getPosition().x - spriteR.getRegionWidth() / 2, body.getTransform().getPosition().y - spriteR.getRegionHeight() / 2);
		
		if (spriteBreak != null)
			spriteBreak.setPosition(body.getTransform().getPosition().x - spriteBreak.getRegionWidth() / 2, body.getTransform().getPosition().y - spriteBreak.getRegionHeight() / 2);
	}
	
	private Vector3 bodyCenter = new Vector3();
	private Vector3 bodyDimensions = new Vector3();
	private final int bBExtra = 1;
	
	@Override
	public void render(float delta) {
		bodyCenter.set(new Vector3(body.getPosition().x, body.getPosition().y, 0));
		bodyDimensions.set(new Vector3(16 + bBExtra / super.getScreen().PPM, 16 + bBExtra / super.getScreen().PPM, 0));
		
		if (super.getScreen().isInFrustum(bodyCenter, bodyDimensions)) {
			sprite.draw(super.getScreen().game.batch);
			
			if (spriteU != null)
				spriteU.draw(super.getScreen().game.batch);
			if (spriteD != null)
				spriteD.draw(super.getScreen().game.batch);
			if (spriteL != null)
				spriteL.draw(super.getScreen().game.batch);
			if (spriteR != null)
				spriteR.draw(super.getScreen().game.batch);
			
			if (spriteBreak != null) {
				spriteBreak.draw(super.getScreen().game.batch);
			}
		}
//		else {
//			System.err.println("DirtBlock NOT rendering!");
//		}
	}

	@Override
	public void dispose() {
//		So far nothing to dispose...	
	}	
}
