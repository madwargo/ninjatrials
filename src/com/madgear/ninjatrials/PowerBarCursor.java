package com.madgear.ninjatrials;

import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;


/**
 * Energy bar with a cursor that moves from left to right, and in reverse direction when
 * the cursor reach the right margin.

 * Cursor moves from a minimum value to the maximum value (from 0 to 200), taking all the range
 * of values. The cursos makes a whole cycle in a time "timeRound".
 *
 * The cursor speed is calculated based on timeRound.
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

    /**
     * Contruct a PowerBarCursor object.
     *
     * @param posX Position axis X.
     * @param posY Position axis Y.
     * @param timeRound Time in seconds the cursor takes in complete a whole cycle. It's used to
     * calculate the cursor speed.
     */
    public PowerBarCursor(float posX, float posY, float timeRound) {
        curXInit = posX - 100;
        bar = new Sprite(posX, posY,
                ResourceManager.getInstance().hudPowerBarCursorTR,
                ResourceManager.getInstance().engine.getVertexBufferObjectManager());
        cursor = new Sprite(curXInit, posY + 60,
                ResourceManager.getInstance().hudCursorTR,
                ResourceManager.getInstance().engine.getVertexBufferObjectManager());
        attachChild(bar);
        attachChild(cursor);
        speed = 2 * (cursorMax - cursorMin) / timeRound;
        setIgnoreUpdate(true);
    }

    /**
     * Sets the cursor value.
     * @param value The new cursor value.
     */
    public void setCursorValue(float value) {
        if (value >= cursorMin && value <= cursorMax)
            cursorValue = value;
    }

    /**
     * Continue moving the cursor.
     */
    public void start() {
        setIgnoreUpdate(false);
    }

    /**
     * Stops moving the cursor.
     */
    public void stop() {
        setIgnoreUpdate(true);
    }

    /**
     * Gets the power value.
     * @return An integer value from -100 (left) to 100 (right). 0 is the center value.
     */
    public int getPowerValue() {
        return Math.round(cursorValue) - 100;
    }

    /**
     * Updates the value of the cursor position and controls when the cursor reach the left or
     * right margin, changing the cursor direction.
     * Cursor doesn't move if the time passed is higher than 0.2 s from the last update.
     */
    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        // controlamos que no se vaya el cursor por el retraso:
        if (pSecondsElapsed < 0.2)
            cursorValue += pSecondsElapsed * speed * direction;
        cursor.setX(curXInit + cursorValue);
        if (cursorValue >= cursorMax)
            direction = -1;
        if (cursorValue <= cursorMin)
            direction = 1;
        super.onManagedUpdate(pSecondsElapsed);
    }
}
