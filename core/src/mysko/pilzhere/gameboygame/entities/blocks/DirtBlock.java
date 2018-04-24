/**
 * 
 */
package mysko.pilzhere.gameboygame.entities.blocks;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Disposable;

import mysko.pilzhere.gameboygame.physics.CollisionContactListener;
import mysko.pilzhere.gameboygame.screens.GameScreen;

/**
 * @author PilzHere
 *
 */
public class DirtBlock extends Block implements Disposable {

	/**
	 * @param screen
	 * @param spawnPosX
	 * @param spawnPosY
	 */
	public DirtBlock(GameScreen screen, float spawnPosX, float spawnPosY) {
		super(screen, spawnPosX, spawnPosY);

		super.createBody(spawnPosX, spawnPosY, BodyType.StaticBody, CollisionContactListener.BLOCK_COL,
				CollisionContactListener.BLOCK_COLLIDES_WITH);

		super.maxHp = 3;
		super.currentHp = super.maxHp;
	}

	@Override
	public void tick(float delta) {
		super.tick(delta);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
	}

	@Override
	public void dispose() {
		// So far nothing to dispose...
		super.dispose();
	}
}
