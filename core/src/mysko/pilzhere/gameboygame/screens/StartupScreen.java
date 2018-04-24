/**
 * 
 */
package mysko.pilzhere.gameboygame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import mysko.pilzhere.gameboygame.GameboyGame;
import mysko.pilzhere.gameboygame.assets.AssetsManager;

/**
 * @author PilzHere
 *
 */
public class StartupScreen implements Screen {
	public GameboyGame game;

	private BitmapFont gbFont;

	private OrthographicCamera cam;
	private Viewport viewport;

	private Texture startBG = AssetsManager.MANAGER.get(AssetsManager.START_BG, Texture.class);

	public StartupScreen(GameboyGame game) {
		this.game = game;

		this.gbFont = AssetsManager.MANAGER.get(AssetsManager.FONT_GB, BitmapFont.class);

		cam = new OrthographicCamera();
		cam.setToOrtho(false, Gdx.graphics.getWidth() / GameScreen.PPM, Gdx.graphics.getHeight() / GameScreen.PPM);
		cam.far = 1;
		cam.position.set(new Vector2(GameboyGame.GAMEBOY_WINDOW_WIDTH / GameScreen.PPM * 8,
				GameboyGame.GAMEBOY_WINDOW_HEIGHT / GameScreen.PPM * 8), 0);
		cam.update();
		viewport = new FitViewport(160, 144, cam);
	}

	@Override
	public void show() {

	}

	public void handleInput(float delta) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			this.dispose();
			game.setScreen(new GameScreen(game));
		}
	}

	public void tick(float delta) {

	}

	@Override
	public void render(float delta) {
		handleInput(delta);
		tick(delta);

		game.batch.begin();

		game.batch.draw(startBG, 0, 0);
		gbFont.draw(game.batch, "SteamBoy DIG", 160 / 10, 144 / 1.25f);
		gbFont.draw(game.batch, "Press -ENTER-", 160 / 10, 144 / 1.25f - 16);

		game.batch.end();

		cam.update();
	}

	@Override
	public void resize(int width, int height) {
		cam.viewportWidth = width;
		cam.viewportHeight = height;
		viewport.update(width, height);

		cam.update();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}

}
