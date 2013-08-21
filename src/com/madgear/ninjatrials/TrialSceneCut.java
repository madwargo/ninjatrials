package com.madgear.ninjatrials;

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
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.util.adt.align.HorizontalAlign;
import com.madgear.ninjatrials.ResourceManager;

/**
 * Cut trial scene.
 *
 * @author Madgear Games
 *
 */
public class TrialSceneCut extends GameScene {
    private static final int SCORE_POOR = 20;
    private static final int SCORE_GREAT = 90;
    private float timeRound;  // tiempo para ciclo de powerbar
    private float timeMax = 10; // Tiempo máximo para corte:
    private float timeCounter = timeMax; // Tiempo total que queda para el corte
    private int frameNum = 0; // Contador para la animación
    private float timerStartedIn = 0; // control de tiempo

    private float width = ResourceManager.getInstance().cameraWidth;
    private float height = ResourceManager.getInstance().cameraHeight;

    private SpriteBackground bg;
    private Tree mTree;
    private Candle candleLeft, candleRight;
    private GameHUD gameHUD;
    private PowerBarCursor powerBarCursor;
    private Chronometer chrono;
    private Character mCharacter;
    private Eyes mEyes;
    private Katana mKatana;
    private Rectangle blinkLayer;
    private boolean cutEnabled = false;
    private TimerHandler trialTimerHandler;
    private IUpdateHandler trialUpdateHandler;
    final private float readyTime = 3f;
    final private float endingTime = 10f;
    private int score = 0;

    /**
     * Calls the super class constructor.
     */
    public TrialSceneCut() {
        super();
    }

    @Override
    public Scene onLoadingScreenLoadAndShown() {
        Scene loadingScene = new Scene(); // Provisional, sera una clase externa
        loadingScene.getBackground().setColor(0.3f, 0.3f, 0.6f);
        // Añadimos algo de texto:
        final Text loadingText = new Text(
                ResourceManager.getInstance().cameraWidth * 0.5f,
                ResourceManager.getInstance().cameraHeight * 0.3f,
                ResourceManager.getInstance().fontBig, "Loading...",
                new TextOptions(HorizontalAlign.CENTER),
                ResourceManager.getInstance().engine.getVertexBufferObjectManager());
        loadingScene.attachChild(loadingText);
        return loadingScene;
    }

    @Override
    public void onLoadingScreenUnloadAndHidden() {}

    /**
     * Loads all the Scene resources and create the main objects.
     */
    @Override
    public void onLoadScene() {
        ResourceManager.getInstance().loadCutSceneResources();
        setTrialDiff(GameManager.getInstance().getSelectedDiff());
        bg = new SpriteBackground(new Sprite(width * 0.5f, height * 0.5f,
                ResourceManager.getInstance().cutBackgroundTR,
                ResourceManager.getInstance().engine.getVertexBufferObjectManager()));
        setBackground(bg);
        mTree = new Tree(width * 0.5f, height * 0.5f + 400);
        candleLeft = new Candle(width * 0.5f - 500, height * 0.5f + 200);
        candleRight = new Candle(width * 0.5f + 500, height * 0.5f + 200);
        gameHUD = new GameHUD();
        powerBarCursor = new PowerBarCursor(200f, 200f, timeRound);
        chrono = new Chronometer(width - 200, height - 200, 10, 0);
        mCharacter = new Character(width / 2 - 120, height / 2);
        mEyes = new Eyes();
        blinkLayer = new Rectangle(width / 2, height / 2, width, height,
                ResourceManager.getInstance().engine.getVertexBufferObjectManager());
        blinkLayer.setAlpha(0f);
        blinkLayer.setColor(1.0f, 1.0f, 1.0f);
        mKatana = new Katana();
    }

    /**
     * Put all the objects in the scene.
     */
    @Override
    public void onShowScene() {
        setBackgroundEnabled(true);
        attachChild(mTree);
        attachChild(candleLeft);
        attachChild(candleRight);
        ResourceManager.getInstance().engine.getCamera().setHUD(gameHUD);
        gameHUD.attachChild(powerBarCursor);
        gameHUD.attachChild(chrono);
        attachChild(mCharacter);
        attachChild(mEyes);
        attachChild(blinkLayer);
        attachChild(mKatana);
        readySequence();
    }

    @Override
    public void onHideScene() {}

    /**
     * Unloads all the scene resources.
     */
    @Override
    public void onUnloadScene() {
        ResourceManager.getInstance().unloadCutSceneResources();
    }

