package com.madgear.ninjatrials;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.JumpModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.RotationByModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.util.adt.align.HorizontalAlign;

import android.util.Log;

import com.madgear.ninjatrials.ResourceManager;



public class SceneGameCut extends ManagedScene {

	float timeMax = 2;				// Tiempo máximo para corte:
	float timeCounter = timeMax;	// Tiempo total que queda para el corte
	int score; 						// puntuación 
	int secuenceNum = 0;  		// Contador para la animación de corte
	
	float width = ResourceManager.getInstance().cameraWidth;
	float height = ResourceManager.getInstance().cameraHeight;

	SpriteBackground bg;
	Tree mTree;
	Candle candleLeft, candleRight;
	HUD pHUD;
	PowerBar mPowerBar;
	Text countingText;
	Character mCharacter;
	Eyes mEyes;
	Katana mKatana;
	Rectangle blinkLayer;  // capa para el destello

	
	public SceneGameCut() {
		super(1f);
	}

	@Override
	public Scene onLoadingScreenLoadAndShown() {  

		Scene loadingScene = new Scene(); // Provisional, sera una clase externa
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
				ResourceManager.getInstance().cameraWidth -200, 
				ResourceManager.getInstance().cameraHeight -200,
				ResourceManager.getInstance().fontSmall,
				"00.00",
				"00.00".length(),
				//new TextOptions(HorizontalAlign.LEFT),
				ResourceManager.getInstance().engine.getVertexBufferObjectManager());

		countingText.setColor(0.6f, 0.6f, 0.2f);
		attachChild(countingText);
	
		
		// Personaje:
		mCharacter = new Character(width/2-120, height/2);
		attachChild(mCharacter);
		
		// Ojos:
		mEyes = new Eyes();
		attachChild(mEyes);
		
		// Destello:
		blinkLayer = new Rectangle(width/2, height/2, width, height,
				ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		blinkLayer.setAlpha(0f);
		blinkLayer.setColor(1.0f, 1.0f, 1.0f); // blanco
		attachChild(blinkLayer);

		// Corte Katana:
		mKatana = new Katana();
		attachChild(mKatana);
		
		
		// Controlarmos desfase de tiempo (se restará en el primer update):
		timeCounter += ResourceManager.getInstance().engine.getSecondsElapsedTotal();
		
		// Update:
		registerUpdateHandler(new IUpdateHandler() {
			// Por cada update movemos el cursor a su posición y comprobamos si se acabó el tiempo:
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(timeCounter <= 0) {
					timeOut();  // Si el tiempo llega a 0 timeout!
					SceneGameCut.this.unregisterUpdateHandler(this);
				}
				else {
					countingText.setText(String.format("%2.2f", timeCounter));  // Pintamos al inicio para no tener tiempo negativo
					timeCounter -= pSecondsElapsed;
					mPowerBar.updateCursorPos(pSecondsElapsed);	
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
	
	// Destello:
	public void blink() {
		blinkLayer.setAlpha(0.95f);
		blinkLayer.registerEntityModifier(new SequenceEntityModifier (
				new DelayModifier(0.6f), new FadeOutModifier(4f)));
	}
	
	// Secuencia de corte:
	// Hay que lanzar cada animación en el tiempo correcto.
	public void cut() {
		secuenceNum = 0;
		registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback()
        {                      
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler)
            {          
            	pTimerHandler.reset();  // esto hace que se repita el timer de nuevo
            	
            	if(secuenceNum == 10) mEyes.cut();
            	if(secuenceNum == 14) mCharacter.cut();
            	if(secuenceNum == 16) blink();
            	if(secuenceNum == 18) mKatana.cutRight();
            	if(secuenceNum == 21) mKatana.cutLeft();
            	if(secuenceNum == 24) mKatana.cutCenter();
            	if(secuenceNum == 45) {
            		mTree.cut();
            		candleLeft.cut();
            		candleRight.cut();
            	}
            	if(secuenceNum == 100) SceneGameCut.this.unregisterUpdateHandler(pTimerHandler);
            	
            	secuenceNum += 1; // Avanzamos la secuencia (decimas de segundo)
            }
        }));
	}
	
	
	// Se acabó el tiempo!!
	public void timeOut(){
		countingText.setText("0.00");
		cut(); //provisional
	}
	
	
	// Puntuación:
	// score (0-100) = valor power (0-100) - penalizacion por tiempo (segundos transcurridos x3)
	public int score() {
		return Math.round(mPowerBar.getPowerValue() - (timeCounter * 3));
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
		public void cut() {
			top.registerEntityModifier(new MoveModifier(20, top.getX(), top.getY(), top.getX()-100, top.getY()-50));
		}
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
		public void cut() {
			light.setVisible(false);
			// Rompemos el farol con valores aleatorios para posicion final y rotación:
			top.registerEntityModifier(new ParallelEntityModifier(
					new JumpModifier(3f, top.getX(), top.getX()+(float)Math.random()*600-300,
							top.getY(), top.getY()-400, 100f),
					new RotationByModifier(2f, (float)Math.random()*180)));
		}
	}
	
	
	// Clase barra de energía:
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
		
		// Devuelve un valor entre 0 y 100, siendo 100 el valor optimo
		public int getPowerValue() {
			return 100 - Math.abs(Math.round(cursorValue) - 100);
		}
	}
	
	
	// Clase personaje:
	private class Character extends Entity {
		AnimatedSprite charSprite;
		
