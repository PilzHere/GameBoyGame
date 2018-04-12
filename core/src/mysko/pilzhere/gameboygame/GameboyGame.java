package mysko.pilzhere.gameboygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import mysko.pilzhere.gameboygame.assets.AssetsManager;
import mysko.pilzhere.gameboygame.screens.GameScreen;

public class GameboyGame extends Game {
	public SpriteBatch batch;
	
	@Override
	public void create () {		
		batch = new SpriteBatch();
		
		loadAssets();
		
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

	public void tick() {
		
	}
	
	@Override
	public void render () {
		tick();
		
//		Gdx.gl.glClearColor(77f/256, 83f/256, 60f/256, 1); // GameBoy darkest shade.
		Gdx.gl.glClearColor(0.25f, 0.25f, 0.25f, 1);
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