    /**
     * Shows a Ready Message during readyTime seconds. Then calls actionSecuence().
     */
    private void readySequence() {
        timerStartedIn = ResourceManager.getInstance().engine.getSecondsElapsedTotal(); 
        trialUpdateHandler = new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                if(ResourceManager.getInstance().engine.getSecondsElapsedTotal() >
                timerStartedIn + readyTime) {
                    TrialSceneCut.this.unregisterUpdateHandler(trialUpdateHandler);
                    actionSequence();
                  }
            }
            @Override public void reset() {}
        };
        registerUpdateHandler(trialUpdateHandler);
        gameHUD.showMessage("Ready");
    }

    /**
     * Main trial secuence. Shows a "Cut!" message, starts the Chronometer and enables the cut.
     */
    protected void actionSequence() {
        trialUpdateHandler = new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                if(chrono.isTimeOut()) {
                    TrialSceneCut.this.unregisterUpdateHandler(trialUpdateHandler);
                    timeOut();
                }
            }
            @Override public void reset() {}
        };
        registerUpdateHandler(trialUpdateHandler);
        gameHUD.showMessage("Cut!");
        chrono.start();
        powerBarCursor.start();
        cutEnabled = true;
    }

    /**
     * Cutting secuence. Launch each objects cut animation at proper time. Stops the chrono and
     * gets the trial score. After the secuence calls the ending secuence.
     */
    public void cutSequence() {
        cutEnabled = false;
        chrono.stop();
        powerBarCursor.stop();
        score = getScore();
        frameNum = 0;
        trialTimerHandler = new TimerHandler(0.1f, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                pTimerHandler.reset();  // new frame each 0.1 second !
                if (frameNum == 10) mEyes.cut();
                if (frameNum == 14) mCharacter.cut();
                if (frameNum == 16) blink();
                if (frameNum == 18) mKatana.cutRight();
                if (frameNum == 21) mKatana.cutLeft();
                if (frameNum == 24) mKatana.cutCenter();
                if (frameNum == 45) {
                    mTree.cut();
                    candleLeft.cut();
                    candleRight.cut();
                }
                if (frameNum == 60) {
                    TrialSceneCut.this.unregisterUpdateHandler(trialTimerHandler);
                    endingSequence();
                }
                frameNum++;
            }
        });
        registerUpdateHandler(trialTimerHandler);
    }

    /**
     * Shows the score and the final animation. Clean the HUD and calls to the next scene.
     */
    private void endingSequence() {
        String message;
        GameManager.getInstance().incrementScore(score);
        if(score <= SCORE_POOR) {
            message = "POOR, SCORE = " + score;
        }
        else if(score >= SCORE_GREAT) {
            message = "GREAT! SCORE = " + score;
        }
        else {
            message = "MEDIUM, SCORE = " + score;
        }
        gameHUD.showMessage(message, 0.25f, 5.0f, 0.25f);
        trialTimerHandler= new TimerHandler(endingTime, new ITimerCallback()
        {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler)
            {
                TrialSceneCut.this.unregisterUpdateHandler(trialTimerHandler);
                gameHUD.detachChildren();
                SceneManager.getInstance().showScene(new DummyMenu());
            }
        });
        registerUpdateHandler(trialTimerHandler);
    }

    /**
     * When time is out the cut is not enabled. Calls ending secuence.
     */
    private void timeOut() {
        cutEnabled = false;
        powerBarCursor.stop();
        score = 0;
        endingSequence();
    }

    /**
     * When the action button is pressed launch the cut if enabled.
     */
    @Override
    public void onPressButtonO() {
        if (cutEnabled) {
            cutSequence();
        }
    }

    /**
     * Adjust the trial parameters using the game difficulty as base.
     * @param diff The game difficulty.
     */
    private void setTrialDiff(int diff) {
        if(diff == GameManager.getInstance().DIFF_EASY)
            timeRound = 4;
        else if(diff == GameManager.getInstance().DIFF_MEDIUM)
            timeRound = 2;
        else if(diff == GameManager.getInstance().DIFF_HARD)
            timeRound = 1;
    }

    /**
     * Calculates the trial score.
     * @return The Trial Score (int from 0 to 100).
     */
    private int getScore() {
        return Math.round(Math.abs(powerBarCursor.getPowerValue()) - (timeCounter * 3));
    }

    /**
     * Adds a white blink effect to the scene.
     */
    private void blink() {
        blinkLayer.setAlpha(0.9f);
        blinkLayer.registerEntityModifier(new SequenceEntityModifier(
                new DelayModifier(0.6f), new FadeOutModifier(5f)));
    }

    // Auxiliary Classes

    /**
     * The Tree class controls the tree in the scene.
     * @author Madgear Games
     */
    private class Tree extends Entity {
        // Space in pixles between the top and bottom parts:
        private final float gap = 160;
        // Adjust the tree bottom position:
        private float offset = (ResourceManager.getInstance().cutTreeTopTR.getHeight() / 2f +
                ResourceManager.getInstance().cutTreeBottomTR.getHeight() / 2f) - gap;
        private Sprite top, bottom;
        
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

        /**
         * Cut the tree!
         */
        public void cut() {
            top.registerEntityModifier(new MoveModifier(15, top.getX(), top.getY(),
                    top.getX() - 100, top.getY() - 50));
        }
    }

    /**
     * The Candle class controls the candle objects in the scene.
     * @author Madgear Games
     */
    private class Candle extends Entity {
        // Space in pixles between the top and bottom parts:
        private final float gap = 40;
        // Space in pixles between the top and bottom parts:
        private float offset = (ResourceManager.getInstance().cutCandleTopTR.getHeight() / 2f +
                ResourceManager.getInstance().cutCandleBottomTR .getHeight() / 2f) - gap;
        private Sprite top, bottom, light;

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

        /**
         * Cut the candle!
         */
        public void cut() {
            light.setVisible(false);
            // Random values for rotation and ending position:
            top.registerEntityModifier(new ParallelEntityModifier(
                    new JumpModifier(3f,
                            top.getX(),
                            top.getX() + (float) Math.random() * 600 - 300,
                            top.getY(),
                            top.getY() - 400, 100f),
                    new RotationByModifier(2f, (float) Math.random() * 180)));
        }
    }

    /**
     * Controls the character object in the scene
     * @author Madgear Games
     */
    private class Character extends Entity {
        private AnimatedSprite charSprite;

        public Character(float posX, float posY) {
            charSprite = new AnimatedSprite(posX, posY,
                    ResourceManager.getInstance().cutShoTR,
                    ResourceManager.getInstance().engine.getVertexBufferObjectManager());
            attachChild(charSprite);
        }

        /**
         * Character cut animation.
         */
        public void cut() {
            charSprite.animate(new long[] { 100, 50, 50, 1000 }, 0, 3, false);
        }

    }

    /**
     * Eyes class.
     * @author Madgear Games
     */
    private class Eyes extends Entity {
        private Sprite eyesSprite;

        public Eyes() {
            eyesSprite = new Sprite(width / 2, height / 2,
                    ResourceManager.getInstance().cutEyesTR,
                    ResourceManager.getInstance().engine.getVertexBufferObjectManager());
            eyesSprite.setAlpha(0f);
            attachChild(eyesSprite);
        }

        /**
         * Eyes cut animation.
         */
        public void cut() {
            eyesSprite.registerEntityModifier(new SequenceEntityModifier(
                    new FadeInModifier(0.1f),
                    new DelayModifier(0.5f),
                    new FadeOutModifier(0.1f)));
        }
    }

    /**
     * Katana class control the katana cuts.
     * @author Madgear Games
     */
    private class Katana extends Entity {
        private AnimatedSprite katanaSpriteRight;
        private AnimatedSprite katanaSpriteLeft;
        private Sprite katanaSpriteCenter;
        private long[] katanaAnimTime = { 50, 50, 50, 50 };

        public Katana() {
            // Right katana cut:
            katanaSpriteRight = new AnimatedSprite(width / 2 + 300, height / 2,
                    ResourceManager.getInstance().cutSwordSparkle2TR,
                    ResourceManager.getInstance().engine.getVertexBufferObjectManager());
            katanaSpriteRight.setAlpha(0f);
            attachChild(katanaSpriteRight);
            // Inverted left katana cut:
            katanaSpriteLeft = new AnimatedSprite(width / 2 - 300, height / 2,
                    ResourceManager.getInstance().cutSwordSparkle2TR,
                    ResourceManager.getInstance().engine.getVertexBufferObjectManager());
            katanaSpriteLeft.setAlpha(0f);
            katanaSpriteLeft.setFlipped(true, true);
            attachChild(katanaSpriteLeft);
            // Central katana cut (tree):
            katanaSpriteCenter = new Sprite(width / 2, height / 2 + 300,
                    ResourceManager.getInstance().cutSwordSparkle1TR,
                    ResourceManager.getInstance().engine.getVertexBufferObjectManager());
            katanaSpriteCenter.setAlpha(0f);
            katanaSpriteCenter.setFlippedHorizontal(true);
            attachChild(katanaSpriteCenter);
        }

        /**
         * Right katana cut animation.
         */
        public void cutRight() {
            katanaSpriteRight.registerEntityModifier(new SequenceEntityModifier(
                    new FadeInModifier(0.05f), new DelayModifier(0.4f), new FadeOutModifier(0.1f)));
            katanaSpriteRight.animate(katanaAnimTime, 0, 3, false);
        }

        /**
         * Left katana cut animation.
         */
        public void cutLeft() {
            katanaSpriteLeft.registerEntityModifier(new SequenceEntityModifier(
                    new FadeInModifier(0.05f), new DelayModifier(0.4f), new FadeOutModifier(0.1f)));
            katanaSpriteLeft.animate(katanaAnimTime, 0, 3, false);
        }

        /**
         * Center katana cut animation.
         */
        public void cutCenter() {
            katanaSpriteCenter.registerEntityModifier(new SequenceEntityModifier(
                    new FadeInModifier(0.1f), new DelayModifier(0.2f), new FadeOutModifier(0.1f)));
        }
    }
}
