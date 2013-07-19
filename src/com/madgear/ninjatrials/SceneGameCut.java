package com.madgear.ninjatrials;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.EntityBackground;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;



public class SceneGameCut extends ManagedScene {

	// Sin pantalla de Loading...
	public final boolean hasLoadingScreen = false;
	
	// Tiempo total transcurrido
	float timeCounter = 0;
	
	@Override
	public Scene onLoadingScreenLoadAndShown() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadingScreenUnloadAndHidden() {
		// TODO Auto-generated method stub

	}

	// Cargamos recursos:
	@Override
	public void onLoadScene() {
		getBackground().setColor(0.09804f, 0.6274f, 0.8784f);  // fondo de prueba para ver si carga.

		//this.setBackground(new SpriteBackground(ResourceManager.getInstance().cut_eyes));
		ResourceManager.getInstance().loadCutSceneResources();

	}

	@Override
	public void onShowScene() {

		float
			width = ResourceManager.getInstance().cameraWidth,
			height = ResourceManager.getInstance().cameraHeight;
		
		// Fondo:
		SpriteBackground fondo = new SpriteBackground(new Sprite(
				width * 0.5f,
				height * 0.5f,
				ResourceManager.getInstance().cutBackgroundTR,
				ResourceManager.getInstance().engine.getVertexBufferObjectManager()));
		setBackground(fondo);
		setBackgroundEnabled(true);
		
		// Arbol:
		Tree mTree = new Tree(width * 0.5f, height * 0.5f + 400);
		attachChild(mTree);

		// Faroles:
		Candle candleLeft = new Candle(width * 0.5f - 500, height * 0.5f + 200);
		Candle candleRight = new Candle(width * 0.5f + 500, height * 0.5f + 200);
		attachChild(candleLeft);
		attachChild(candleRight);
		
	
	
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
		
		Sprite top, bottom;
		
		public Candle(float posX, float posY) {
			top = new Sprite(posX, posY,
					ResourceManager.getInstance().cutCandleTopTR,
					ResourceManager.getInstance().engine.getVertexBufferObjectManager());
			bottom = new Sprite(posX, posY - offset,
					ResourceManager.getInstance().cutCandleBottomTR,
					ResourceManager.getInstance().engine.getVertexBufferObjectManager());
			attachChild(bottom);
			attachChild(top);
		}
		// Rompe el farol
		public void shatter() {}
	}
	
	
	
}
