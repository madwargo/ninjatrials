package com.madgear.ninjatrials;

import org.andengine.entity.Entity;
import org.andengine.entity.text.Text;

/*
 * Reloj de cuatro dígitos.
 * Formato: 00:00
 * Cada dígito tiene su propia posición (de 1 a 4), de manera que no se mueve el cronometro según largo.
 */
public class Chronometer extends Entity {
	private float timeValue;
	private float initialValue;
	private float finalValue;
	private float direction;
	private float posX, posY, pos1X, pos2X, pos3X, pos4X;
	Text digit1, digit2, digit3, digit4, colon;
	
	public Chronometer(float posX, float posY, int initialValue, int finalValue) {
		this.initialValue = (float)initialValue;
		this.finalValue = (float)finalValue;
		this.timeValue = this.initialValue;
		this.posX = posX;
		this.posY = posY;
		
		digit1 = new Text(posX-100, posY, ResourceManager.getInstance().fontMedium,"0",
				ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		digit2 = new Text(posX-50, posY, ResourceManager.getInstance().fontMedium,"0",
				ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		digit3 = new Text(posX+50, posY, ResourceManager.getInstance().fontMedium,"0",
				ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		digit4 = new Text(posX+100, posY, ResourceManager.getInstance().fontMedium,"0",
				ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		colon = new Text(posX, posY, ResourceManager.getInstance().fontMedium, ":",
				ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		
		setIgnoreUpdate(true);
		if(initialValue < finalValue) direction = 1;
		else direction = -1;
		draw();
	}

	public float getTimeValue() {
		return timeValue;
	}
	
	public void setTimeValue(float value) {
		if (value >= initialValue && value <= finalValue)
			timeValue = value;
	}
	
	public void start() {
		setIgnoreUpdate(false);
	}
	
	public void stop() {
		setIgnoreUpdate(true);
	}
	
	private void draw() {
		String timeString = String.format("%2.2f", timeValue);
		digit1.setText(timeString.substring(0,0));
		digit2.setText(timeString.substring(1,1));
		digit1.setText(timeString.substring(3,3));
		digit1.setText(timeString.substring(4,4));
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		timeValue += pSecondsElapsed * direction;
		draw();
		super.onManagedUpdate(pSecondsElapsed);
	}
}
