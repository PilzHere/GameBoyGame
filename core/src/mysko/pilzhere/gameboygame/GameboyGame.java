package mysko.pilzhere.gameboygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2D;

import mysko.pilzhere.gameboygame.assets.AssetsManager;
import mysko.pilzhere.gameboygame.screens.GameScreen;
import mysko.pilzhere.gameboygame.screens.StartupScreen;

public class GameboyGame extends Game {
	
//	Window data.
	public static final String APP_TITLE = "Steamboy DIG";
	public static final int GAMEBOY_WINDOW_WIDTH = 160;
	public static final int GAMEBOY_WINDOW_HEIGHT = 144;
	public static final int DEFAULT_WINDOW_SCALE = 4;
	
	public static boolean USING_PROFILER = false; // false
	public static boolean USING_B2D_DEBUG_RENDERER = false; // false
	public static GLProfiler profiler;
	
	public SpriteBatch batch;
	public OrthogonalTiledMapRenderer mapRenderer;
	
	@Override
	public void create () {
		Box2D.init();
		
		if (USING_PROFILER) {
			profiler = new GLProfiler(Gdx.graphics);
			profiler.enable();
		}
		
		loadAssets();
		
		batch = new SpriteBatch();
		
		this.setScreen(new StartupScreen(this));
//		this.setScreen(new GameScreen(this));
		
		Gdx.graphics.setWindowedMode(GameboyGame.GAMEBOY_WINDOW_WIDTH * GameboyGame.DEFAULT_WINDOW_SCALE, GameboyGame.GAMEBOY_WINDOW_HEIGHT * GameboyGame.DEFAULT_WINDOW_SCALE);
	}
	
	private void loadAssets() {
		AssetsManager.loadFonts();
		AssetsManager.MANAGER.update();
		AssetsManager.MANAGER.finishLoading();
		AssetsManager.loadMaps();
		AssetsManager.MANAGER.update();
		AssetsManager.MANAGER.finishLoading();
		AssetsManager.loadTextures();
		AssetsManager.MANAGER.update();
		AssetsManager.MANAGER.finishLoading();
		AssetsManager.loadSounds();
		AssetsManager.MANAGER.update();
		AssetsManager.MANAGER.finishLoading();
	}

	public void tick() {		
		if (USING_PROFILER) {
			profiler.reset();
		}
	}
	
	@Override
	public void render () {
		tick();
		
		Gdx.gl.glClearColor(15f/256, 40f/256, 15f/256, 1); // GameBoy darkest shade.
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
		mapRenderer.dispose();
		
		AssetsManager.dispose();
	}
}
