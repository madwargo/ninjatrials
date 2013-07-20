package com.madgear.ninjatrials;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;






public class NinjaTrials extends BaseGameActivity {

	// Resoluci�n de la c�mara:
	private static final int WIDTH = 1920;		// Ouya res.
	private static final int HEIGHT = 1080;		// Ouya res.

	// La c�mara:
	private Camera mCamera;
	
	
	
	/* Opciones para el motor:
		- Pol�tica de ratio 16/9.
		- Landscape fixed
		- Sonido: s�
		- M�sica: s�
	 */
	@Override
	public EngineOptions onCreateEngineOptions() {

		// Camara:
		mCamera = new Camera(0, 0, WIDTH, HEIGHT);

		// Opciones de engine:
		EngineOptions engineOptions = new EngineOptions(
				true,
				ScreenOrientation.LANDSCAPE_FIXED,
				new RatioResolutionPolicy(16/9f),   // Ratio 16:9
				mCamera);

		// Pantalla encendida siempre:
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);

		// M�sica:
		engineOptions.getAudioOptions().setNeedsMusic(true);
		
		// Sonido:
		engineOptions.getAudioOptions().setNeedsSound(true);
		
		
		// Return the engineOptions object, passing it to the engine
		return engineOptions;
	}
	
	
	// Engine de M�ximo FPS = 60;
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		return new LimitedFPSEngine(pEngineOptions, 60);
	}

	
	
	
/*	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/

	
	
	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback) {
		
		
		// Setup de ResourceManager.
		ResourceManager.setup(this, this.getEngine(), this.getApplicationContext(),
				WIDTH, HEIGHT);

		// Iniciamos la puntuaci�n, fase, etc
		GameManager.getInstance().resetGame();
		
		// Se crea el fichero de datos del usuario si no existe:
		UserData.getInstance().init(ResourceManager.getInstance().context);

		// Cargamos las fuentes:
		ResourceManager.getInstance().loadFonts(ResourceManager.getInstance().engine);

		pOnCreateResourcesCallback.onCreateResourcesFinished();
		
	}

	
	
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
	
		// No necesitamos ninguna escena
		pOnCreateSceneCallback.onCreateSceneFinished(null);
	}

	
	
	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {
		
		// Iniciamos a la primera escena:
		SceneManager.getInstance().showScene(new SceneGameCut(3f));
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	
	
	// CONTINUAR juego:
	@Override
	public synchronized void onResumeGame() {
		if(ResourceManager.getInstance().music != null && !ResourceManager.getInstance().music.isPlaying()){
			ResourceManager.getInstance().music.play();
		}
		super.onResumeGame();
	}
	
	
	
	// Juego en PAUSA:
	@Override
	public synchronized void onPauseGame() {
		if(ResourceManager.getInstance().music != null && ResourceManager.getInstance().music.isPlaying()){
			ResourceManager.getInstance().music.pause();
		}
		super.onPauseGame();
	}


	// Liberamos la memoria:
	public void onUnloadResources () {
		
		ResourceManager.getInstance().unloadFonts();

		
	}
	
	
	// Esto finaliza el juego:
	/*	// Some devices do not exit the game when the activity is destroyed.
		// This ensures that the game is closed.
		@Override
		protected void onDestroy() {
			super.onDestroy();
			System.exit(0);
		}*/
	
	

}
