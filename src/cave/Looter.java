package cave;

import MemoryAccess.Locations;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

public class Looter {

    private static Looter instance;
    private static final CaveBot caveInstance = CaveBot.getInstance();
    private static final MonsterHandler monsterInstance = MonsterHandler.getInstance();
    private static final CaveBotActions actionInstance = CaveBotActions.getInstance();
    private int currentBackpack = 1;
    private final int maxBackpack = 20;
    private final int[] rightColumn = {3, 6, 9, 12, 15, 18};
    private final int[] colorsToIgnore = {-11639452,-15266047,-112480032,-3381983,-6141943,-15592942,-5012736, -14935012, -1, 0, -15461356, -15198184, -15263977, -15395563, -15329770, -1513239, -1493501, -1500080, -1506659, -1513239, -1486921, -1506659, 1539556, -1532977, -1546135, -1526397};
    private int[] gold = {-219648, -3965696, -2723072, -7320576, -136448, -219648};
    private Robot goldBot;
    private final int startX = Locations.returnPaperDollX() + 35;
    private final int startY = 290;
    private final int bpHeight = 90;
    private final int bpWidth = 40;
    private final int bpFullY = 747;
    public boolean isPaused = false;

    /*
     Automated checking to see if there is any loot, and if so, handling it.
     */
    private final TimerTask lootCycle = new TimerTask() {
        @Override
        public void run() {
            if (!isPaused) {
                checkForLoot();
            }
        }
    };
    ;
    private final Timer looter = new Timer();
    private boolean isStarted = false;

    /*
     Private constructor.
     */
    private Looter() {
        try {
            goldBot = new Robot();
        } catch (AWTException ex) {
            System.out.println("Couldnt make looting robot for some reason");
        }
    }

    /*
     External function to start looting, if we haven't already.
     */
    public void startLooting() {
        if (!isStarted) {
            looter.schedule(lootCycle, 2000, 100);
            isStarted = true;
        }
    }

    /*
     We only need one instance of this looter.
     */
    public static Looter getInstance() {
        if (instance == null) {
            instance = new Looter();
            return instance;
        }
        return instance;
    }

    /*
     Checks to see if there is any loot to loot. If so, pauses
     cavebot,cavebotactions, and monsterhandler. If no loot,
     makes sure the others are not paused.
     */
    private void checkForLoot() {
        int currentColor = goldBot.getPixelColor(1788, 921).getRGB();
        while (!contains(colorsToIgnore, currentColor) && contains(gold, currentColor) && !isPaused) {
            caveInstance.looting = true;
            monsterInstance.looting = true;
            actionInstance.looting = true;
            System.out.println("gold item " + goldBot.getPixelColor(1788, 833).getRGB());
            goldBot.mouseMove(1787, 921);
            goldBot.keyPress(KeyEvent.VK_CONTROL);
            goldBot.mousePress(MouseEvent.BUTTON1_MASK);
            goldBot.mouseMove(1787, 745);
            goldBot.mouseRelease(MouseEvent.BUTTON1_MASK);
            goldBot.keyRelease(KeyEvent.VK_CONTROL);
            goldBot.delay(20);
            goldBot.keyPress(KeyEvent.VK_ENTER);
            goldBot.keyRelease(KeyEvent.VK_ENTER);
            currentColor = goldBot.getPixelColor(1788, 921).getRGB();
        }
        while (!contains(colorsToIgnore, currentColor) && !contains(gold, currentColor) && !isPaused) {
            caveInstance.looting = true;
            monsterInstance.looting = true;
            actionInstance.looting = true;
            System.out.println("non gold item " + goldBot.getPixelColor(1788, 833).getRGB());
            goldBot.mouseMove(1787, 921);
            goldBot.keyPress(KeyEvent.VK_CONTROL);
            goldBot.mousePress(MouseEvent.BUTTON1_MASK);
            goldBot.mouseMove(1787, 831);
            goldBot.mouseRelease(MouseEvent.BUTTON1_MASK);
            goldBot.keyRelease(KeyEvent.VK_CONTROL);
            goldBot.delay(20);
            goldBot.keyPress(KeyEvent.VK_ENTER);
            goldBot.keyRelease(KeyEvent.VK_ENTER);
            currentColor = goldBot.getPixelColor(1788, 921).getRGB();
        }
        caveInstance.looting = false;
        monsterInstance.looting = false;
        actionInstance.looting = false;

    }

