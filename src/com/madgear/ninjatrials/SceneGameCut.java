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
		
		Sprite tree = new Sprite(
				width * 0.5f,
				height * 0.5f,
				ResourceManager.getInstance().cutTreeTR,
				ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		
		attachChild(tree);
		
		
		
		
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

}
