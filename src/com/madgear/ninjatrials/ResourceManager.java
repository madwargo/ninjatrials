package com.madgear.ninjatrials;


import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.debug.Debug;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;



public class ResourceManager {

	private static final TextureOptions mTransparentTextureOption = TextureOptions.BILINEAR;
	
	
	// ResourceManager Singleton instance
	private static ResourceManager INSTANCE;
	
	
	/* The variables listed should be kept public, allowing us easy access
	   to them when creating new Sprites, Text objects and to play sound files */
	
	public NinjaTrials activity;
	public Engine engine;
	public Context context;
	public float cameraWidth;
	public float cameraHeight;
	public TextureManager textureManager;
	
	
	// CUT SCENE:
	public static ITiledTextureRegion cutShoTR;
	public static ITextureRegion cutTreeTR;
	public static ITiledTextureRegion cutCandleTR;
	public static ITiledTextureRegion cutCandleLightTR;
	public static ITextureRegion cutEyesTR;
	public static ITextureRegion cutBackgroundTR;
	public static ITextureRegion cutSweatDropTR;
	public static ITextureRegion cutSwordSparkle1TR;
	public static ITiledTextureRegion cutSwordSparkle2TR;
	public static ITextureRegion cutHudBarTR;
	public static ITextureRegion cutHudCursorTR;
	
	
	// FONTS:
	public Font fontSmall;		// pequeño
	public Font fontBig;		// grande
	
	
	
	
	
	
	
	//public BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	
	public ITiledTextureRegion mTiledTextureRegion;
	
	
	//public ITextureRegion mSpriteTextureRegion;

	
	public Music music;
	
	public Sound mSound;

	public float cameraScaleFactorX = 1;

	public float cameraScaleFactorY = 1;


	
	// Inicializa el manejador:
	public static void setup(NinjaTrials pActivity, Engine pEngine, Context pContext, 
			float pCameraWidth, float pCameraHeight){
		
		getInstance().activity = pActivity;
		getInstance().engine = pEngine; 
		getInstance().context = pContext;
		getInstance().cameraWidth = pCameraWidth;
		getInstance().cameraHeight = pCameraHeight;		
		getInstance().textureManager = pActivity.getTextureManager();
	}
	
	// Constructor:
	ResourceManager(){
		// The constructor is of no use to us
	}

	public synchronized static ResourceManager getInstance(){
		if(INSTANCE == null){
			INSTANCE = new ResourceManager();
		}
		return INSTANCE;
	}

	// Cada escena debe tener sus métodos para cargar y descargar recursos (metodo load y unload).
	// tanto en gráficos como música y sonido.
	// Deben ser "synchronized".
	
	
	// Recursos para la escena de corte:
	public synchronized void loadCutSceneResources() {
		
		// Texturas:
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/trial_cut/");
		
		BuildableBitmapTextureAtlas
			shoTextureAtlas = new BuildableBitmapTextureAtlas(textureManager, 1742, 1720, mTransparentTextureOption),
			//treeTextureAtlas = new BuildableBitmapTextureAtlas(textureManager, 640, 950, mTransparentTextureOption),
			candleTextureAtlas = new BuildableBitmapTextureAtlas(textureManager, 310, 860, mTransparentTextureOption),
			candleLightTextureAtlas = new BuildableBitmapTextureAtlas(textureManager, 760, 380, mTransparentTextureOption),
			swordSparkle2TextureAtlas = new BuildableBitmapTextureAtlas(textureManager, 1358, 1034, mTransparentTextureOption);
		
		cutShoTR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(shoTextureAtlas, context, "cut_ch_sho_cut_anim.png", 2, 2);
		//cutTreeTR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(treeTextureAtlas, context, "cut_breakable_tree.png", 1, 2);
		cutCandleTR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(candleTextureAtlas, context, "cut_breakable_candle_base.png", 1, 2);
		cutCandleLightTR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(candleLightTextureAtlas, context, "cut_breakable_candle_light.png", 2, 1);
		cutSwordSparkle2TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(swordSparkle2TextureAtlas, context, "cut_sword_sparkle2.png", 1, 2);
		
		try {
			shoTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
			//treeTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
			candleTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
			candleLightTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
			swordSparkle2TextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
			
		} catch (TextureAtlasBuilderException e) { 
			e.printStackTrace(); 
		}
		
		shoTextureAtlas.load();
		//treeTextureAtlas.load();		
		candleTextureAtlas.load();
		candleLightTextureAtlas.load();
		swordSparkle2TextureAtlas.load();
		
		
		BitmapTextureAtlas
			eyesTextureAtlas = new BitmapTextureAtlas(textureManager, 1416, 611, mTransparentTextureOption),
			treeTextureAtlas = new BitmapTextureAtlas(textureManager, 640, 950, mTransparentTextureOption),
			backgroundTextureAtlas = new BitmapTextureAtlas(textureManager, 1920, 1080, mTransparentTextureOption),
			sweatDropTextureAtlas = new BitmapTextureAtlas(textureManager, 46, 107, mTransparentTextureOption),
			swordSparkle1TextureAtlas = new BitmapTextureAtlas(textureManager, 503, 345, mTransparentTextureOption),
			hudBarTextureAtlas = new BitmapTextureAtlas(textureManager, 240, 120, mTransparentTextureOption),
			hudCursorTextureAtlas = new BitmapTextureAtlas(textureManager, 59, 52, mTransparentTextureOption);
													

		if(cutEyesTR==null) {
			cutEyesTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(eyesTextureAtlas, activity, "cut_ch_sho_eyes.png", 0, 0);
			eyesTextureAtlas.load();
		}

		if(cutTreeTR==null) {
			cutTreeTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(eyesTextureAtlas, activity, "cut_breakable_tree.png", 0, 0);
			treeTextureAtlas.load();
		}
		
		if(cutBackgroundTR==null) {
			cutBackgroundTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backgroundTextureAtlas, activity, "cut_background.png", 0, 0);
			backgroundTextureAtlas.load();
		}
		
