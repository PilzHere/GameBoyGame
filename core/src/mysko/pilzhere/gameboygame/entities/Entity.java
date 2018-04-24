/**
 * 
 */
package mysko.pilzhere.gameboygame.entities;

import com.badlogic.gdx.math.Vector3;

import mysko.pilzhere.gameboygame.screens.GameScreen;

/**
 * @author PilzHere
 *
 */
public abstract class Entity {
	private GameScreen screen;

	public boolean render = false;

	public abstract void tick(float delta);

	protected Vector3 bodyCenter = new Vector3();
	protected Vector3 bodyDimensions = new Vector3();
	protected int bBExtra = 0;

	public abstract void render(float delta);

	public void drawRays() {

	}

	public GameScreen getScreen() {
		return screen;
	}

	public void setScreen(GameScreen screen) {
		this.screen = screen;
	}
}
