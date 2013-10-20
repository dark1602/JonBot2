package cave;

import MemoryAccess.Locations;
import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

public class MonsterHandler {

    //--------------------------------------------------------------------------This instance of monsterhandler
    private static MonsterHandler instance = null;
    private static final CaveBot caveInstance = CaveBot.getInstance();
    private static final CaveBotActions caveActions = CaveBotActions.getInstance();
    //--------------------------------------------------------------------------Objects for logic cycle
    //--------------------------------------------------------------------------
    public boolean handlingMonsters = false;
    Robot targetBot;
    //--------------------------------------------------------------------------Logic objects
    private boolean foundTarget = false;
    private boolean isRunning = false;
    public boolean isPaused = false;
    public boolean needsHealing = false;
    boolean looting = false;
    boolean attackingPaused = false;

    /*
     handler task does all logic every 100ms
     */
    private final TimerTask handlerTask = new TimerTask() {
        @Override
        public void run() {
            if (!isPaused && !needsHealing && !looting && !attackingPaused) {
                targetMonster();
                if (foundTarget) {
                    caveInstance.attackingMonster = true;
                    caveActions.attackingMonster = true;
                }
                if (Locations.returnCurrentTarget() == 0) {
                    foundTarget = false;
                    togglePauseCavebot();
                }
            }
        }

        /*
         * Pauses and upauses the cavebot and it's CaveBotActions
         */
        private void togglePauseCavebot() {
            if (!foundTarget) {
                caveInstance.attackingMonster = false;
                caveActions.attackingMonster = false;
            }
        }

        private void targetMonster() {
            int tileRange = 3;
            int tileWidth = 60;
            if (Locations.returnCurrentTarget() == 0) {
                //System.out.println("Gonna right click monster now");
                Point center = Locations.returnCenterScreen();
                int clientWidth = Locations.returnClientWidth();
                int startX = center.x - (tileRange * tileWidth);
                //System.out.println("Starting here x: " + startX);
                int startY = center.y - (tileRange * tileWidth);
                //System.out.println("Starting here y: " + startY);
                int endX = center.x + (tileRange * tileWidth);
                int endY = center.y + (tileRange * tileWidth);
                while (startX < endX + 60 && !looting) {
                    while (startY < endY && !looting) {
                        //System.out.println("Right clicking here at : " + startX + "-" + startY);
                        targetBot.mouseMove(startX, startY + 60);
                        targetBot.mousePress(MouseEvent.BUTTON3_MASK);
                        targetBot.mouseRelease(MouseEvent.BUTTON3_MASK);
                        if (Locations.returnCurrentTarget() != 0) {
                            foundTarget = true;
                            break;
                        }
                        startY = startY + 60;
                    }
                    startY = center.y - (tileRange * tileWidth);
                    startX = startX + 60;
                    if (Locations.returnCurrentTarget() != 0) {
                        foundTarget = true;
                        break;
                    }
                }

            }
        }
    };
    private final Timer handlerTimer = new Timer();

    /*
     * Private constructor. There shall be only 1!
     */
    private MonsterHandler() {
        try {
            targetBot = new Robot();
            targetBot.setAutoDelay(1);
        } catch (AWTException ex) {
            System.out.println("Target bot cant start for some reason.");
        }
    }

    /*
     * Returns the instance of MonsterHandler
     */
    public static MonsterHandler getInstance() {
        if (instance == null) {
            instance = new MonsterHandler();
            return instance;
        }
        return instance;
    }

    /*
     * Returns true if a new monster has been detected
     */
    public void startDetection() {
        if (!isRunning) {
            handlerTimer.schedule(handlerTask, 2000, 100);
            isRunning = true;
        }
    }
}