    /*
     Checks to see if the value is in the specified array.
     */
    public boolean contains(final int[] array, final int value) {
        for (final int i : array) {
            if (i == value) {
                return true;
            }
        }
        return false;
    }

    /*
     Pauses cavebot, cavebotactions, and monsterhandler.
     */
    private void pauseEverything() {
        monsterInstance.looting = true;
        caveInstance.looting = true;
    }

    /*
     Unpauses cavebot, cavebotactions, and monsterhandler.
     */
    private void unpauseEverything() {
        monsterInstance.looting = false;
        caveInstance.looting = false;
    }

    /*
     Drags the loot from the corpse into the loot backpack.
     */
    private void lootCorpse() {
        int oldX = MouseInfo.getPointerInfo().getLocation().x;
        int oldY = MouseInfo.getPointerInfo().getLocation().y;
        goldBot.mouseMove(startX, 826);
        goldBot.keyPress(KeyEvent.VK_CONTROL);
        goldBot.mousePress(MouseEvent.BUTTON1_MASK);
        goldBot.mouseMove(startX, 736);
        goldBot.mouseRelease(MouseEvent.BUTTON1_MASK);
        goldBot.delay(50);
        goldBot.keyPress(KeyEvent.VK_ENTER);
        goldBot.keyRelease(KeyEvent.VK_ENTER);
        goldBot.keyRelease(KeyEvent.VK_CONTROL);
        goldBot.mouseMove(oldX, oldY);
    }

    /*
     Returns true if there is something in the last slot of the loot backpack
     meaning it is full.
     */
    private boolean isFull() {
        return !contains(colorsToIgnore, goldBot.getPixelColor(startX + 70, 749).getRGB());
    }

    /*
     Closes the current loot backpack, and opens the next loot backpack. After
     opening the next backpack, scrolls all the way to the end of the loot backpack.
     */
    private void openNextBackpack() {
        //increase the current backpack.
        int oldBP = currentBackpack;
        currentBackpack++;

        //if we still have a backpack we can throw loot into
        if (currentBackpack <= maxBackpack) {
            goldBot.mouseMove(startX + 155, 709);
            //we press the button several times to make sure all bodies are closed. :D
            goldBot.mousePress(MouseEvent.BUTTON1_MASK);
            goldBot.mouseRelease(MouseEvent.BUTTON1_MASK);
            goldBot.mousePress(MouseEvent.BUTTON1_MASK);
            goldBot.mouseRelease(MouseEvent.BUTTON1_MASK);
            goldBot.mousePress(MouseEvent.BUTTON1_MASK);
            goldBot.mouseRelease(MouseEvent.BUTTON1_MASK);
            goldBot.mousePress(MouseEvent.BUTTON1_MASK);
            goldBot.mouseRelease(MouseEvent.BUTTON1_MASK);
            //now open the next backpack
            goldBot.mouseMove(startX + (bpWidth * currentBackpack), 658);
            goldBot.mousePress(MouseEvent.BUTTON3_MASK);
            goldBot.mouseRelease(MouseEvent.BUTTON3_MASK);
            //now see if we need to scroll down to the next row of backpacks, for next time.
            if (contains(rightColumn, oldBP)) {
                goldBot.mouseWheel(3);
            }
            //now scroll down to the end of the new backpack
            goldBot.mouseMove(startX + 30, 750);
            goldBot.mouseWheel(16);
        }
    }
}
