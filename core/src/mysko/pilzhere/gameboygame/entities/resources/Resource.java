/**
 * 
 */
package mysko.pilzhere.gameboygame.entities.resources;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;

import mysko.pilzhere.gameboygame.entities.Entity;
import mysko.pilzhere.gameboygame.screens.GameScreen;

/**
 * @author PilzHere
 *
 */
public class Resource extends Entity implements Disposable{
	
	protected Body body;
	protected TextureRegion texRegMain;	
	protected Sprite spriteMain;
	public boolean remove = false; 
	
	public Resource(GameScreen screen, float spawnPosX, float spawnPosY) {
		super.setScreen(screen);
	}

	@Override
	public void tick(float delta) {
		if (remove) {
			if (!super.getScreen().b2dWorld.isLocked()) {
				super.getScreen().b2dWorld.destroyBody(this.body);
				super.getScreen().resources.removeValue(this, true);
			}
		}
	}
	
	@Override
	public void render(float delta) {
//		Nothing here
	}
	
	@Override
	public void dispose() {
		
	}
}
