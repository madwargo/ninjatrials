package com.madgear.ninjatrials;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;

/** 
 * Barra de energía con un cursor que se mueve de izquierda a derecha.
 * 
 * El cursor se mueve desde el valor mínimo al máximo (de 0 a 200), tomando los valores intermedios.
 * El cursor completa un ciclo completo en un tiempo igual a timeRound.
 * 
 * cursorMin	valor mínimo del cursor
 * cursorMax	valor máximo del cursor
 * speed		velocidad del cursor. v = e/t => speed = 2 * (cursorMax - cursorMin) / timeRound
 * curXInit		Posición inicial del cursor en eje X.
 * direction	Dirección del cursor. 1 = derecha, -1 = izda;
 * 
 * @author Madgear Games
 */
@SuppressWarnings({ "static-access" })
public class PowerBarCursor extends Entity {
	private final float cursorMin = 0f;
	private final float cursorMax = 200f;
	private float cursorValue = 0f;
	private float speed;
	private int direction = 1;
	private float curXInit;
	private Sprite bar, cursor;
	
	/*
	 * timeRound	tiempo (en seg.) que tarda el cursor en ir desde la posición inicial a la izquierda hacia la derecha y volver.
	 * inicialmente el cursor no se mueve a menos que indiquemos con el comando start().
	 */
	public PowerBarCursor(float posX, float posY, float timeRound) {
		curXInit = posX-100;
		bar = new Sprite(posX, posY,
				ResourceManager.getInstance().hudPowerBarCursorTR,
				ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		cursor = new Sprite(curXInit, posY+60,
				ResourceManager.getInstance().hudCursorTR,
				ResourceManager.getInstance().engine.getVertexBufferObjectManager());
		attachChild(bar);
		attachChild(cursor);
		speed = 2 * (cursorMax - cursorMin) / timeRound;
		setIgnoreUpdate(true);
	}
	
	public void setCursorValue(float value) {
		if (value >= cursorMin && value <= cursorMax)
			cursorValue = value;
	}
	
	public void start() {
		setIgnoreUpdate(false);
	}
	
	public void stop() {
		setIgnoreUpdate(true);
	}
		
	/** 
	 * @return un valor entre -100 y 100, siendo 0 el valor del cursor en el centro, -100 el valor más a la derecha,
	 * y +100 el valor más a la derecha.
	 */
	public int getPowerValue() {
		return Math.round(cursorValue) - 100;
	}
	
	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		// controlamos que no se vaya el cursor por el retraso:
		if(pSecondsElapsed < 0.2) cursorValue += pSecondsElapsed*speed*direction;
		cursor.setX(curXInit + cursorValue);
		if(cursorValue >= cursorMax) direction = -1;
		if(cursorValue <= cursorMin) direction = 1;
		super.onManagedUpdate(pSecondsElapsed);
	}
}
