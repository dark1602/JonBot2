package Controller;

import MemoryAccess.Locations;
import cave.CaveBot;
import cave.CaveBotActions;

public class Controller {

    private static Controller instance = null;
    private Healing healer;
    private CaveBotActions keyboard;
    private CaveBot cavebot;

    /*
     * Singleton controller
     */
    protected Controller() {
        healer = new Healing();
        keyboard = CaveBotActions.getInstance();
        cavebot = CaveBot.getInstance();
    }

    /*
     * returns this controller
     */
    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
            return instance;
        }
        return instance;
    }

    /*
     * Starts the cavebot
     */
    public void startCavebot() {
        cavebot.start();
    }
    
    /*
     * Pauses and unpauses the cavebot
     */
    public void toggleCavebotPause(){
        cavebot.togglePause();
    }

    public void test() {
        Locations.test();
    }
}
