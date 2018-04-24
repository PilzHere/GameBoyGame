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
import mysko.pilzhere.gameboygame.physics.CollisionContactListener;
import mysko.pilzhere.gameboygame.screens.GameScreen;

/**
 * @author PilzHere
 *
 */
public class Block extends Entity implements RayCastCallback, Disposable {

	protected int maxHp;
	public int currentHp;

	public boolean remove = false;

	public Body body;

	private int texNr;

	public boolean containsResource = false;

	protected boolean nU = false;
	protected boolean nR = false;
	protected boolean nD = false;
	protected boolean nL = false;

	protected TextureRegion texRegMain;
	private TextureRegion texRegEdgeU;
	private TextureRegion texRegEdgeD;
	private TextureRegion texRegEdgeL;
	private TextureRegion texRegEdgeR;

	private TextureRegion tempTexRegBreak;

	protected Sprite spriteMain;
	private Sprite spriteEdgeU;
	private Sprite spriteEdgeD;
	private Sprite spriteEdgeL;
	private Sprite spriteEdgeR;

	private Sprite spriteBreak = new Sprite();

	public Block(GameScreen screen, float spawnPosX, float spawnPosY) {
		super.setScreen(screen);

		texNr = MathUtils.random(3);
	}

	protected void createBody(float bodySpawnX, float bodySpawnY, BodyType bodyType, short categoryBits,
			short maskBits) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = bodyType;
		bodyDef.allowSleep = true;
		bodyDef.fixedRotation = true;
		bodyDef.position.set(bodySpawnX + 8, bodySpawnY + 8); // spawnPos

		ChainShape shape = new ChainShape();
		float[] vertices = new float[] { -8, 8, 8, 8, 8, -8, -8, -8, -8, 8 }; // We could create a polygonal box but
																				// this way player box body does not
																				// randomly get stuck bodyedges.
		shape.createChain(vertices);

		FixtureDef fixtDef = new FixtureDef();
		fixtDef.filter.categoryBits = categoryBits;
		fixtDef.filter.maskBits = maskBits;
		fixtDef.density = 1.0f;
		fixtDef.friction = 0.5f;
		fixtDef.restitution = 0.0f; // Bounciness
		fixtDef.shape = shape;

		body = super.getScreen().b2dWorld.createBody(bodyDef);
		body.createFixture(fixtDef);
		body.setUserData(this);

