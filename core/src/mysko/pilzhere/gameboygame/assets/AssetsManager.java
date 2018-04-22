/**
 * 
 */
package mysko.pilzhere.gameboygame.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * @author PilzHere
 *
 */
public class AssetsManager {
	
	public final static AssetManager MANAGER = new AssetManager();
	
	// Resolvers
		private static FileHandleResolver resolver = new InternalFileHandleResolver();

		// Fonts data locations.
//			public static final String FONT_01 = "fonts/font01.ttf";
		public static final String FONT_GB = "fonts/gb.fnt";

		/**
		 * Loads fonts into memory.
		 */
		public static void loadFonts() {
//				MANAGER.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
//				MANAGER.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
			
				MANAGER.setLoader(BitmapFont.class, ".fnt", new BitmapFontLoader(resolver));
				
				BitmapFontParameter bmfParam = new BitmapFontParameter();
				bmfParam.genMipMaps = false;
				bmfParam.magFilter = TextureFilter.Nearest;
				bmfParam.minFilter = TextureFilter.Nearest;
				
				MANAGER.load(FONT_GB, BitmapFont.class, bmfParam);
//
//				FreeTypeFontLoaderParameter size32Params = new FreeTypeFontLoaderParameter();
//				size32Params.fontFileName = FONT_01;
//				size32Params.fontParameters.size = 32;
//				size32Params.fontParameters.shadowOffsetX = 1;
//				size32Params.fontParameters.shadowOffsetY = 1;
//				size32Params.fontParameters.shadowColor = Color.BLACK;
//				MANAGER.load(FONT_01, BitmapFont.class, size32Params);

			// To add more fonts:
			/*
			 * FreeTypeFontLoaderParameter otherFontParams = new
			 * FreeTypeFontLoaderParameter(); otherFontParams.fontFileName =
			 * "fonts/nameOfOtherFont.ttf"; otherFontParams.fontParameters.size = 64;
			 * manager.load("fonts/nameOfOtherFont.ttf", BitmapFont.class, otherFontParams);
			 */
		}

		public static final String MAP01 = "maps/map01.tmx";
		
		public static void loadMaps() {
				MANAGER.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
				TmxMapLoader.Parameters tmxParams = new TmxMapLoader.Parameters();
				tmxParams.textureMinFilter = Texture.TextureFilter.Nearest;
				tmxParams.textureMagFilter = Texture.TextureFilter.Nearest;
				tmxParams.generateMipMaps = false;
				
				MANAGER.load(MAP01, TiledMap.class, tmxParams);
		}
		
		// Textures data locations.
		public static final String GUI = "textures/gui.png";
		public static final String ATLAS01 = "textures/atlas01.png";
		public static final String PLAYER = "textures/player.png";
		public static final String SPIDER = "textures/spider.png";

		/**
		 * Loads textures into memory.
		 */
		public static void loadTextures() {
			TextureParameter texParam = new TextureParameter();
			texParam.magFilter = TextureFilter.Nearest;
			texParam.minFilter = TextureFilter.Nearest;
			texParam.genMipMaps = false;
			
			MANAGER.load(GUI, Texture.class, texParam);
			MANAGER.load(ATLAS01, Texture.class, texParam);
			MANAGER.load(PLAYER, Texture.class, texParam);
			MANAGER.load(SPIDER, Texture.class, texParam);
		}

		public static final String SFX_JUMP = "sounds/sfx/jump.wav";
		public static final String SFX_DIG = "sounds/sfx/dig.wav";
		
		/**
		 * Loads sounds into memory.
		 */
		public static void loadSounds() {
			
			MANAGER.setLoader(Sound.class, ".wav", new SoundLoader(resolver));
			
			// SFX
			MANAGER.load(SFX_JUMP, Sound.class);
			MANAGER.load(SFX_DIG, Sound.class);

			// Music
			// manager.load(music01, Music.class);
		}

		public static void dispose() {
			MANAGER.dispose();
		}

}