		if(cutSweatDropTR==null) {
			cutSweatDropTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sweatDropTextureAtlas, activity, "cut_ch_sweatdrop.png", 0, 0);
			sweatDropTextureAtlas.load();
		}
		
		if(cutSwordSparkle1TR==null) {
			cutSwordSparkle1TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(swordSparkle1TextureAtlas, activity, "cut_sword_sparkle1.png", 0, 0);
			swordSparkle1TextureAtlas.load();
		}
		
		if(cutHudBarTR==null) {
			cutHudBarTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudBarTextureAtlas, activity, "cut_hud_bar.png", 0, 0);
			hudBarTextureAtlas.load();
		}
		
		if(cutHudCursorTR==null) {
			cutHudCursorTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudCursorTextureAtlas, activity, "cut_hud_cursor.png", 0, 0);
			hudCursorTextureAtlas.load();
		}
		
		
		// Sonido:
		
	}
	
	// Liberamos los recursos de la escena de corte:
	public synchronized void unloadCutSceneResources() {
	
		if(cutShoTR!=null) {
			if(cutShoTR.getTexture().isLoadedToHardware()) {
				cutShoTR.getTexture().unload();
				cutShoTR = null;
			}
		}
		if(cutTreeTR!=null) {
			if(cutTreeTR.getTexture().isLoadedToHardware()) {
				cutTreeTR.getTexture().unload();
				cutTreeTR = null;
			}
		}
		if(cutCandleTR!=null) {
			if(cutCandleTR.getTexture().isLoadedToHardware()) {
				cutCandleTR.getTexture().unload();
				cutCandleTR = null;
			}
		}
		if(cutCandleLightTR!=null) {
			if(cutCandleLightTR.getTexture().isLoadedToHardware()) {
				cutCandleLightTR.getTexture().unload();
				cutCandleLightTR = null;
			}
		}		
		if(cutEyesTR!=null) {
			if(cutEyesTR.getTexture().isLoadedToHardware()) {
				cutEyesTR.getTexture().unload();
				cutEyesTR = null;
			}
		}
		if(cutBackgroundTR!=null) {
			if(cutBackgroundTR.getTexture().isLoadedToHardware()) {
				cutBackgroundTR.getTexture().unload();
				cutBackgroundTR = null;
			}
		}
		if(cutSweatDropTR!=null) {
			if(cutSweatDropTR.getTexture().isLoadedToHardware()) {
				cutSweatDropTR.getTexture().unload();
				cutSweatDropTR = null;
			}
		}
		if(cutSwordSparkle1TR!=null) {
			if(cutSwordSparkle1TR.getTexture().isLoadedToHardware()) {
				cutSwordSparkle1TR.getTexture().unload();
				cutSwordSparkle1TR = null;
			}
		}
		if(cutSwordSparkle2TR!=null) {
			if(cutSwordSparkle2TR.getTexture().isLoadedToHardware()) {
				cutSwordSparkle2TR.getTexture().unload();
				cutSwordSparkle2TR = null;
			}
		}
		if(cutHudBarTR!=null) {
			if(cutHudBarTR.getTexture().isLoadedToHardware()) {
				cutHudBarTR.getTexture().unload();
				cutHudBarTR = null;
			}
		}
		if(cutHudCursorTR!=null) {
			if(cutHudCursorTR.getTexture().isLoadedToHardware()) {
				cutHudCursorTR.getTexture().unload();
				cutHudCursorTR = null;
			}
		}
		
		// Garbage Collector:
		System.gc();
	}
	
	
	
	
	/* Each scene within a game should have a loadTextures method as well
	 * as an accompanying unloadTextures method. This way, we can display
	 * a loading image during scene swapping, unload the first scene's textures
	 * then load the next scenes textures.
	 */
	public synchronized void loadGameTextures(Engine pEngine, Context pContext){
		// Set our game assets folder in "assets/gfx/game/"
		//BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		
		/* Create the bitmap texture atlas for the sprite's texture
		region */
		BuildableBitmapTextureAtlas mBitmapTextureAtlas =
				new BuildableBitmapTextureAtlas(pEngine.getTextureManager(), 1548, 332, TextureOptions.BILINEAR);
		
		/* Create the TiledTextureRegion object, passing in the usual
		parameters, as well as the number of rows and columns in our sprite sheet
		for the final two parameters */
		// 6 = nº de imágenes que tiene la animación :D
		mTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, pContext, "sprite1.png", 6, 1);
		
		/* Build the bitmap texture atlas */
		try {
			mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
		} catch (TextureAtlasBuilderException e) {
			e.printStackTrace();
		}
		
		/* Load the bitmap texture atlas into the device's gpu memory
		*/
		mBitmapTextureAtlas.load();
		
		
	}
	
	/* All textures should have a method call for unloading once
	 * they're no longer needed; ie. a level transition. */
	public synchronized void unloadGameTextures(){
		// call unload to remove the corresponding texture atlas from memory
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = (BuildableBitmapTextureAtlas) mTiledTextureRegion.getTexture();
		mBitmapTextureAtlas.unload();
		
		// ... Continue to unload all textures related to the 'Game' scene
		
		// Once all textures have been unloaded, attempt to invoke the Garbage Collector
		System.gc();
	}
	
	
	
	// Se crea un método de load/unload para cada escena:
	
