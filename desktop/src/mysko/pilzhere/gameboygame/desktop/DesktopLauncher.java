package mysko.pilzhere.gameboygame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;

import mysko.pilzhere.gameboygame.GameboyGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = GameboyGame.APP_TITLE;
		config.width = GameboyGame.GAMEBOY_WINDOW_WIDTH;
		config.height = GameboyGame.GAMEBOY_WINDOW_HEIGHT;
		config.foregroundFPS = 60;
		config.backgroundFPS = 60;
		config.fullscreen = false;
		config.resizable = true;
		config.initialBackgroundColor = new Color(0.25f, 0.25f, 0.25f, 1);
		config.samples = 0;
		config.vSyncEnabled = false;
//		config.addIcon(path, fileType);
		
		new LwjglApplication(new GameboyGame(), config);
	}
}
