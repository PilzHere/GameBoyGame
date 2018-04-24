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
import mysko.pilzhere.gameboygame.physics.CollisionContactListener;
import mysko.pilzhere.gameboygame.screens.GameScreen;

/**
 * @author PilzHere
 *
 */
public class UnbreakableBlock extends Block implements Disposable {
	private TextureRegion texRegMain;
	private Sprite spriteMain;

	private int randomTexReg;

	/**
	 * @param screen
	 * @param spawnPosX
	 * @param spawnPosY
	 */
	public UnbreakableBlock(GameScreen screen, float spawnPosX, float spawnPosY) {
		super(screen, spawnPosX, spawnPosY);

		super.createBody(spawnPosX, spawnPosY, BodyType.StaticBody, CollisionContactListener.TERRAIN_COL,
				CollisionContactListener.TERRAIN_COLLIDES_WITH);

		randomTexReg = MathUtils.random(1);

		if (randomTexReg == 0) {
			texRegMain = new TextureRegion(AssetsManager.MANAGER.get(AssetsManager.ATLAS01, Texture.class), 74, 56, 16,
					16);
		} else {
			texRegMain = new TextureRegion(AssetsManager.MANAGER.get(AssetsManager.ATLAS01, Texture.class), 74, 74, 16,
					16);
		}

		spriteMain = new Sprite(texRegMain);
		spriteMain.setFlip(MathUtils.randomBoolean(), MathUtils.randomBoolean());
	}

	@Override
	public void tick(float delta) {
		super.tick(delta);

		spriteMain.setPosition(body.getTransform().getPosition().x - spriteMain.getRegionWidth() / 2,
				body.getTransform().getPosition().y - spriteMain.getRegionHeight() / 2);
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		if (super.render) {
			spriteMain.draw(super.getScreen().game.batch);
		}
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
