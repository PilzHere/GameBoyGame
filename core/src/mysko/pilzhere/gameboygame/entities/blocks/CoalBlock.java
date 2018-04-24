/**
 * 
 */
package mysko.pilzhere.gameboygame.entities.blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Disposable;

import mysko.pilzhere.gameboygame.assets.AssetsManager;
import mysko.pilzhere.gameboygame.entities.resources.Coal;
import mysko.pilzhere.gameboygame.physics.CollisionContactListener;
import mysko.pilzhere.gameboygame.screens.GameScreen;

/**
 * @author PilzHere
 *
 */
public class CoalBlock extends Block implements Disposable {

	private TextureRegion texRegRes;
	private Sprite spriteRes;

	private byte resourceAmount;

	/**
	 * @param screen
	 * @param spawnPosX
	 * @param spawnPosY
	 */
	public CoalBlock(GameScreen screen, float spawnPosX, float spawnPosY) {
		super(screen, spawnPosX, spawnPosY);

		super.createBody(spawnPosX, spawnPosY, BodyType.StaticBody, CollisionContactListener.BLOCK_COL,
				CollisionContactListener.BLOCK_COLLIDES_WITH);

		super.maxHp = 5;
		super.currentHp = super.maxHp;

		super.containsResource = true;

		resourceAmount = (byte) MathUtils.random(1, 3);

		texRegRes = new TextureRegion(AssetsManager.MANAGER.get(AssetsManager.ATLAS01, Texture.class), 92, 20, 16, 16);
		spriteRes = new Sprite(texRegRes);
		spriteRes.setFlip(MathUtils.randomBoolean(), false);
	}

	@Override
	public void tick(float delta) {
		super.tick(delta);

		spriteRes.setPosition(body.getTransform().getPosition().x - spriteRes.getRegionWidth() / 2,
				body.getTransform().getPosition().y - spriteRes.getRegionHeight() / 2);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		if (super.render) {
			spriteRes.draw(super.getScreen().game.batch);
		}
	}

	@Override
	public void spawnResource() {
		super.spawnResource();

		for (byte i = 0; i < resourceAmount; i++) {
			super.getScreen().resources.add(new Coal(super.getScreen(), body.getPosition().x, body.getPosition().y));
		}
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
