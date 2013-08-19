package com.madgear.ninjatrials;

import org.andengine.entity.Entity;
import org.andengine.entity.text.Text;

/**
 * Choronometer with 4 digits with format 00:00.
 * Each digit has his own position, this way the digits doesn't move if the lenght of the
 * string changes.
 * 
 * @author Madgear Games
 */
public class Chronometer extends Entity {
    private float timeValue;
    private float initialValue;
    private float finalValue;
    private float direction;
    private float posX, posY;
    private boolean timeOut;
    Text digit1, digit2, digit3, digit4, colon;

    /**
     * Construct a chronometer.
     * @param posX Position axis X.
     * @param posY Position axis Y.
     * @param initialValue Initial time value. 
     * @param finalValue Final time value.
     */
    public Chronometer(float posX, float posY, int initialValue, int finalValue) {
        this.initialValue = (float) initialValue;
        this.finalValue = (float) finalValue;
        this.timeValue = this.initialValue;
        this.posX = posX;
        this.posY = posY;

        digit1 = new Text(posX - 100, posY,
                ResourceManager.getInstance().fontMedium, "0",
                ResourceManager.getInstance().engine.getVertexBufferObjectManager());
        digit2 = new Text(posX - 50, posY,
                ResourceManager.getInstance().fontMedium, "0",
                ResourceManager.getInstance().engine.getVertexBufferObjectManager());
        digit3 = new Text(posX + 50, posY,
                ResourceManager.getInstance().fontMedium, "0",
                ResourceManager.getInstance().engine.getVertexBufferObjectManager());
        digit4 = new Text(posX + 100, posY,
                ResourceManager.getInstance().fontMedium, "0",
                ResourceManager.getInstance().engine.getVertexBufferObjectManager());
        colon = new Text(posX, posY,
                ResourceManager.getInstance().fontMedium, ":",
                ResourceManager.getInstance().engine.getVertexBufferObjectManager());
        setIgnoreUpdate(true);
        if (initialValue < finalValue)
            direction = 1;
        else
            direction = -1;
        draw();
    }

    /**
     * @return The current time of the chronometer.
     */
    public float getTimeValue() {
        return timeValue;
    }

    /**
     * Sets the time value of the chronometer to value.
     * @param value The new time value.
     */
    public void setTimeValue(float value) {
        if (value >= initialValue && value <= finalValue)
            timeValue = value;
    }

    /**
     * Continue counting time.
     */
    public void start() {
        setIgnoreUpdate(false);
    }

    /**
     * Stop counting time.
     */
    public void stop() {
        setIgnoreUpdate(true);
    }

    /**
     * 
     * @return True if the Chronometer has reached his final value;
     */
    public boolean isTimeOut() {
        return timeOut;
    }
    
    /**
     * Draw the chronometer in the screen with the current time value.
     */
    private void draw() {
        String timeString = String.format("%2.2f", timeValue);
        digit1.setText(timeString.substring(0, 0));
        digit2.setText(timeString.substring(1, 1));
        digit1.setText(timeString.substring(3, 3));
        digit1.setText(timeString.substring(4, 4));
    }

    /**
     * Updates the value of current time and draws the chronometer. If the time exceds the final
     * value then stop the chronometer.
     */
    @Override
    protected void onManagedUpdate(final float pSecondsElapsed) {
        if(direction == 1) {
            timeValue += pSecondsElapsed;
            if(timeValue >= finalValue) {
                timeValue = finalValue;
                stop();
                timeOut = true;
            }
        }
        else {
            timeValue -= pSecondsElapsed;
            if(timeValue <= finalValue) {
                timeValue = finalValue;
                stop();
                timeOut = true;
            }
        }
        draw();
        super.onManagedUpdate(pSecondsElapsed);
    }
}
