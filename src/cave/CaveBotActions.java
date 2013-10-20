package cave;

import MemoryAccess.Locations;
import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.*;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

/*
 * Simulates keyboard to do various things for the CaveBot.
 */
public class CaveBotActions {

    public boolean isPaused = false;
    public boolean looting = false;
    TimerTask moveTask = new TimerTask() {
        @Override
        public void run() {
            if (!isPaused && !attackingMonster && Locations.returnCurrentTarget()== 0 && !looting) {
                if (moveTo.x != 0 && moveTo.y != 0) {
                    moveTo(moveTo);
                }
            }
        }
    };
    Timer moveTimer = new Timer();
    Point moveTo = new Point(0, 0);
    private static CaveBotActions instance = null;
    static Robot robot;
    boolean attackingMonster = false;

    public void start() {
        moveTimer.schedule(moveTask, 2000, 100);
    }

    /*
     * Singleton constructor.
     */
    protected CaveBotActions() {
        try {
            CaveBotActions.robot = new Robot();
            robot.setAutoDelay(100);
        } catch (AWTException ex) {
            System.out.println("Error when trying to make robot for keyboard");
        }
    }

    void setPoint(Point moveHere) {
        this.moveTo = moveHere;
    }

    /*
     * Returns CaveBotActions instance.
     */
    public static CaveBotActions getInstance() {
        if (instance == null) {
            instance = new CaveBotActions();
        }
        return instance;
    }

    /*
     * Returns this robot.
     */
    private static Robot returnBot() {
        return CaveBotActions.robot;
    }

    /*
     * Sends arrow key presses until the character is at thisPoint.
     */
    void moveTo(Point thisPoint) {
        if (Locations.returnPlayerX() > thisPoint.x || Locations.returnPlayerX() < thisPoint.x) {
            moveX(thisPoint);
        }
        if (Locations.returnPlayerY() > thisPoint.y || Locations.returnPlayerY() < thisPoint.y) {
            moveY(thisPoint);
        }
    }

    /*
     * Move the character up and down depending on where it needs to go.
     */
    private void moveY(Point thisPoint) {
        int currentY = Locations.returnPlayerY();
        int yDiff = thisPoint.y - currentY;

        if (yDiff < 0 && !attackingMonster && !looting && Locations.returnCurrentTarget() == 0) {
            robot.keyPress(KeyEvent.VK_UP);
            robot.keyRelease(KeyEvent.VK_UP);
        }

        yDiff = thisPoint.y - currentY;
        if (yDiff > 0 && !attackingMonster && !looting && Locations.returnCurrentTarget() == 0) {
            robot.keyPress(KeyEvent.VK_DOWN);
            robot.keyRelease(KeyEvent.VK_DOWN);
        }
    }

    /*
     * Moves the character left and right depending on where it needs to go.
     */
    private void moveX(Point thisPoint) {
        int currentX = Locations.returnPlayerX();
        int xDiff = thisPoint.x - currentX;
        int oldXDiff = xDiff;

        if (xDiff < 0 && !attackingMonster && !looting && Locations.returnCurrentTarget() == 0) {
            robot.keyPress(KeyEvent.VK_LEFT);
            robot.keyRelease(KeyEvent.VK_LEFT);
        }

        xDiff = thisPoint.x - currentX;
        if (xDiff > 0 && !attackingMonster && !looting && Locations.returnCurrentTarget() == 0) {
            robot.keyPress(KeyEvent.VK_RIGHT);
            robot.keyRelease(KeyEvent.VK_RIGHT);
        }

    }
    /*
     * Types out the string to the client and sends it with "enter".
     */

    void sayThis(String string) {
        robot.setAutoDelay(25);
        type(string);
        robot.setAutoDelay(100);
    }

    public void type(CharSequence characters) {
        int length = characters.length();
        for (int i = 0; i < length; i++) {
            char character = characters.charAt(i);
            type(character);
        }
    }

