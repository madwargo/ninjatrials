package com.madgear.ninjatrials;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.util.adt.align.HorizontalAlign;



public class SceneGameCut extends ManagedScene {

	float timeMax = 10;				// Tiempo máximo para corte:
	float timeCounter = timeMax;	// Tiempo total que queda para el corte
	float
		width = ResourceManager.getInstance().cameraWidth,
		height = ResourceManager.getInstance().cameraHeight;
	
	SpriteBackground bg;
	Tree mTree;
	Candle candleLeft, candleRight;
	HUD pHUD;
	PowerBar mPowerBar;
	public Text countingText;


	
	public SceneGameCut() {
		super(1f);
	}

	@Override
	public Scene onLoadingScreenLoadAndShown() {

		Scene loadingScene = new Scene();
		loadingScene.getBackground().setColor(0.3f, 0.3f, 0.6f);
		
		// Añadimos algo de texto:
		final Text loadingText = new Text(
				ResourceManager.getInstance().cameraWidth * 0.5f, 
				ResourceManager.getInstance().cameraHeight *0.3f,
				ResourceManager.getInstance().fontBig,
				"Loading...",
				new TextOptions(HorizontalAlign.CENTER),
				ResourceManager.getInstance().engine.getVertexBufferObjectManager());

		loadingScene.attachChild(loadingText);
		
		return loadingScene;
	}

	@Override
	public void onLoadingScreenUnloadAndHidden() {
		// TODO Auto-generated method stub

	}

	// Cargamos recursos:
	@Override
	public void onLoadScene() {
		//getBackground().setColor(0.09804f, 0.6274f, 0.8784f);  // fondo de prueba para ver si carga.

		//this.setBackground(new SpriteBackground(ResourceManager.getInstance().cut_eyes));
		ResourceManager.getInstance().loadCutSceneResources();

	}

	@Override
	public void onShowScene() {

		// Fondo:
		bg = new SpriteBackground(new Sprite(
				width * 0.5f,
				height * 0.5f,
				ResourceManager.getInstance().cutBackgroundTR,
				ResourceManager.getInstance().engine.getVertexBufferObjectManager()));
		setBackground(bg);
		setBackgroundEnabled(true);
		
		// Arbol:
		mTree = new Tree(width * 0.5f, height * 0.5f + 400);
		attachChild(mTree);

		// Faroles:
		candleLeft = new Candle(width * 0.5f - 500, height * 0.5f + 200);
		candleRight = new Candle(width * 0.5f + 500, height * 0.5f + 200);
		attachChild(candleLeft);
		attachChild(candleRight);
		
		// HUD:
		pHUD = new HUD();
		ResourceManager.getInstance().engine.getCamera().setHUD(pHUD);
		mPowerBar = new PowerBar(200f, 200f);
		pHUD.attachChild(mPowerBar);
	
		
		// Crono de prueba:
		countingText = new Text(
				ResourceManager.getInstance().cameraWidth * 0.5f, 
				ResourceManager.getInstance().cameraHeight * 0.3f,
				ResourceManager.getInstance().fontSmall,
				"00.00",
				"00.00".length(),
				new TextOptions(HorizontalAlign.CENTER),
				ResourceManager.getInstance().engine.getVertexBufferObjectManager());

		attachChild(countingText);
	
		
		// Controlarmos desfase de tiempo (se restará en el primer update):
		timeCounter += ResourceManager.getInstance().engine.getSecondsElapsedTotal();
		
		// Update:
		registerUpdateHandler(new IUpdateHandler() {
			// Por cada update movemos el cursor a su posición y comprobamos si se acabó el tiempo:
			@Override
			public void onUpdate(float pSecondsElapsed) {
				
				if(timeCounter <= 0)
					timeOut();  // Si el tiempo llega a 0 timeout!
				else {
					timeCounter -= pSecondsElapsed;
					mPowerBar.updateCursorPos(pSecondsElapsed);
					
					countingText.setText(String.valueOf(
							Math.round(timeCounter)));
				}
				
			}
			
			@Override public void reset() {}
		});
	}


	
	@Override
	public void onHideScene() {
		// TODO Auto-generated method stub

	}

	
	// Liberamos recursos:
	@Override
	public void onUnloadScene() {
		ResourceManager.getInstance().unloadCutSceneResources();
	}
	
	
	
