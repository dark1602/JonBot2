package Controller;

import MemoryAccess.Locations;
import cave.MonsterHandler;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

/*
 * This class will control the healing of the character.
 */
public class Healing {

    private static Healing instance = null;
    //--------------------------------------------------------------------------Selected Healing hotkey
    public static int healingHotkey = 0;
    //--------------------------------------------------------------------------Restoration values
    private final int hpHigh = Integer.valueOf(main.GUI.hpHighBox.getText());
    private final int hpLow = Integer.valueOf(main.GUI.hpLowBox.getText());
    private final int mpRestore = Integer.valueOf(main.GUI.manaBox.getText());
    //--------------------------------------------------------------------------Healing type checks
    public boolean castingAllowed = main.GUI.spellCheck.isSelected();
    public boolean healthPotionsAllowed = main.GUI.potionCheck.isSelected();
    //--------------------------------------------------------------------------Timing Devices(so we don't spam)
    private boolean canCast = true;
    private boolean canPotion = true;
    private final int delay = 1000;
    private long lastCastTime = System.currentTimeMillis();
    private long lastPotionTime = System.currentTimeMillis();
    //--------------------------------------------------------------------------Healing booleans
    private boolean needCriticalHealth = false;
    private boolean needHighHealth = false;
    private boolean needMana = false;
    //--------------------------------------------------------------------------Colors
    public static Color healthColor = new Color(-5636096);
    public static Color manaColor = new Color(-16777046);
    //--------------------------------------------------------------------------Offsets for health bar location
    private final Point healthOffset = new Point(14, 68);
    private static final double pixelPercent = 1.2;
    private final int paperDollHeight = 260;
    //--------------------------------------------------------------------------Character information
    private final int lastMonsterID = 0;
    //--------------------------------------------------------------------------Backpack information
    private final int healthPotionBackpackSlot = 3;
    private final int manaPotionBackpackSlot = 2;
    //--------------------------------------------------------------------------SpellTraining Objects
    private long lastTrainTime = 0;
    private long lastFoodEatTime = 0;
    private int spellCost = Integer.valueOf(main.GUI.manaCostBox.getText());
    //--------------------------------------------------------------------------Timers for healing
    Robot robot;
    TimerTask check = new TimerTask() {
        @Override
        public void run() {
            if (!main.GUI.pauseCheck.isSelected()) {
                //update hotkey to make sure it is always current
                updateHotkey();
                //now check to see if we need to, and if so, can heal
                checkStats();
                spellTrain();
                eatFood();
            }
        }
    };
    Timer timer = new Timer();

    protected Healing() {
        timer.schedule(check, 0, 100);
        lastFoodEatTime = System.currentTimeMillis();
        try {
            robot = new Robot();
            robot.setAutoDelay(100);
        } catch (AWTException ex) {
            System.out.println("Couldnt make healing robot for some reason.");
        }
    }

    /*
     * Returns the instance of the healer
     */
    public static Healing getInstance() {
        if (instance == null) {
            instance = new Healing();
        }
        return instance;
    }

    /*
     * First updates spell and potion timing and checks.
     * Queries current health and mana and see if any are below the threshhold.
     * If so, potions, or manas, as required. Order of healing priority is
     * 1)hpLow
     * 2)hpHigh
     * 3)mpRestore
     */
    public void checkStats() {
        checkTiming();
        checkHealthMana();
        healAsNeeded();
    }

    /*
     * Compared current system time to last healing and potion times to
     * see if it is safe to potion again. If it is, their respective
     * variables will reflect that.
     */
    private void checkTiming() {
        if (System.currentTimeMillis() - lastCastTime >= delay) {
            canCast = true;
        }
        if (System.currentTimeMillis() - lastPotionTime >= delay) {
            canPotion = true;
        }
    }

    /*
     * Updates needHealth and needMana booleans if they are below the user
     * stated amounts.
     */
    @SuppressWarnings("empty-statement")
    private void checkHealthMana() {
        //Learn current percentages to heal at
        int criticalHealthPercent = Integer.valueOf(main.GUI.hpLowBox.getText());
        int lowHealthPercent = Integer.valueOf(main.GUI.hpHighBox.getText());
        int manaPercent = Integer.valueOf(main.GUI.manaBox.getText());

        //Set base locations
        int healthY = Locations.returnStatusY() + healthOffset.y;
        int healthX = Locations.returnStatusX() + healthOffset.x;

        //Now compare colors and update
        if (main.GUI.hpLowCheck.isSelected()) {
            int criticalHealthX = (int) (healthX + (pixelPercent * criticalHealthPercent));
            Color criticalHealth = robot.getPixelColor(criticalHealthX, healthY);

            if (criticalHealth.getRGB() != healthColor.getRGB()) {
                needCriticalHealth = true;
                MonsterHandler.getInstance().needsHealing = true;
            }
        }
        if (main.GUI.hpHighCheck.isSelected()) {
            int lowHealthX = (int) (healthX + (pixelPercent * lowHealthPercent));
            Color highHealth = robot.getPixelColor(lowHealthX, healthY);
            if (highHealth.getRGB() != healthColor.getRGB()) {
                needHighHealth = true;
            }
        }
        if (main.GUI.manaCheck.isSelected()) {
            int lowManaX = (int) (healthX + (pixelPercent * manaPercent));
            Color lowMana = robot.getPixelColor(lowManaX, healthY + 22);
            if (lowMana.getRGB() != manaColor.getRGB()) {
                needMana = true;
                MonsterHandler.getInstance().needsHealing = true;
            }
        }
    }