		shape.dispose();
	}

	private Vector2 rayEndUp = new Vector2();
	private Vector2 rayEndRight = new Vector2();
	private Vector2 rayEndDown = new Vector2();
	private Vector2 rayEndLeft = new Vector2();
	private boolean hit = false;

	public void checkForNeighbours() {
		rayEndUp.set(body.getTransform().getPosition().add(new Vector2(0, 16)));
		rayEndRight.set(body.getTransform().getPosition().add(new Vector2(16, 0)));
		rayEndDown.set(body.getTransform().getPosition().add(new Vector2(0, -16)));
		rayEndLeft.set(body.getTransform().getPosition().add(new Vector2(-16, 0)));

		super.getScreen().b2dWorld.rayCast(this, body.getTransform().getPosition(), rayEndUp);
		if (hit) {
			// System.out.println("I has neighbour UP!");
			nU = true;
		} else {
			nU = false;
		}
		hit = false;

		super.getScreen().b2dWorld.rayCast(this, body.getTransform().getPosition(), rayEndRight);
		if (hit) {
			// System.out.println("I has neighbour RIGHT!");
			nR = true;
		} else {
			nR = false;
		}
		hit = false;

		super.getScreen().b2dWorld.rayCast(this, body.getTransform().getPosition(), rayEndDown);
		if (hit) {
			// System.out.println("I has neighbour DOWN!");
			nD = true;
		} else {
			nD = false;
		}
		hit = false;

		super.getScreen().b2dWorld.rayCast(this, body.getTransform().getPosition(), rayEndLeft);
		if (hit) {
			// System.out.println("I has neighbour LEFT!");
			nL = true;
		} else {
			nL = false;
		}
		hit = false;
	}

	public void damageBlock() {
		if (currentHp != maxHp) {
			if (spriteBreak == null) {
				spriteBreak = new Sprite();
			}

			if ((float) currentHp / maxHp >= 0.6f) {
				tempTexRegBreak = null;
				tempTexRegBreak = new TextureRegion(AssetsManager.MANAGER.get(AssetsManager.ATLAS01, Texture.class), 2,
						38, 16, 16);
				spriteBreak.setRegion(tempTexRegBreak);
				spriteBreak.setSize(spriteBreak.getRegionWidth(), spriteBreak.getRegionHeight());
			} else if ((float) currentHp / maxHp >= 0.3f) {
				tempTexRegBreak = null;
				tempTexRegBreak = new TextureRegion(AssetsManager.MANAGER.get(AssetsManager.ATLAS01, Texture.class), 2,
						56, 16, 16);
				spriteBreak.setRegion(tempTexRegBreak);
				spriteBreak.setSize(spriteBreak.getRegionWidth(), spriteBreak.getRegionHeight());
			}
		}
	}

	public void updateSprite(Texture tex) {
		setProperRandomTexture(tex, texNr);

		texRegEdgeU = new TextureRegion(tex, 38, 20, 16, 16);
		texRegEdgeD = new TextureRegion(tex, 56, 20, 16, 16);
		texRegEdgeL = new TextureRegion(tex, 38, 36, 16, 16);
		texRegEdgeR = new TextureRegion(tex, 56, 36, 16, 16);

		spriteMain = new Sprite();
		spriteMain.setRegion(texRegMain);
		spriteMain.setSize(spriteMain.getRegionWidth(), spriteMain.getRegionHeight());

		if (!nU) {
			spriteEdgeU = new Sprite();
			spriteEdgeU.setRegion(texRegEdgeU);
			spriteEdgeU.setSize(spriteEdgeU.getRegionWidth(), spriteEdgeU.getRegionHeight());
		} else {
			if (spriteEdgeU != null) {
				spriteEdgeU = null;
			}
		}

		if (!nD) {
			spriteEdgeD = new Sprite();
			spriteEdgeD.setRegion(texRegEdgeD);
			spriteEdgeD.setSize(spriteEdgeD.getRegionWidth(), spriteEdgeD.getRegionHeight());
		} else {
			if (spriteEdgeD != null) {
				spriteEdgeD = null;
			}
		}

		if (!nL) {
			spriteEdgeL = new Sprite();
			spriteEdgeL.setRegion(texRegEdgeL);
			spriteEdgeL.setSize(spriteEdgeL.getRegionWidth(), spriteEdgeL.getRegionHeight());
		} else {
			if (spriteEdgeL != null) {
				spriteEdgeL = null;
			}
		}

		if (!nR) {
			spriteEdgeR = new Sprite();
			spriteEdgeR.setRegion(texRegEdgeR);
			spriteEdgeR.setSize(spriteEdgeR.getRegionWidth(), spriteEdgeR.getRegionHeight());
		} else {
			if (spriteEdgeR != null) {
				spriteEdgeR = null;
			}
		}
	}

	private void setProperRandomTexture(Texture tex, int texNr) {
		if (texNr == 0) {
			texRegMain = new TextureRegion(tex, 20, 56, 16, 16);
		} else if (texNr == 1) {
			texRegMain = new TextureRegion(tex, 56, 56, 16, 16);
		} else if (texNr == 2) {
			texRegMain = new TextureRegion(tex, 38, 74, 16, 16);
		} else if (texNr == 3) {
			texRegMain = new TextureRegion(tex, 56, 74, 16, 16);
		}
	}

	@Override
	public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
		if (fixture.getFilterData().categoryBits == CollisionContactListener.BLOCK_COL
				&& fixture.getBody() != this.body) {
			hit = true;
		} else if (fixture.getFilterData().categoryBits == CollisionContactListener.TERRAIN_COL
				&& fixture.getBody() != this.body) {
			hit = true;
		}
		return -1;
	}

	public void remove() {
		getScreen().blocks.removeValue(this, true);
	}

	@Override
	public void tick(float delta) {
		spriteMain.setPosition(body.getTransform().getPosition().x - spriteMain.getRegionWidth() / 2,
				body.getTransform().getPosition().y - spriteMain.getRegionHeight() / 2);

		if (spriteEdgeU != null)
			spriteEdgeU.setPosition(body.getTransform().getPosition().x - spriteEdgeU.getRegionWidth() / 2,
					body.getTransform().getPosition().y - spriteEdgeU.getRegionHeight() / 2);
		if (spriteEdgeD != null)
			spriteEdgeD.setPosition(body.getTransform().getPosition().x - spriteEdgeD.getRegionWidth() / 2,
					body.getTransform().getPosition().y - spriteEdgeD.getRegionHeight() / 2);
		if (spriteEdgeL != null)
			spriteEdgeL.setPosition(body.getTransform().getPosition().x - spriteEdgeL.getRegionWidth() / 2,
					body.getTransform().getPosition().y - spriteEdgeL.getRegionHeight() / 2);
		if (spriteEdgeR != null)
			spriteEdgeR.setPosition(body.getTransform().getPosition().x - spriteEdgeR.getRegionWidth() / 2,
					body.getTransform().getPosition().y - spriteEdgeR.getRegionHeight() / 2);

		if (spriteBreak != null)
			spriteBreak.setPosition(body.getTransform().getPosition().x - spriteBreak.getRegionWidth() / 2,
					body.getTransform().getPosition().y - spriteBreak.getRegionHeight() / 2);
	}

	@Override
	public void render(float delta) {
		super.bodyCenter.set(new Vector3(body.getPosition().x, body.getPosition().y, 0));
		super.bodyDimensions
				.set(new Vector3(16 + super.bBExtra / GameScreen.PPM, 16 + super.bBExtra / GameScreen.PPM, 0));

		if (super.getScreen().isInFrustum(super.bodyCenter, super.bodyDimensions)) {
			super.render = true;
		} else {
			super.render = false;
		}

		if (super.render) {
			spriteMain.draw(super.getScreen().game.batch);

			if (spriteEdgeU != null)
				spriteEdgeU.draw(super.getScreen().game.batch);
			if (spriteEdgeD != null)
				spriteEdgeD.draw(super.getScreen().game.batch);
			if (spriteEdgeL != null)
				spriteEdgeL.draw(super.getScreen().game.batch);
			if (spriteEdgeR != null)
				spriteEdgeR.draw(super.getScreen().game.batch);

			if (spriteBreak != null) {
				if (spriteBreak.getTexture() != null) {
					spriteBreak.draw(super.getScreen().game.batch);
				}
			}
		}
	}

	public void spawnResource() {
		// Only here for slave-class.
	}

	@Override
	public void dispose() {
		// So far nothing to dispose...
	}

}
