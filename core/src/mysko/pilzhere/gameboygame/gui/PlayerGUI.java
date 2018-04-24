/**
 * 
 */
package mysko.pilzhere.gameboygame.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import mysko.pilzhere.gameboygame.GameboyGame;
import mysko.pilzhere.gameboygame.assets.AssetsManager;
import mysko.pilzhere.gameboygame.entities.Player;
import mysko.pilzhere.gameboygame.screens.GameScreen;

/**
 * @author PilzHere
 *
 */
public class PlayerGUI implements Disposable {
	private GameScreen screen;
	public Player player;

	private BitmapFont gbFont;

	private TextureRegion texRegGUIHeartFull;
	private TextureRegion texRegGUIHeartHalf;
	private TextureRegion texRegGUIPickaxe;

	private TextureRegion texRegGUIWood;
	private TextureRegion texRegGUICoal;
	private TextureRegion texRegGUIIron;

	// private Sprite spriteGUIHeartFull;
	// private Sprite spriteGUIHeartHalf;
	// private Sprite spriteGUIPickaxe;

	private Sprite spriteGUIWood;
	private Sprite spriteGUICoal;
	private Sprite spriteGUIIron;

	public Array<Sprite> spritesGUIHearts = new Array<Sprite>();

	public PlayerGUI(GameScreen screen, Player player) {
		this.screen = screen;
		this.player = player;

		gbFont = AssetsManager.MANAGER.get(AssetsManager.FONT_GB, BitmapFont.class);

		texRegGUIHeartFull = new TextureRegion(AssetsManager.MANAGER.get(AssetsManager.GUI, Texture.class), 1, 1, 7, 8);
		texRegGUIHeartHalf = new TextureRegion(AssetsManager.MANAGER.get(AssetsManager.GUI, Texture.class), 9, 1, 7, 8);
		texRegGUIPickaxe = new TextureRegion(AssetsManager.MANAGER.get(AssetsManager.GUI, Texture.class), 17, 1, 9, 9);

		texRegGUIWood = new TextureRegion(AssetsManager.MANAGER.get(AssetsManager.GUI, Texture.class), 1, 10, 10, 10);
		texRegGUICoal = new TextureRegion(AssetsManager.MANAGER.get(AssetsManager.GUI, Texture.class), 12, 11, 6, 6);
		texRegGUIIron = new TextureRegion(AssetsManager.MANAGER.get(AssetsManager.GUI, Texture.class), 19, 11, 6, 6);

		spriteGUIWood = new Sprite(texRegGUIWood);
		spriteGUICoal = new Sprite(texRegGUICoal);
		spriteGUIIron = new Sprite(texRegGUIIron);

		spritesGUIHearts.ordered = true;
		spritesGUIHearts.insert(0, new Sprite(texRegGUIHeartFull));
		spritesGUIHearts.insert(1, new Sprite(texRegGUIHeartFull));
		spritesGUIHearts.insert(2, new Sprite(texRegGUIHeartFull));
		spritesGUIHearts.insert(3, new Sprite(texRegGUIHeartFull));
		spritesGUIHearts.insert(4, new Sprite(texRegGUIHeartFull));
		spritesGUIHearts.setSize(5);
	}

	public void tick(float delta) {
		spriteGUIWood.setPosition(43, 5);
		spriteGUICoal.setPosition(43 + 34 + 2, 9);
		spriteGUIIron.setPosition(43 + 34 + 2, 1);

		spritesGUIHearts.get(0).setPosition(1, 7);

		for (int i = 1; i < spritesGUIHearts.size; i++) {
			spritesGUIHearts.get(i).setPosition(spritesGUIHearts.get(i - 1).getX() + 7 + 1, 7);
		}

		// Player max hp = 10.
		switch (player.hp) {
		case 9:
			hearts = 5;
			break;
		case 7:
			hearts = 4;
			break;
		case 5:
			hearts = 3;
			break;
		case 3:
			hearts = 2;
			break;
		case 1:
			hearts = 1;
			break;
		default:
			hearts = player.hp / 2;
			break;
		}

		// If uneven: keep last heart half.
		if (player.hp % 2 == 1) {
			for (Sprite spriteGUIHeart : spritesGUIHearts) {
				spriteGUIHeart.setRegion(texRegGUIHeartFull);
			}
			spritesGUIHearts.get(hearts - 1).setRegion(texRegGUIHeartHalf);
		} else {
			for (Sprite spriteGUIHeart : spritesGUIHearts) {
				spriteGUIHeart.setRegion(texRegGUIHeartFull);
			}
		}
	}

	private int hearts = 0;

	public void render(float delta) {
		screen.shapeRenderer.setProjectionMatrix(screen.shapeRenderer.getProjectionMatrix().setToOrtho2D(0, 0,
				GameboyGame.GAMEBOY_WINDOW_WIDTH, GameboyGame.GAMEBOY_WINDOW_HEIGHT));
		screen.shapeRenderer.begin(ShapeType.Filled);

		screen.shapeRenderer.setColor(230f / 256, 1, 220f / 256, 1);
		screen.shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), 16);
		screen.shapeRenderer.setColor(15 / 256, 40f / 256, 15f / 256, 1);
		screen.shapeRenderer.rect(41, 1, 1, 14);
		screen.shapeRenderer.rect(110, 1, 1, 14);
		screen.shapeRenderer.end();

		screen.game.batch.setProjectionMatrix(screen.game.batch.getProjectionMatrix().setToOrtho2D(0, 0,
				GameboyGame.GAMEBOY_WINDOW_WIDTH, GameboyGame.GAMEBOY_WINDOW_HEIGHT));
		screen.game.batch.begin();

		for (int i = 0; i < spritesGUIHearts.size; i++) {
			if (i < hearts) {
				spritesGUIHearts.get(i).draw(screen.game.batch);
			}
		}

		spriteGUIWood.draw(screen.game.batch);
		spriteGUICoal.draw(screen.game.batch);
		spriteGUIIron.draw(screen.game.batch);

		gbFont.draw(screen.game.batch, Byte.toString(player.xWoods), 55, 16);
		gbFont.draw(screen.game.batch, Byte.toString(player.xCoal), 43 + 34 + 10, 16);
		gbFont.draw(screen.game.batch, Byte.toString(player.xIron), 43 + 34 + 10, 8);
		gbFont.draw(screen.game.batch, "E", 1, 8);

		if (GameboyGame.USING_PROFILER) {
			gbFont.draw(screen.game.batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 1, 144);
			gbFont.draw(screen.game.batch, "GLCalls: " + GameboyGame.profiler.getCalls(), 1, 144 - 8);
			gbFont.draw(screen.game.batch, "DrawCalls: " + GameboyGame.profiler.getDrawCalls(), 1, 144 - 8 - 8);
			gbFont.draw(screen.game.batch, "TextureBindings: " + GameboyGame.profiler.getTextureBindings(), 1,
					144 - 8 - 8 - 8);
			gbFont.draw(screen.game.batch, "ShaderSwitches: " + GameboyGame.profiler.getShaderSwitches(), 1,
					144 - 8 - 8 - 8 - 8);
			gbFont.draw(screen.game.batch, "UsedVertices: " + GameboyGame.profiler.getVertexCount().count, 1,
					144 - 8 - 8 - 8 - 8 - 8);
			gbFont.draw(screen.game.batch, "ObjectsRendered: " + screen.objectsRendered, 1,
					144 - 8 - 8 - 8 - 8 - 8 - 8);
		}

		screen.game.batch.end();
	}

	@Override
	public void dispose() {

	}
}