    /*
     * Use potions, or spells, as needed and allowed, and then update variables.
     */
    private void healAsNeeded() {
        //check critical health first
        if (needCriticalHealth && main.GUI.potionCheck.isSelected()) {
            if (canPotion) {
                needCriticalHealth = false;
                MonsterHandler.getInstance().needsHealing = false;
                healthPotion();
                //we check again here to see if we need to combo heal with spell
                checkHealthMana();
            }
            if (canCast && needCriticalHealth) {
                needCriticalHealth = false;
                MonsterHandler.getInstance().needsHealing = false;
                healingSpell();
            }
        }
        //next check for high healing
        if (needHighHealth && main.GUI.spellCheck.isSelected()) {
            if (canCast) {
                needHighHealth = false;
                healingSpell();
            }
        }
        //finally, restore mana
        if (needMana && main.GUI.potionCheck.isSelected()) {
            if (canPotion) {
                needMana = false;
                MonsterHandler.getInstance().needsHealing = false;
                manaPotion();
            }
        }
    }

    /*
     * Uses a health potion and updates lastPotionTime and canPotion.
     */
    private void healthPotion() {
        //get location to move to
        int healthBPX = Locations.returnPaperDollX() + 32;
        int healthBPY = Locations.returnPaperDollY() + 220 + (90 * healthPotionBackpackSlot);
        //save current location
        int oldX = MouseInfo.getPointerInfo().getLocation().x;
        int oldY = MouseInfo.getPointerInfo().getLocation().y;
        //move to that location
        robot.mouseMove(healthBPX, healthBPY);
        //right click and use the potion
        robot.mousePress(MouseEvent.BUTTON3_MASK);
        robot.mouseRelease(MouseEvent.BUTTON3_MASK);
        //update the variables
        lastPotionTime = System.currentTimeMillis();
        canPotion = false;
        //Now move back to old spot for convenience
        robot.mouseMove(oldX, oldY);
    }

    /*
     * Uses a mana potion and updates lastPotionTime and canPotion.
     */
    private void manaPotion() {
        //get location to move to
        int manaBPX = Locations.returnPaperDollX() + 32;
        int manaBPY = Locations.returnPaperDollY() + 220 + (90 * manaPotionBackpackSlot);
        //save current location
        int oldX = MouseInfo.getPointerInfo().getLocation().x;
        int oldY = MouseInfo.getPointerInfo().getLocation().y;
        //move to that location
        robot.mouseMove(manaBPX, manaBPY);
        //right click and use the potion
        robot.mousePress(MouseEvent.BUTTON3_MASK);
        robot.mouseRelease(MouseEvent.BUTTON3_MASK);
        //update variables
        lastPotionTime = System.currentTimeMillis();
        canPotion = false;
        //Now move back to old spot for convenience
        robot.mouseMove(oldX, oldY);
    }

    /*
     * Casts the players selected healing spell and updates lastCastTime
     * and canCast.
     */
    private void healingSpell() {
        robot.keyPress(healingHotkey);
        robot.keyRelease(healingHotkey);
        lastCastTime = System.currentTimeMillis();
        canCast = false;
    }

    /*
     * External method to make sure hotkey is set.
     */
    private void updateHotkey() {
        switch (main.GUI.hotKeySelect.getSelectedIndex()) {
            case 1:
                healingHotkey = KeyEvent.VK_F1;
            case 2:
                healingHotkey = KeyEvent.VK_F2;
            case 3:
                healingHotkey = KeyEvent.VK_F3;
            case 4:
                healingHotkey = KeyEvent.VK_F4;
            case 5:
                healingHotkey = KeyEvent.VK_F5;
            case 6:
                healingHotkey = KeyEvent.VK_F6;
            case 7:
                healingHotkey = KeyEvent.VK_F7;
            case 8:
                healingHotkey = KeyEvent.VK_F8;
            case 9:
                healingHotkey = KeyEvent.VK_F9;
            case 10:
                healingHotkey = KeyEvent.VK_F10;
            case 11:
                healingHotkey = KeyEvent.VK_F11;
            case 12:
                healingHotkey = KeyEvent.VK_F12;
            default:
                healingHotkey = KeyEvent.VK_F1;
        }
    }

    private void spellTrain() {
        if (main.GUI.spellTrainerCheck.isSelected()) {
            if (System.currentTimeMillis() - lastTrainTime > (spellCost * 1000) + 20000) {
                spellCost = Integer.valueOf(main.GUI.manaCostBox.getText());
                robot.keyPress(KeyEvent.VK_F11);
                robot.keyRelease(KeyEvent.VK_F11);
                lastTrainTime = System.currentTimeMillis();
            }
        }

    }

    private void eatFood() {
        if (main.GUI.foodCheck.isSelected()) {
            if (System.currentTimeMillis() - lastFoodEatTime > 30000) {
                int oldX = MouseInfo.getPointerInfo().getLocation().x;
                int oldY = MouseInfo.getPointerInfo().getLocation().y;
                int foodBPX = Locations.returnPaperDollX() + 32;
                int foodBPY = Locations.returnPaperDollY() + 220 + (90 * healthPotionBackpackSlot) + 90;
                robot.mouseMove(foodBPX, foodBPY);
                robot.mousePress(MouseEvent.BUTTON3_MASK);
                robot.mouseRelease(MouseEvent.BUTTON3_MASK);
                robot.mouseMove(oldX, oldY);
                lastFoodEatTime = System.currentTimeMillis();
            }
        }
    }
}
