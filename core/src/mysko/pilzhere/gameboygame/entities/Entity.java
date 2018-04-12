/**
 * 
 */
package mysko.pilzhere.gameboygame.entities;

import mysko.pilzhere.gameboygame.screens.GameScreen;

/**
 * @author PilzHere
 *
 */
public abstract class Entity {
	private GameScreen screen;
	
	public int id;
	
//	public Entity() {
//		
//	}
	
	public abstract void tick(float delta);
	
	public abstract void render(float delta);

	public GameScreen getScreen() {
		return screen;
	}

	public void setScreen(GameScreen screen) {
		this.screen = screen;
	}
}
