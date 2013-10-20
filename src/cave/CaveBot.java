package cave;

import MemoryAccess.Locations;
import java.awt.AWTException;
import java.awt.Point;
import java.util.Timer;
import java.util.TimerTask;

/*
 * This class controls the player when activated.
 */
public class CaveBot {

    static CaveBot instance = null;
    CaveBotActions caveActions = CaveBotActions.getInstance();
    MonsterHandler monsterHandler = MonsterHandler.getInstance();
    Looter looter = Looter.getInstance();
    //--------------------------------------------------------------------------Script Objects
    String[] script = new String[1];
    int currentLine = 0;
    int finalLine = 0;
    private boolean finishedLine = false;
    //--------------------------------------------------------------------------Timing Objects
    boolean isRunning = false;
    boolean isPaused = false;
    boolean attackingMonster = false;
    boolean looting = false;

    /*
     Task to handle the processing.
     */
    TimerTask caveTasking = new TimerTask() {
        @Override
        public void run() {
            try {
                if (!isPaused && !attackingMonster && !looting) {
                    readLine();
                    if (finishedLine) {
                        currentLine++;
                        finishedLine = false;
                        if (currentLine > finalLine) {
                            currentLine = 0;
                        }
                    }
                }
            } catch (AWTException ex) {
                System.out.println("Something stupid happened.");
            }
        }
    };
    Timer caveTimer = new Timer();

    /*
     * Creates a new CaveBot with the script currently loaded into scriptArea.
     */
    private CaveBot() {
        finalLine = script.length - 1;
    }

    /*
     * returns this instance of cavebot
     */
    public static CaveBot getInstance() {
        if (instance == null) {
            instance = new CaveBot();
            return instance;
        }
        return instance;
    }

    /*
     * Starts cavebot from the start of the specified script.
     */
    public void start() {
        if (!isRunning) {
            System.out.println("Starting caveTasking");
            script = main.GUI.scriptArea.getText().split("\n");
            finalLine = script.length - 1;
            caveTimer.schedule(caveTasking, 2000, 500);
            caveActions.start();
            monsterHandler.startDetection();
            looter.startLooting();
            isRunning = true;
        }
    }

    /*
     * Stops the cavebot.
     */
    public void stop() {
        caveTimer.cancel();
        isRunning = false;
    }

    /*
     * Pauses the bot at current line in script. When unpaused, continues
     * from where left off.
     */
    public void togglePause() {
        if (!isPaused) {
            isPaused = true;
            caveActions.isPaused = true;
            monsterHandler.isPaused = true;
            looter.isPaused = true;
        } else {
            isPaused = false;
            caveActions.isPaused = false;
            monsterHandler.isPaused = false;
            looter.isPaused = false;
        }
    }

    /*
     * Reads and executes the next line in the script.
     */
    private void readLine() throws AWTException {
        String current = script[currentLine];
        interpretLine(current);
    }

    /*
     * Interprets the current cavebot script line and does stuff
     */
    private void interpretLine(String current) throws AWTException {
        String[] parsed = current.split(",");
        System.out.println(current);
        System.out.println(Locations.returnCurrentTarget());

        //----------------------------------------------------------------------handle movement
        if (parsed[0].equals("MOVE")) {
            Point moveHere = new Point(Integer.valueOf(parsed[1]), Integer.valueOf(parsed[2]));
            //System.out.println("moving to " + moveHere.x + ":" + moveHere.y);
            caveActions.setPoint(moveHere);
            //System.out.println("i am at : " + Locations.returnCoords());
            if (moveHere.x == Locations.returnCoords().x && moveHere.y == Locations.returnCoords().y) {
                finishedLine = true;
            }

        }

        //----------------------------------------------------------------------handle saying things
        if (parsed[0].equals("SAY")) {
            String say = parsed[1];
            System.out.println("saying " + say);
            try {
                caveTimer.wait(2000);
            } catch (InterruptedException ex) {
                System.out.println("Tried to wait for bot to finish typing message, but got error.");
            }
            caveActions.sayThis(say);
            finishedLine = true;
        }

        //----------------------------------------------------------------------handle using things
        if (parsed[0].equals("USE")) {
            Point useHere = new Point(Integer.valueOf(parsed[1]), Integer.valueOf(parsed[2]));
            //System.out.println("using at location " + useHere.x + ":" + useHere.y);
            caveActions.useAt(useHere);
            finishedLine = true;
        }
        if (parsed[0].equals("PAUSEATTACK")) {
            monsterHandler.attackingPaused = true;
        }
        if (parsed[0].equals("UNPAUSEATTACK")) {
            monsterHandler.attackingPaused = false;
        }
    }
}
