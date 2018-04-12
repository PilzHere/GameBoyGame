/**
 * 
 */
package mysko.pilzhere.gameboygame.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
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

		/**
		 * Loads fonts into memory.
		 */
		public static void loadFonts() {
//				MANAGER.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
//				MANAGER.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
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
		public static final String TEST = "textures/test.png";
		public static final String ATLAS01 = "textures/atlas01.png";
		public static final String PLAYER = "textures/player.png";

		/**
		 * Loads textures into memory.
		 */
		public static void loadTextures() {
			TextureParameter texParam = new TextureParameter();
			texParam.magFilter = TextureFilter.Nearest;
			texParam.minFilter = TextureFilter.Nearest;
			texParam.genMipMaps = false;
			
			MANAGER.load(TEST, Texture.class, texParam);
			MANAGER.load(ATLAS01, Texture.class, texParam);
			MANAGER.load(PLAYER, Texture.class, texParam);
		}

		/**
		 * Loads sounds into memory.
		 */
		public static void loadSounds() {
			// SFX
			// manager.load(sfx01, Sound.class);

			// Music
			// manager.load(music01, Music.class);
		}

		public static void dispose() {
			MANAGER.dispose();
		}

}