	// Se acabó el tiempo!!
	public void timeOut(){
		
	}
	
	
	// Clases auxiliares:
	
	// Clase Arbol:
	private class Tree extends Entity {
		private final float gap = 160;  // espacio entre las partes superior y la inferior
		// calcula el desplazamiento entre las partes
		private float offset = (ResourceManager.getInstance().cutTreeTopTR.getHeight() / 2f +
				ResourceManager.getInstance().cutTreeBottomTR.getHeight() / 2f ) - gap;
		
		Sprite top, bottom;
		
		public Tree(float posX, float posY) {
			top = new Sprite(posX, posY,
					ResourceManager.getInstance().cutTreeTopTR,
					ResourceManager.getInstance().engine.getVertexBufferObjectManager());
			bottom = new Sprite(posX, posY - offset,
					ResourceManager.getInstance().cutTreeBottomTR,
					ResourceManager.getInstance().engine.getVertexBufferObjectManager());
			attachChild(bottom);
			attachChild(top);
		}
		// Rompe el arbol:
		public void shatter() {}
	}
	
	
	// Clase Farol:
	private class Candle extends Entity {
		private final float gap = 40;  // espacio entre las partes superior y la inferior
		// calcula el desplazamiento entre las partes
		private float offset = (ResourceManager.getInstance().cutCandleTopTR.getHeight() / 2f +
				ResourceManager.getInstance().cutCandleBottomTR.getHeight() / 2f) - gap;
		
		Sprite top, bottom, light;
		
		public Candle(float posX, float posY) {
			top = new Sprite(posX, posY,
					ResourceManager.getInstance().cutCandleTopTR,
					ResourceManager.getInstance().engine.getVertexBufferObjectManager());
			bottom = new Sprite(posX, posY - offset,
					ResourceManager.getInstance().cutCandleBottomTR,
					ResourceManager.getInstance().engine.getVertexBufferObjectManager());
			light = new Sprite(posX, posY,
					ResourceManager.getInstance().cutCandleLightTR,
					ResourceManager.getInstance().engine.getVertexBufferObjectManager());
			light.setAlpha(0.6f);
			
			attachChild(bottom);
			attachChild(top);
			attachChild(light);

		}
		// Rompe el farol
		public void shatter() {}
	}
	
	
	private class PowerBar extends Entity {
		final float timeRound = 1f; 	// nº de segundos que tarda en hacer un ciclo el cursor
		final float cursorMin = 0f;
		final float cursorMax = 200f;  	// el cursor se mueve desde la pos 0 a 200.
		float cursorValue = 0f;			// Inicialmente = 0;
		float cursorOffset;				// Posición inicial del cursor en eje X
		float speed = (cursorMax - cursorMin) / timeRound;  // v = e/t   velocidad
		int direction = 1; 				// Dirección del cursor. 1 = derecha, -1 = izda;
		
		Sprite bar, cursor;
		
		public PowerBar(float posX, float posY) {
			bar = new Sprite(posX, posY,
					ResourceManager.getInstance().cutHudBarTR,
					ResourceManager.getInstance().engine.getVertexBufferObjectManager());
			cursor = new Sprite(posX-100, posY+60,
					ResourceManager.getInstance().cutHudCursorTR,
					ResourceManager.getInstance().engine.getVertexBufferObjectManager());
			attachChild(bar);
			attachChild(cursor);
			
			cursorOffset = posX-100;  // 
		}
		
		// Calcula la posición del cursor pasado un tiempo dado:
		public void updateCursorPos(float time) {
			if(time < 0.2) cursorValue += time*speed*direction;  // controlamos que no se vaya el cursor por el retraso
			cursor.setX(cursorOffset+cursorValue);
			if(cursorValue >= cursorMax) direction = -1;
			if(cursorValue <= cursorMin) direction = 1;
		}
	}
	

}