		public Character(float posX, float posY) {
			charSprite = new AnimatedSprite(posX, posY,
					ResourceManager.getInstance().cutShoTR,
					ResourceManager.getInstance().engine.getVertexBufferObjectManager());
			attachChild(charSprite);
		}
		
		public void cut() {
			charSprite.animate(new long[] {100, 50, 50, 1000}, 0, 3, false);
		}
		
	}
	
	
	// Clase para los ojos:
	private class Eyes extends Entity {
		Sprite eyesSprite;
		
		public Eyes() {
			eyesSprite = new Sprite(width/2, height/2,
					ResourceManager.getInstance().cutEyesTR,
					ResourceManager.getInstance().engine.getVertexBufferObjectManager());
			eyesSprite.setAlpha(0f);  // inicialmente no se ven.
			attachChild(eyesSprite);	
		
		}
		
		public void cut() {
			eyesSprite.registerEntityModifier(new SequenceEntityModifier(
					new FadeInModifier(0.1f),new DelayModifier(0.5f),new FadeOutModifier(0.1f)));
		}
	}

	
	// Katanas:
	private class Katana extends Entity {
		AnimatedSprite katanaSpriteRight;
		AnimatedSprite katanaSpriteLeft;
		Sprite katanaSpriteCenter;
		
		long[] katanaAnimTime =  {50, 50, 50, 50};
		
		public Katana() {
			// Katana derecha
			katanaSpriteRight = new AnimatedSprite(width/2 + 300, height/2,
						ResourceManager.getInstance().cutSwordSparkle2TR,
						ResourceManager.getInstance().engine.getVertexBufferObjectManager());
			katanaSpriteRight.setAlpha(0f);
			attachChild(katanaSpriteRight);
			
			// Katana izquierda (invertida):
			katanaSpriteLeft = new AnimatedSprite(width/2 - 300, height/2,
					ResourceManager.getInstance().cutSwordSparkle2TR,
					ResourceManager.getInstance().engine.getVertexBufferObjectManager());
			katanaSpriteLeft.setAlpha(0f);
			katanaSpriteLeft.setFlipped(true, true);
			attachChild(katanaSpriteLeft);
			
			// Katana central (arbol):
			katanaSpriteCenter = new Sprite(width/2, height/2 + 300,
					ResourceManager.getInstance().cutSwordSparkle1TR,
					ResourceManager.getInstance().engine.getVertexBufferObjectManager());
			katanaSpriteCenter.setAlpha(0f);  // inicialmente no se ve.
			katanaSpriteCenter.setFlippedHorizontal(true);
			attachChild(katanaSpriteCenter);		
		}
		
		public void cutRight() {
			katanaSpriteRight.registerEntityModifier(new SequenceEntityModifier(
					new FadeInModifier(0.05f), new DelayModifier(0.4f), new FadeOutModifier(0.1f)));
			katanaSpriteRight.animate(katanaAnimTime, 0, 3, false);
		}
		
		public void cutLeft() {		
			katanaSpriteLeft.registerEntityModifier(new SequenceEntityModifier(
					new FadeInModifier(0.05f), new DelayModifier(0.4f), new FadeOutModifier(0.1f)));
			katanaSpriteLeft.animate(katanaAnimTime, 0, 3, false);
		}		
		
		public void cutCenter() {
			katanaSpriteCenter.registerEntityModifier(new SequenceEntityModifier(
					new FadeInModifier(0.1f), new DelayModifier(0.2f), new FadeOutModifier(0.1f)));
		}
	}
	
	
	
}




