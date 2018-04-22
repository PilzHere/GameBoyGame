package mysko.pilzhere.gameboygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
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
	public static boolean USING_B2D_DEBUG_RENDERER = true; // false
	public static GLProfiler profiler;
	
	public SpriteBatch batch;
	
	@Override
	public void create () {
		Box2D.init();
		
		if (USING_PROFILER) {
			profiler = new GLProfiler(Gdx.graphics);
			profiler.enable();
		}
		
		loadAssets();
		
		batch = new SpriteBatch();
		
//		this.setScreen(new StartupScreen(this));
		this.setScreen(new GameScreen(this));
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

//	private long waitTime = 1000L;
//	private long waitTimeEnd;
//	private boolean waitTimeSet = false;
//	private boolean onlyOnce = false;
	public void tick() {
//		if (!onlyOnce) {
//			if (!waitTimeSet) {
//				waitTimeEnd = System.currentTimeMillis() + waitTime;
//				waitTimeSet = true;
//			} else {
//				if (System.currentTimeMillis() >= waitTimeEnd) {
//					this.setScreen(new GameScreen(this));
//					onlyOnce = true;
//				}
//			}
//		}		
		
		if (USING_PROFILER) {
			profiler.reset();
		}
	}
	
	@Override
	public void render () {
		tick();
		
		Gdx.gl.glClearColor(15f/256, 40f/256, 15f/256, 1); // GameBoy darkest shade.
//		Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
		
		AssetsManager.dispose();
	}
}