    public void type(char character) {
        switch (character) {
            case 'a':
                doType(VK_A);
                break;
            case 'b':
                doType(VK_B);
                break;
            case 'c':
                doType(VK_C);
                break;
            case 'd':
                doType(VK_D);
                break;
            case 'e':
                doType(VK_E);
                break;
            case 'f':
                doType(VK_F);
                break;
            case 'g':
                doType(VK_G);
                break;
            case 'h':
                doType(VK_H);
                break;
            case 'i':
                doType(VK_I);
                break;
            case 'j':
                doType(VK_J);
                break;
            case 'k':
                doType(VK_K);
                break;
            case 'l':
                doType(VK_L);
                break;
            case 'm':
                doType(VK_M);
                break;
            case 'n':
                doType(VK_N);
                break;
            case 'o':
                doType(VK_O);
                break;
            case 'p':
                doType(VK_P);
                break;
            case 'q':
                doType(VK_Q);
                break;
            case 'r':
                doType(VK_R);
                break;
            case 's':
                doType(VK_S);
                break;
            case 't':
                doType(VK_T);
                break;
            case 'u':
                doType(VK_U);
                break;
            case 'v':
                doType(VK_V);
                break;
            case 'w':
                doType(VK_W);
                break;
            case 'x':
                doType(VK_X);
                break;
            case 'y':
                doType(VK_Y);
                break;
            case 'z':
                doType(VK_Z);
                break;
            case 'A':
                doType(VK_SHIFT, VK_A);
                break;
            case 'B':
                doType(VK_SHIFT, VK_B);
                break;
            case 'C':
                doType(VK_SHIFT, VK_C);
                break;
            case 'D':
                doType(VK_SHIFT, VK_D);
                break;
            case 'E':
                doType(VK_SHIFT, VK_E);
                break;
            case 'F':
                doType(VK_SHIFT, VK_F);
                break;
            case 'G':
                doType(VK_SHIFT, VK_G);
                break;
            case 'H':
                doType(VK_SHIFT, VK_H);
                break;
            case 'I':
                doType(VK_SHIFT, VK_I);
                break;
            case 'J':
                doType(VK_SHIFT, VK_J);
                break;
            case 'K':
                doType(VK_SHIFT, VK_K);
                break;
            case 'L':
                doType(VK_SHIFT, VK_L);
                break;
            case 'M':
                doType(VK_SHIFT, VK_M);
                break;
            case 'N':
                doType(VK_SHIFT, VK_N);
                break;
            case 'O':
                doType(VK_SHIFT, VK_O);
                break;
            case 'P':
                doType(VK_SHIFT, VK_P);
                break;
            case 'Q':
                doType(VK_SHIFT, VK_Q);
                break;
            case 'R':
                doType(VK_SHIFT, VK_R);
                break;
            case 'S':
                doType(VK_SHIFT, VK_S);
                break;
            case 'T':
                doType(VK_SHIFT, VK_T);
                break;
            case 'U':
                doType(VK_SHIFT, VK_U);
                break;
            case 'V':
                doType(VK_SHIFT, VK_V);
                break;
            case 'W':
                doType(VK_SHIFT, VK_W);
                break;
            case 'X':
                doType(VK_SHIFT, VK_X);
                break;
            case 'Y':
                doType(VK_SHIFT, VK_Y);
                break;
            case 'Z':
                doType(VK_SHIFT, VK_Z);
                break;
            case '`':
                doType(VK_BACK_QUOTE);
                break;
            case '0':
                doType(VK_0);
                break;
            case '1':
                doType(VK_1);
                break;
            case '2':
                doType(VK_2);
                break;
            case '3':
                doType(VK_3);
                break;
            case '4':
                doType(VK_4);
                break;
            case '5':
                doType(VK_5);
                break;
            case '6':
                doType(VK_6);
                break;
            case '7':
                doType(VK_7);
                break;
            case '8':
                doType(VK_8);
                break;
            case '9':
                doType(VK_9);
                break;
            case '-':
                doType(VK_MINUS);
                break;
            case '=':
                doType(VK_EQUALS);
                break;
            case '~':
                doType(VK_SHIFT, VK_BACK_QUOTE);
                break;
            case '!':
                doType(VK_EXCLAMATION_MARK);
                break;
            case '@':
                doType(VK_AT);
                break;
            case '#':
                doType(VK_NUMBER_SIGN);
                break;
            case '$':
                doType(VK_DOLLAR);
                break;
            case '%':
                doType(VK_SHIFT, VK_5);
                break;
            case '^':
                doType(VK_CIRCUMFLEX);
                break;
            case '&':
                doType(VK_AMPERSAND);
                break;
            case '*':
                doType(VK_ASTERISK);
                break;
            case '(':
                doType(VK_LEFT_PARENTHESIS);
                break;
            case ')':
                doType(VK_RIGHT_PARENTHESIS);
                break;
            case '_':
                doType(VK_UNDERSCORE);
                break;
            case '+':
                doType(VK_PLUS);
                break;
            case '\t':
                doType(VK_TAB);
                break;
            case '\n':
                doType(VK_ENTER);
                break;
            case '[':
                doType(VK_OPEN_BRACKET);
                break;
            case ']':
                doType(VK_CLOSE_BRACKET);
                break;
            case '\\':
                doType(VK_BACK_SLASH);
                break;
            case '{':
                doType(VK_SHIFT, VK_OPEN_BRACKET);
                break;
            case '}':
                doType(VK_SHIFT, VK_CLOSE_BRACKET);
                break;
            case '|':
                doType(VK_SHIFT, VK_BACK_SLASH);
                break;
            case ';':
                doType(VK_SEMICOLON);
                break;
            case ':':
                doType(VK_COLON);
                break;
            case '\'':
                doType(VK_QUOTE);
                break;
            case '"':
                doType(VK_QUOTEDBL);
                break;
            case ',':
                doType(VK_COMMA);
                break;
            case '<':
                doType(VK_LESS);
                break;
            case '.':
                doType(VK_PERIOD);
                break;
            case '>':
                doType(VK_GREATER);
                break;
            case '/':
                doType(VK_SLASH);
                break;
            case '?':
                doType(VK_SHIFT, VK_SLASH);
                break;
            case ' ':
                doType(VK_SPACE);
                break;
            default:
                throw new IllegalArgumentException("Cannot type character " + character);
        }
    }

    private void doType(int... keyCodes) {
        doType(keyCodes, 0, keyCodes.length);
    }

    private void doType(int[] keyCodes, int offset, int length) {
        if (length == 0) {
            return;
        }

        robot.keyPress(keyCodes[offset]);
        doType(keyCodes, offset + 1, length - 1);
        robot.keyRelease(keyCodes[offset]);
        //Now that it is all typed out, lets send it.
        robot.keyPress(VK_ENTER);
        robot.keyRelease(VK_ENTER);
    }

    /*
     * Moves to location specified at useHere, then right clicks over the character, at that location.
     */
    void useAt(Point useHere) {
        setPoint(useHere);
        moveTo(useHere);
        robot.mouseMove(Locations.returnCenterScreen().x, Locations.returnCenterScreen().y);
        robot.mousePress(MouseEvent.BUTTON3_MASK);
        robot.mouseRelease(MouseEvent.BUTTON3_MASK);
    }
}
