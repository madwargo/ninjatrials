package com.madgear.ninjatrials;

import org.andengine.entity.scene.Scene;

public class CutScene extends ManagedScene {

	// Sin pantalla de Loading...
	public final boolean hasLoadingScreen = false;
	
	
	@Override
	public Scene onLoadingScreenLoadAndShown() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadingScreenUnloadAndHidden() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadScene() {
		getBackground().setColor(0.09804f, 0.6274f, 0.8784f);  // fondo de prueba


	}

	@Override
	public void onShowScene() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHideScene() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnloadScene() {
		// TODO Auto-generated method stub

	}

}
