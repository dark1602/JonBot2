package MemoryAccess;

import MemoryAccess.Kernel32;
import MemoryAccess.Module;
import MemoryAccess.PsapiTools;
import MemoryAccess.User32;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Memory;
import com.sun.jna.ptr.IntByReference;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.List;

/*
 * This class stores the relative location of all client objects
 * for easier access and updating using MemoryAccess tools.
 */
public class Locations {

    private static final boolean DEBUG = false;
    static MemoryAccess.Process zezenia = new MemoryAccess.Process(Integer.valueOf(main.GUI.PID.getText()), "Zezenia.exe");
    //--------------------------------------------------------------------------Client Objects Offsets
    static int[] statusXOffsets = {0x17E210, 0xa4, 0x358};
    static int[] statusYOffsets = {0x17E210, 0xa4, 0x35C};
    static int[] charXOffsets = {0x17E168, 0x8, 0x90, 0x58};
    static int[] charYOffsets = {0x17E168, 0x8, 0x90, 0x5c};
    static int[] charZOffsets = {0x17E168, 0x8, 0x90, 0x60};
    //--------------------------------------------------------------------------Method variables
    static long dynAddress;
    //--------------------------------------------------------------------------JNA Neccessities
    static Kernel32 kernel32 = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);
    static User32 user32 = (User32) Native.loadLibrary("user32", User32.class);
    static public int PROCESS_VM_READ = 0x0010;
    static public int PROCESS_VM_WRITE = 0x0020;
    static public int PROCESS_VM_OPERATION = 0x0008;
    static final int PROCESS_QUERY_INFORMATION = 0x0400;


    /*
     * Opens the process specified by pid and returns it as a pointer.
     */
    private static Pointer returnProcess() {
        Pointer process = kernel32.OpenProcess(PROCESS_VM_READ | PROCESS_VM_WRITE | PROCESS_VM_OPERATION | PROCESS_QUERY_INFORMATION, true, Integer.valueOf(main.GUI.PID.getText()));
        return process;
    }

    /*
     * Returns zezenias dynamically generated base address.
     */
    private static int getBaseAddress() {
        try {
            zezenia = new MemoryAccess.Process(Integer.valueOf(main.GUI.PID.getText()), "Zezenia.exe");
            Pointer hProcess = zezenia.getPointer();
            List<Module> hModules = PsapiTools.getInstance().EnumProcessModules(hProcess);

            for (Module m : hModules) {
                if (m.getFileName().contains(zezenia.getSzExeFile())) {
                    return Integer.valueOf("" + Pointer.nativeValue(m.getLpBaseOfDll()));
                }
            }
        } catch (Exception e) {
        }
        return -1;
    }

    /*
     * Returns the final address at the end of base+offsets in memory.
     */
    public static long findDynAddress(Pointer process, int[] offsets, long startHere) {

        long trueBase = startHere;

        int size = 4;
        Memory addressHolder = new Memory(size);
        long pointerAddress = 0;

        for (int i = 0; i < offsets.length; i++) {
            if (i == 0) {
                kernel32.ReadProcessMemory(process, trueBase, addressHolder, size, null);
            }

            pointerAddress = ((addressHolder.getInt(0) + offsets[i]));

            if (i != offsets.length - 1) {
                kernel32.ReadProcessMemory(process, pointerAddress, addressHolder, size, null);
            }
        }

        return pointerAddress;
    }

    /*
     * Reads the specified number of bytes in the specified memory location
     * of the specified process.
     */
    private static Memory readMemory(Pointer process, long address, int bytesToRead) {
        IntByReference read = new IntByReference(0);
        Memory output = new Memory(bytesToRead);

        kernel32.ReadProcessMemory(process, address, output, bytesToRead, read);
        return output;
    }

    /*
     * Writes the specified number of bytes at the specified memory location 
     * of the specified process.
     */
    private void writeMemory(Pointer process, long address, byte[] data) {
        int size = data.length;
        Memory toWrite = new Memory(size);

        for (int i = 0; i < size; i++) {
            toWrite.setByte(i, data[i]);
        }

        boolean b = kernel32.WriteProcessMemory(process, address, toWrite, size, null);
    }

    /*
     * Method soley for testing. I change it a lot depending on what I am
     * testing at the current time.
     */
    public static int test() {
        return 0;
    }


    /*=========================================================================
     * 
     *                  BELOW THIS POINT IS RETURN METHODS ONLY
     * 
     *                  BELOW THIS POINT IS RETURN METHODS ONLY
     * 
     *                  BELOW THIS POINT IS RETURN METHODS ONLY
     * 
     * ========================================================================
     */
    /*
     * Returns the location above the character, for mouse events.
     */
    public static Point returnCenterScreen() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        return new Point((int) (width / 2), (int) (height / 2)-40);
    }

    public static Point returnCoords() {
        long getX = Long.parseLong(main.GUI.charXBox.getText(), 16);
        long getY = Long.parseLong(main.GUI.charYBox.getText(), 16);
        int x = readMemory(returnProcess(), getX, 4).getInt(0);
        int y = readMemory(returnProcess(), getY, 4).getInt(0);
        Point result = new Point(x, y);
        return result;
    }
    /*
     * Returns Players X position
     */

    public static int returnPlayerX() {
        long getX = Long.parseLong(main.GUI.charXBox.getText(), 16);
        return readMemory(returnProcess(), getX, 4).getInt(0);
    }

    /*
     * Returns Players Y position
     */
    public static int returnPlayerY() {
        long getY = Long.parseLong(main.GUI.charYBox.getText(), 16);
        return readMemory(returnProcess(), getY, 4).getInt(0);
    }

    /*
     * Returns Players Z position
     */
    public static int returnPlayerZ() {
        long getZ = Long.parseLong(main.GUI.charZBox.getText(), 16);
        return readMemory(returnProcess(), getZ, 4).getInt(0);
    }

    /*
     * Return character status information X
     */
    public static int returnStatusX() {
        long getStatusX = Long.parseLong(main.GUI.charStatusX.getText(), 16);
        return readMemory(returnProcess(), getStatusX, 4).getInt(0);
    }

    /*
     * Return character status information Y
     */
    public static int returnStatusY() {
        long getStatusY = Long.parseLong(main.GUI.charStatusY.getText(), 16);
        return readMemory(returnProcess(), getStatusY, 4).getInt(0);
    }

    /*
     * Return paper doll window X
     */
    public static int returnPaperDollX() {
        long getPaperdollX = Long.parseLong(main.GUI.paperDollX.getText(), 16);
        return readMemory(returnProcess(), getPaperdollX, 4).getInt(0);
    }

    /*
     * Return paper doll window Y
     */
    public static int returnPaperDollY() {
        long getPaperdollY = Long.parseLong(main.GUI.paperDollY.getText(), 16);
        return readMemory(returnProcess(), getPaperdollY, 4).getInt(0);
    }

    /*
     * Return players identifier
     */
    public static int returnPlayerID() {
        long getPlayerID = Long.parseLong(main.GUI.charIDBox.getText(), 16);
        return readMemory(returnProcess(), getPlayerID, 4).getInt(0);
    }
    
    /*
     * Returns the current target ID
     */
    public static int returnCurrentTarget(){
        long getCurrentTarget = Long.parseLong(main.GUI.currentTargetID.getText(), 16);
        return readMemory(returnProcess(),getCurrentTarget,4).getInt(0);
    }
    
    /*
    Return game screen width
    */
    public static int returnClientWidth(){
        long getCurrentWidth = Long.parseLong(main.GUI.gameWindowWidth.getText(), 16);
        return readMemory(returnProcess(),getCurrentWidth,4).getInt(0);
    }
}