/*	 Similar to the loadGameTextures(...) method, except this method will be
	 * used to load a different scene's textures
	 
	public synchronized void loadMenuTextures(Engine pEngine, Context pContext){
		// Set our menu assets folder in "assets/gfx/menu/"
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(pEngine.getTextureManager() ,800 , 480);
		
		mMenuBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, pContext, "menu_background.png");
		
		try {
			mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
			mBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
		
	}
	
	// Once again, this method is similar to the 'Game' scene's for unloading
	public synchronized void unloadMenuTextures(){
		// call unload to remove the corresponding texture atlas from memory
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = (BuildableBitmapTextureAtlas) mMenuBackgroundTextureRegion.getTexture();
		mBitmapTextureAtlas.unload();
		
		// ... Continue to unload all textures related to the 'Game' scene
		
		// Once all textures have been unloaded, attempt to invoke the Garbage Collector
		System.gc();
	}*/
	
	
	
	
	/* As with textures, we can create methods to load sound/music objects
	 * for different scene's within our games.
	 */
	public synchronized void loadSounds(Engine pEngine, Context pContext){
		// Set the SoundFactory's base path
		SoundFactory.setAssetBasePath("sounds/");
		 try {
			 // Create mSound object via SoundFactory class
			 mSound	= SoundFactory.createSoundFromAsset(pEngine.getSoundManager(), pContext, "sound.mp3");			 
		 } catch (final IOException e) {
             Log.v("Sounds Load","Exception:" + e.getMessage());
		 }
	}	
	
	/* In some cases, we may only load one set of sounds throughout
	 * our entire game's life-cycle. If that's the case, we may not
	 * need to include an unloadSounds() method. Of course, this all
	 * depends on how much variance we have in terms of sound
	 */
	public synchronized void unloadSounds(){
		// we call the release() method on sounds to remove them from memory
		if(!mSound.isReleased())mSound.release();
	}
	
	
	/* Lastly, we've got the loadFonts method which, once again,
	 * tends to only need to be loaded once as Font's are generally 
	 * used across an entire game, from menu to shop to game-play.
	 */
	public synchronized void loadFonts(Engine pEngine){
		FontFactory.setAssetBasePath("fonts/");
		
		// Small = 64
		fontSmall = FontFactory.createFromAsset(pEngine.getFontManager(), pEngine.getTextureManager(), 256, 256, 
				activity.getAssets(), "go3v2.ttf", 64f, true, org.andengine.util.adt.color.Color.WHITE_ABGR_PACKED_INT);
		fontSmall.load();
		
		// Big = 128
		fontBig = FontFactory.createFromAsset(pEngine.getFontManager(), pEngine.getTextureManager(), 256, 256, 
				activity.getAssets(), "go3v2.ttf", 128f, true, org.andengine.util.adt.color.Color.WHITE_ABGR_PACKED_INT);
		fontBig.load();	
		
		
	}
	
	/* If an unloadFonts() method is necessary, we can provide one
	 */
	public synchronized void unloadFonts(){
		fontSmall.unload();
		fontBig.unload();
	}
}