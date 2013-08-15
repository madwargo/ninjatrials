package com.madgear.ninjatrials;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.util.adt.align.HorizontalAlign;

public class GameHUD extends HUD {
	private Text mTextComboMessage = null;
	private float width = ResourceManager.getInstance().cameraWidth;
	private float height = ResourceManager.getInstance().cameraHeight;
	public PowerBarCursor mPowerBarCursor = null;

	
	public void showMessage(String message) { // Displaytime default = 1 second
		showMessage(message, 0.25f, 1.0f, 0.25f); 
	}
	
	public void showMessage(String message, float msgEnterTime, float msgDisplayTime, float msgExitTime) {
		showMessage(message, msgEnterTime, msgDisplayTime, msgExitTime, width/2, height/2); 
	}
	
	public void showMessage(String message, float msgEnterTime, float msgDisplayTime, float msgExitTime, float xPos, float yPos) {
		final Text mTextMessage = new Text(xPos, yPos,
				ResourceManager.getInstance().fontMedium,
				message,
				new TextOptions(HorizontalAlign.CENTER),
				ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		
		attachChild(mTextMessage);
		
		mTextMessage.setAlpha(0);
		mTextMessage.setScale(0.9f);
		
		SequenceEntityModifier mSeqEntMod= new SequenceEntityModifier(
				new ParallelEntityModifier(// entrada logo
						new AlphaModifier(msgEnterTime, 0, 1), 
						new ScaleModifier(msgEnterTime, 0.95f, 1)
				),
				new DelayModifier(msgDisplayTime),// logo quieto
				new ParallelEntityModifier(// entrada logo
						new AlphaModifier(msgExitTime, 1, 0), 
						new ScaleModifier(msgExitTime, 1, 0.95f)
				)
			);
		mTextMessage.registerEntityModifier(mSeqEntMod);
	}
	

	public void showComboMessage(String message){
		showComboMessage(message, width/2, height/2);
	}
	
	public void showComboMessage(String message, float xPos, float yPos){
		mTextComboMessage.setPosition(xPos, yPos);
		mTextComboMessage.setText(message);
		mTextComboMessage.setAlpha(1);
		
	}
	
	public void changeComboMessage(String message){
		mTextComboMessage.setText(message);
	}
	
	public void hideComboMessage(){
		mTextComboMessage.setAlpha(0);
	}
	
	
}
