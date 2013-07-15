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

	// Resolución de la cámara:
	private static final int WIDTH = 1920;		// Ouya res.
	private static final int HEIGHT = 1080;		// Ouya res.

	// La cámara:
	private Camera mCamera;
	
	
	
	/* Opciones para el motor:
		- Política de ratio 16/9.
		- Landscape fixed
		- Sonido: sí
		- Música: sí
	 */
	@Override
	public EngineOptions onCreateEngineOptions() {

		// Define our mCamera object
		mCamera = new Camera(0, 0, WIDTH, HEIGHT);

		// Declare & Define our engine options to be applied to our Engine object
		EngineOptions engineOptions = new EngineOptions(
				true,
				ScreenOrientation.LANDSCAPE_FIXED,
				new RatioResolutionPolicy(16/9f),   // Ratio 16:9
				mCamera);

		// It is necessary in a lot of applications to define the following
		// wake lock options in order to disable the device's display
		// from turning off during gameplay due to inactivity
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);

		// Música:
		engineOptions.getAudioOptions().setNeedsMusic(true);
		
		// Sonido:
		engineOptions.getAudioOptions().setNeedsSound(true);
		
		
		// Return the engineOptions object, passing it to the engine
		return engineOptions;
	}
	
	
	// Engine de Máximo FPS = 60;
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
				WIDTH, HEIGHT, 0,0);

		// Iniciamos la puntuación, fase, etc
		GameManager.getInstance().resetGame();
		
		// Se crea el fichero de datos del usuario si no existe:
		UserData.getInstance().init(ResourceManager.getInstance().context);

		/* We should notify the pOnCreateResourcesCallback that we've finished
		 * loading all of the necessary resources in our game AFTER they are loaded.
		 * onCreateResourcesFinished() should be the last method called.  */
		pOnCreateResourcesCallback.onCreateResourcesFinished();
		
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		
/*		// Create the Scene object
		Scene mScene = new Scene();
		mScene.getBackground().setColor(0.09804f, 0.6274f, 0.8784f);
		*/
	
		// Notify the callback that we're finished creating the scene, returning
		// mScene to the mEngine object (handled automatically)
		pOnCreateSceneCallback.onCreateSceneFinished(null);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) {
		
		// Iniciamos a la primera escena:
		SceneManager.getInstance().showScene(new CutScene());
		
		
		// onPopulateSceneFinished(), similar to the resource and scene callback
		// methods, should be called once we are finished populating the scene.
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	/* Music objects which loop continuously should be played in
	 * onResumeGame() of the activity life cycle
	 */
	@Override
	public synchronized void onResumeGame() {
		if(ResourceManager.getInstance().music != null && !ResourceManager.getInstance().music.isPlaying()){
			ResourceManager.getInstance().music.play();
		}
		super.onResumeGame();
	}
	
	
	/* Music objects which loop continuously should be paused in
	 * onPauseGame() of the activity life cycle
	 */
	@Override
	public synchronized void onPauseGame() {
		if(ResourceManager.getInstance().music != null && ResourceManager.getInstance().music.isPlaying()){
			ResourceManager.getInstance().music.pause();
		}
		super.onPauseGame();
	}



}
