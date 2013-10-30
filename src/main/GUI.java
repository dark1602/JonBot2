package main;

import Controller.Controller;
import MemoryAccess.Locations;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFileChooser;
import com.melloware.jintellitype.*;
import java.awt.event.KeyEvent;

public class GUI extends javax.swing.JFrame implements HotkeyListener {

    //--------------------------------------------------------------------------Controller for everything besides GUI
    Controller controller;
    //--------------------------------------------------------------------------Saving/Loading Objects
    File fileDir = new File(System.getProperty("user.home") + "\\Documents\\");
    File file = new File(fileDir, "JonBot2Settings.txt");
    BufferedReader reader;
    BufferedWriter writer;
    //--------------------------------------------------------------------------Cavebot Script Objects
    private final javax.swing.JFileChooser fileChooser = new JFileChooser();
    //--------------------------------------------------------------------------Local robot for various tasks.
    Robot robot;
    //--------------------------------------------------------------------------Timers for automation.
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        private PointerInfo pointer;
        private Point coord;

        @Override
        public void run() {
            pointer = MouseInfo.getPointerInfo();
            coord = pointer.getLocation();

            colorRGB.setText("" + robot.getPixelColor(coord.x, coord.y).getRGB());
        }
    };

    public GUI() {
        try {
            this.robot = new Robot();
            initComponents();

            //Load settings if they exist
            if (file.canRead()) {
                autoLoad();
            }

            //start timer for color identifier
            timer.scheduleAtFixedRate(task, 0, 1);

            //------------------------------------------------------------------Set jintellitype variables
            int bitType = Integer.valueOf(System.getProperty("sun.arch.data.model"));
            if (bitType == 32) {
                JIntellitype.setLibraryLocation("C:\\Windows\\System32\\JIntellitype.dll");
                System.loadLibrary("JIntellitype");

            }
            if (bitType == 64) {
                JIntellitype.setLibraryLocation("C:\\Windows\\System32\\JIntellitype64.dll");
                System.loadLibrary("JIntellitype64");
            }
            if (bitType != 32 && bitType != 64) {
                JIntellitype.setLibraryLocation("C:\\Windows\\System32\\JIntellitype.dll");
                System.loadLibrary("JIntellitype");
            }
            //------------------------------------------------------------------Add Jintellitype hotkeys
            JIntellitype.getInstance().registerHotKey(1, 0, KeyEvent.VK_PAUSE);
            JIntellitype.getInstance().registerHotKey(2, 0, 45);//Insert key            
            JIntellitype.getInstance().addHotKeyListener((HotkeyListener) this);
        } catch (AWTException ex) {
            System.out.println("Couldnt start robot for some reason.");
        }
    }

    //--------------------------------------------------------------------------listen for hotkey
    @Override
    public void onHotKey(int aIdentifier) {
        if (aIdentifier == 1) {//pause key                                                        
            if (!pauseCheck.isSelected()) {
                pauseCheck.setSelected(true);
            } else {
                pauseCheck.setSelected(false);
            }
        }
        if (aIdentifier == 2) {//insert key
            //System.out.println("works");
            try {
                controller.toggleCavebotPause();
            } catch (NullPointerException e) {
                System.out.println("Cant pause cavebot if it isn't started");
            }
        }
        if (aIdentifier == 3) {//delete key
            scriptArea.append("MOVE," + Locations.returnPlayerX() + "," + Locations.returnPlayerY() + "," + Locations.returnPlayerZ() + "\n");
        }
    }

    /*
     * Saves all current settings to file JonBot2Settings in the users
     * My Documents folder
     */
    public void save() {
        try {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                fileDir.mkdirs();
            }
            try {
                writer = new BufferedWriter(new FileWriter(file));
            } catch (IOException ex) {
                System.out.println("Can't write the save to the location for some reason.");
            }
            //now to write everything to the file.
            writer.write(hpHighBox.getText() + "\n");
            writer.write(hpLowBox.getText() + "\n");
            writer.write(manaBox.getText() + "\n");
            writer.write(charXBox.getText() + "\n");
            writer.write(charYBox.getText() + "\n");
            writer.write(charZBox.getText() + "\n");
            writer.write(charIDBox.getText() + "\n");
            writer.write(paperDollX.getText() + "\n");
            writer.write(paperDollY.getText() + "\n");
            writer.write(charStatusX.getText() + "\n");
            writer.write(charStatusY.getText() + "\n");
            writer.write(gameWindowX.getText() + "\n");
            writer.write(gameWindowY.getText() + "\n");
            writer.write(gameWindowWidth.getText() + "\n");
            writer.write(currentTargetID.getText() + "\n");
            writer.write(PID.getText() + "\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            System.out.println("Couldnt write save to file");
        }
    }

    /*
     * If the settings file exists, it will attempt to load settings.
     */
    public void autoLoad() {
        try {
            reader = new BufferedReader(new FileReader(file.getAbsolutePath()));

            hpHighBox.setText(reader.readLine());
            hpLowBox.setText(reader.readLine());
            manaBox.setText(reader.readLine());
            charXBox.setText(reader.readLine());
            charYBox.setText(reader.readLine());
            charZBox.setText(reader.readLine());
            charIDBox.setText(reader.readLine());
            paperDollX.setText(reader.readLine());
            paperDollY.setText(reader.readLine());
            charStatusX.setText(reader.readLine());
            charStatusY.setText(reader.readLine());
            gameWindowX.setText(reader.readLine());
            gameWindowY.setText(reader.readLine());
            gameWindowWidth.setText(reader.readLine());
            currentTargetID.setText(reader.readLine());
            PID.setText(reader.readLine());
            reader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("File doesnt exist?");
        } catch (IOException ex) {
            System.out.println("Couldnt read from file for some reason during loading");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        healingTab = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        hpHighBox = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        hpLowBox = new javax.swing.JTextField();
        hpHighCheck = new javax.swing.JCheckBox();
        hpLowCheck = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        manaBox = new javax.swing.JTextField();
        manaCheck = new javax.swing.JCheckBox();
        spellCheck = new javax.swing.JCheckBox();
        startButton = new javax.swing.JButton();
        pauseCheck = new javax.swing.JCheckBox();
        potionCheck = new javax.swing.JCheckBox();
        hotKeySelect = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        PID = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        pauseCavebotCheck = new javax.swing.JCheckBox();
        startCavebot = new javax.swing.JButton();
        huntingTab = new javax.swing.JPanel();
        handSelect = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        arrowCheck = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        backpackSelect = new javax.swing.JComboBox();
        lootCheck = new javax.swing.JCheckBox();
        trainingTab = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        manaCostBox = new javax.swing.JTextField();
        spellTrainerCheck = new javax.swing.JCheckBox();
        colorRGB = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        playerAlertCheck = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        foodCheck = new javax.swing.JCheckBox();
        cavebotTab = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        scriptArea = new javax.swing.JTextArea();
        moveCenterButton = new javax.swing.JButton();
        moveNorthButton = new javax.swing.JButton();
        moveSouthButton = new javax.swing.JButton();
        moveWestButton = new javax.swing.JButton();
        moveEastButton = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        useButton = new javax.swing.JButton();
        sayButton = new javax.swing.JButton();
        sayBox = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        pauseAttackButton = new javax.swing.JButton();
        unPauseAttack = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        addressTab = new javax.swing.JPanel();
        characterAddressTab = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        charXBox = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        charYBox = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        charZBox = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        charIDBox = new javax.swing.JTextField();
        clientAddressTab = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        paperDollX = new javax.swing.JTextField();
        paperDollY = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        charStatusX = new javax.swing.JTextField();
        charStatusY = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        gameWindowX = new javax.swing.JTextField();
        gameWindowY = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        gameWindowWidth = new javax.swing.JTextField();
        monsterAddressTab = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        currentTargetID = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        saveMenu = new javax.swing.JMenuItem();
        saveScriptMenu = new javax.swing.JMenuItem();
        loadScriptMenu = new javax.swing.JMenuItem();
        exitMenu = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        aboutMenu = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JonBot2");
        setResizable(false);

        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        jLabel2.setText("High HP %");

        hpHighBox.setText("68");
        hpHighBox.setAutoscrolls(false);

        jLabel3.setText("Low HP %");

        hpLowBox.setText("40");
        hpLowBox.setAutoscrolls(false);

        hpHighCheck.setText("Enabled");

        hpLowCheck.setText("Enabled");

        jLabel4.setText("Mana %");

        manaBox.setText("40");
        manaBox.setAutoscrolls(false);

        manaCheck.setText("Enabled");

        spellCheck.setText("Use Spells");

        startButton.setText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        pauseCheck.setSelected(true);
        pauseCheck.setText("Paused");
        pauseCheck.setEnabled(false);

        potionCheck.setText("Use Potions");

        hotKeySelect.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10", "F11", "F12" }));

        jLabel8.setText("Healing Spell Hotkey");

        jLabel11.setText("PID");

        PID.setText("19376");

        jLabel27.setText("Healing");

        jLabel31.setText("Cavebot");

        pauseCavebotCheck.setSelected(true);
        pauseCavebotCheck.setText("Paused");
        pauseCavebotCheck.setEnabled(false);

        startCavebot.setText("Start");
        startCavebot.setEnabled(false);
        startCavebot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startCavebotActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout healingTabLayout = new javax.swing.GroupLayout(healingTab);
        healingTab.setLayout(healingTabLayout);
        healingTabLayout.setHorizontalGroup(
            healingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(healingTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(healingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(healingTabLayout.createSequentialGroup()
                        .addGroup(healingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(healingTabLayout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                                .addGap(160, 160, 160))
                            .addGroup(healingTabLayout.createSequentialGroup()
                                .addGroup(healingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(healingTabLayout.createSequentialGroup()
                                        .addComponent(hpHighBox, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(hpHighCheck))
                                    .addGroup(healingTabLayout.createSequentialGroup()
                                        .addComponent(hpLowBox, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(hpLowCheck))
                                    .addComponent(jLabel4)
                                    .addGroup(healingTabLayout.createSequentialGroup()
                                        .addComponent(manaBox, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(manaCheck))
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(healingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(healingTabLayout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(hotKeySelect, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(healingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel31)
                                .addGroup(healingTabLayout.createSequentialGroup()
                                    .addComponent(jLabel27)
                                    .addGap(27, 27, 27)
                                    .addGroup(healingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(pauseCheck, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, healingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(pauseCavebotCheck)
                                            .addComponent(startButton)))))))
                    .addGroup(healingTabLayout.createSequentialGroup()
                        .addGroup(healingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(potionCheck)
                            .addComponent(spellCheck))
                        .addGroup(healingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(healingTabLayout.createSequentialGroup()
                                .addGap(87, 87, 87)
                                .addComponent(jLabel11)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(healingTabLayout.createSequentialGroup()
                                .addGap(73, 73, 73)
                                .addComponent(PID, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(startCavebot, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        healingTabLayout.setVerticalGroup(
            healingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(healingTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(healingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(healingTabLayout.createSequentialGroup()
                        .addGroup(healingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(hpHighBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(hpHighCheck))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(healingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(hpLowBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(hpLowCheck)))
                    .addGroup(healingTabLayout.createSequentialGroup()
                        .addGroup(healingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(hotKeySelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addGap(49, 49, 49)))
                .addComponent(jLabel4)
                .addGroup(healingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(healingTabLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(healingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(manaBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(manaCheck))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, healingTabLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(healingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pauseCheck)
                            .addComponent(jLabel27))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(startButton)
                        .addGap(18, 18, 18)))
                .addGroup(healingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(potionCheck)
                    .addComponent(jLabel11)
                    .addComponent(jLabel31)
                    .addComponent(pauseCavebotCheck))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(healingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spellCheck)
                    .addComponent(PID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startCavebot))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Healing", healingTab);

        handSelect.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Left Hand", "Right Hand"}));

        jLabel5.setText("Arrow Hand");

        arrowCheck.setText("Restock Arrows");
        arrowCheck.setEnabled(false);

        jLabel6.setText("Loot Backpack");

        backpackSelect.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Backpack 4","Backpack 5","Backpack 6"}));

        lootCheck.setText("Autoloot");
        lootCheck.setEnabled(false);

        javax.swing.GroupLayout huntingTabLayout = new javax.swing.GroupLayout(huntingTab);
        huntingTab.setLayout(huntingTabLayout);
        huntingTabLayout.setHorizontalGroup(
            huntingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(huntingTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(huntingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addGroup(huntingTabLayout.createSequentialGroup()
                        .addGroup(huntingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(handSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(backpackSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(70, 70, 70)
                        .addGroup(huntingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lootCheck)
                            .addComponent(arrowCheck))))
                .addContainerGap(194, Short.MAX_VALUE))
        );
        huntingTabLayout.setVerticalGroup(
            huntingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(huntingTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(huntingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(handSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(arrowCheck))
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(huntingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backpackSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lootCheck))
                .addContainerGap(141, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Hunting", huntingTab);

        jLabel1.setText("Spell Mana Cost");

        manaCostBox.setText("0");

        spellTrainerCheck.setText("Spell Training Enabled");

        colorRGB.setEditable(false);
        colorRGB.setText("Color is this        ");
        colorRGB.setFocusable(false);
        colorRGB.setOpaque(false);
        colorRGB.setRequestFocusEnabled(false);
        colorRGB.setVerifyInputWhenFocusTarget(false);

        jLabel32.setText("Player Alert");

        playerAlertCheck.setText("Enabled");
        playerAlertCheck.setEnabled(false);

        jLabel9.setText("Eat Food");

        foodCheck.setText("Enabled");

        javax.swing.GroupLayout trainingTabLayout = new javax.swing.GroupLayout(trainingTab);
        trainingTab.setLayout(trainingTabLayout);
        trainingTabLayout.setHorizontalGroup(
            trainingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(trainingTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(trainingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(trainingTabLayout.createSequentialGroup()
                        .addGroup(trainingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(manaCostBox))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spellTrainerCheck)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 113, Short.MAX_VALUE)
                        .addGroup(trainingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel32)
                            .addComponent(jLabel9)
                            .addComponent(playerAlertCheck)
                            .addComponent(foodCheck)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, trainingTabLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(colorRGB, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        trainingTabLayout.setVerticalGroup(
            trainingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(trainingTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(trainingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel32))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(trainingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(manaCostBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spellTrainerCheck)
                    .addComponent(playerAlertCheck))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(foodCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 122, Short.MAX_VALUE)
                .addComponent(colorRGB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Training", trainingTab);

        scriptArea.setColumns(20);
        scriptArea.setRows(5);
        scriptArea.setWrapStyleWord(true);
        jScrollPane1.setViewportView(scriptArea);

        moveCenterButton.setText("Center");
        moveCenterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveCenterButtonActionPerformed(evt);
            }
        });

        moveNorthButton.setText("North");
        moveNorthButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveNorthButtonActionPerformed(evt);
            }
        });

        moveSouthButton.setText("South");
        moveSouthButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveSouthButtonActionPerformed(evt);
            }
        });

        moveWestButton.setText("West");
        moveWestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveWestButtonActionPerformed(evt);
            }
        });

        moveEastButton.setText("East");
        moveEastButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveEastButtonActionPerformed(evt);
            }
        });

        jLabel7.setText("Movement");

        useButton.setText("Use");
        useButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                useButtonActionPerformed(evt);
            }
        });

        sayButton.setText("Say");
        sayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sayButtonActionPerformed(evt);
            }
        });

        sayBox.setText("Hello");

        jButton1.setText("Delete Last");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        pauseAttackButton.setText("Pause");
        pauseAttackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseAttackButtonActionPerformed(evt);
            }
        });

        unPauseAttack.setText("Unpause");
        unPauseAttack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unPauseAttackActionPerformed(evt);
            }
        });

        jLabel10.setText("Attack:");

        javax.swing.GroupLayout cavebotTabLayout = new javax.swing.GroupLayout(cavebotTab);
        cavebotTab.setLayout(cavebotTabLayout);
        cavebotTabLayout.setHorizontalGroup(
            cavebotTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cavebotTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cavebotTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(cavebotTabLayout.createSequentialGroup()
                        .addGroup(cavebotTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(moveWestButton)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(cavebotTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(moveCenterButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(moveNorthButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(moveSouthButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(cavebotTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(cavebotTabLayout.createSequentialGroup()
                                .addGroup(cavebotTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(moveEastButton)
                                    .addComponent(useButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                                .addGroup(cavebotTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(cavebotTabLayout.createSequentialGroup()
                                        .addComponent(sayButton, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(sayBox, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(cavebotTabLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pauseAttackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(unPauseAttack, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        cavebotTabLayout.setVerticalGroup(
            cavebotTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cavebotTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cavebotTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(cavebotTabLayout.createSequentialGroup()
                        .addGroup(cavebotTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(moveNorthButton)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(cavebotTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(moveCenterButton)
                            .addComponent(moveWestButton)
                            .addComponent(moveEastButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(cavebotTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(moveSouthButton)
                            .addComponent(useButton)))
                    .addGroup(cavebotTabLayout.createSequentialGroup()
                        .addGroup(cavebotTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pauseAttackButton)
                            .addComponent(unPauseAttack)
                            .addComponent(jLabel10))
                        .addGroup(cavebotTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(cavebotTabLayout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(jButton1))
                            .addGroup(cavebotTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(sayBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(sayButton)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("CaveBot", cavebotTab);

        jLabel14.setText("Char X");

        charXBox.setText("06B5E5F8");

        jLabel15.setText("Char Y");

        charYBox.setText("023F069C");

        jLabel16.setText("Char Z");

        charZBox.setText("023F06A0");

        jLabel30.setText("Character ID");

        charIDBox.setText("06F92410");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel30)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(charIDBox, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(charXBox, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(34, 34, 34)))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(charYBox, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(charZBox, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 82, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(charXBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(charYBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(charZBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(charIDBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(129, Short.MAX_VALUE))
        );

        characterAddressTab.addTab("Character", jPanel1);

        jLabel21.setText("Paperdoll X");

        jLabel22.setText("Paperdoll Y");

        paperDollX.setText("003881B0");

        paperDollY.setText("003881B4");

        jLabel23.setText("Character Status X");

        jLabel24.setText("Character Status Y");

        charStatusX.setText("00388238");

        charStatusY.setText("0038823C");

        jLabel28.setText("Game Window X");

        jLabel29.setText("Game Window Y");

        gameWindowX.setText("0701A540");

        gameWindowY.setText("0701A544");

        jLabel17.setText("Game Window Width");

        gameWindowWidth.setText("0704F1FC");

        javax.swing.GroupLayout clientAddressTabLayout = new javax.swing.GroupLayout(clientAddressTab);
        clientAddressTab.setLayout(clientAddressTabLayout);
        clientAddressTabLayout.setHorizontalGroup(
            clientAddressTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clientAddressTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(clientAddressTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(paperDollX, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(charStatusX, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(gameWindowX, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53)
                .addGroup(clientAddressTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(charStatusY, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24)
                    .addComponent(paperDollY, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, clientAddressTabLayout.createSequentialGroup()
                        .addGroup(clientAddressTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(gameWindowY, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                        .addGroup(clientAddressTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(gameWindowWidth, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))))
                .addGap(23, 23, 23))
        );
        clientAddressTabLayout.setVerticalGroup(
            clientAddressTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clientAddressTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(clientAddressTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(clientAddressTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(paperDollX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paperDollY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(clientAddressTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(clientAddressTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(charStatusX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(charStatusY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(clientAddressTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(clientAddressTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gameWindowX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gameWindowY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(gameWindowWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(83, Short.MAX_VALUE))
        );

        paperDollY.getAccessibleContext().setAccessibleName("");
        gameWindowWidth.getAccessibleContext().setAccessibleName("");

        characterAddressTab.addTab("Client", clientAddressTab);

        jLabel20.setText("Current Target ID");

        currentTargetID.setText("07028668");

        javax.swing.GroupLayout monsterAddressTabLayout = new javax.swing.GroupLayout(monsterAddressTab);
        monsterAddressTab.setLayout(monsterAddressTabLayout);
        monsterAddressTabLayout.setHorizontalGroup(
            monsterAddressTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(monsterAddressTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(monsterAddressTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(monsterAddressTabLayout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addContainerGap(302, Short.MAX_VALUE))
                    .addGroup(monsterAddressTabLayout.createSequentialGroup()
                        .addComponent(currentTargetID, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        monsterAddressTabLayout.setVerticalGroup(
            monsterAddressTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(monsterAddressTabLayout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currentTargetID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(83, Short.MAX_VALUE))
        );

        characterAddressTab.addTab("Monsters", monsterAddressTab);

        javax.swing.GroupLayout addressTabLayout = new javax.swing.GroupLayout(addressTab);
        addressTab.setLayout(addressTabLayout);
        addressTabLayout.setHorizontalGroup(
            addressTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(characterAddressTab)
        );
        addressTabLayout.setVerticalGroup(
            addressTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(characterAddressTab)
        );

        jTabbedPane1.addTab("Addresses", addressTab);

        jMenu1.setText("File");

        saveMenu.setText("Save Settings");
        saveMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuActionPerformed(evt);
            }
        });
        jMenu1.add(saveMenu);

        saveScriptMenu.setText("Save Script");
        saveScriptMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveScriptMenuActionPerformed(evt);
            }
        });
        jMenu1.add(saveScriptMenu);

        loadScriptMenu.setText("Load Script");
        loadScriptMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadScriptMenuActionPerformed(evt);
            }
        });
        jMenu1.add(loadScriptMenu);

        exitMenu.setText("Exit");
        jMenu1.add(exitMenu);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        aboutMenu.setText("About");
        jMenu2.add(aboutMenu);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        //start the controller that controls everything
        JIntellitype.getInstance().registerHotKey(3, 0, KeyEvent.VK_SUBTRACT);
        controller = Controller.getInstance();
        //controller.test();
        //hide this button, so this shit isnt done again.
        startButton.setVisible(false);
        startCavebot.setEnabled(true);
        //unpause the bot
        pauseCheck.setEnabled(true);
        pauseCheck.setSelected(false);
        
        //disable delete hotkey        
    }//GEN-LAST:event_startButtonActionPerformed

    private void moveCenterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveCenterButtonActionPerformed
        scriptArea.append("MOVE," + Locations.returnPlayerX() + "," + Locations.returnPlayerY() + "," + Locations.returnPlayerZ() + "\n");
    }//GEN-LAST:event_moveCenterButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        boolean updated = false;
        if (!scriptArea.getText().isEmpty() && scriptArea.getText().contains("\n")) {
            String[] tempArray = scriptArea.getText().split("\n");
            int x = 0;
            scriptArea.setText("");
            while (x < tempArray.length - 1) {
                scriptArea.append(tempArray[x] + "\n");
                x++;
            }
            updated = true;
        } else if (!scriptArea.getText().isEmpty() && !updated) {
            scriptArea.setText("");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void sayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sayButtonActionPerformed
        scriptArea.append("SAY," + sayBox.getText() + "\n");
    }//GEN-LAST:event_sayButtonActionPerformed

    private void moveWestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveWestButtonActionPerformed
        scriptArea.append("MOVE," + (Locations.returnPlayerX() - 1) + "," + Locations.returnPlayerY() + "," + Locations.returnPlayerZ() + "\n");
    }//GEN-LAST:event_moveWestButtonActionPerformed

    private void moveEastButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveEastButtonActionPerformed
        scriptArea.append("MOVE," + (Locations.returnPlayerX() + 1) + "," + Locations.returnPlayerY() + "," + Locations.returnPlayerZ() + "\n");
    }//GEN-LAST:event_moveEastButtonActionPerformed

    private void moveSouthButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveSouthButtonActionPerformed
        scriptArea.append("MOVE," + Locations.returnPlayerX() + "," + (Locations.returnPlayerY() + 1) + "," + Locations.returnPlayerZ() + "\n");
    }//GEN-LAST:event_moveSouthButtonActionPerformed

    private void moveNorthButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveNorthButtonActionPerformed
        scriptArea.append("MOVE," + Locations.returnPlayerX() + "," + (Locations.returnPlayerY() - 1) + "," + Locations.returnPlayerZ() + "\n");
    }//GEN-LAST:event_moveNorthButtonActionPerformed

    private void useButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useButtonActionPerformed
        scriptArea.append("USE," + Locations.returnPlayerX() + "," + Locations.returnPlayerY() + "," + Locations.returnPlayerZ() + "\n");
    }//GEN-LAST:event_useButtonActionPerformed

    private void loadScriptMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadScriptMenuActionPerformed
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                scriptArea.read(new FileReader(file.getAbsolutePath()), null);
            } catch (IOException ex) {
                System.out.println("problem accessing file" + file.getAbsolutePath());
            }
        } else {
            System.out.println("File access cancelled by user.");
        }
    }//GEN-LAST:event_loadScriptMenuActionPerformed

    private void saveScriptMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveScriptMenuActionPerformed
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(file));
                writer.write(scriptArea.getText());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                System.out.println("Couldnt save for some reason.");
            }
        }
    }//GEN-LAST:event_saveScriptMenuActionPerformed

    private void saveMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuActionPerformed
        save();
    }//GEN-LAST:event_saveMenuActionPerformed

    private void startCavebotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startCavebotActionPerformed
        controller.startCavebot();
        startCavebot.setVisible(false);
        JIntellitype.getInstance().unregisterHotKey(3);
    }//GEN-LAST:event_startCavebotActionPerformed

    private void pauseAttackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseAttackButtonActionPerformed
        scriptArea.append("PAUSEATTACK," + "\n");
    }//GEN-LAST:event_pauseAttackButtonActionPerformed

    private void unPauseAttackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unPauseAttackActionPerformed
        scriptArea.append("UNPAUSEATTACK," + "\n");
    }//GEN-LAST:event_unPauseAttackActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JTextField PID;
    private javax.swing.JMenuItem aboutMenu;
    private javax.swing.JPanel addressTab;
    public static javax.swing.JCheckBox arrowCheck;
    public static javax.swing.JComboBox backpackSelect;
    private javax.swing.JPanel cavebotTab;
    public static javax.swing.JTextField charIDBox;
    public static javax.swing.JTextField charStatusX;
    public static javax.swing.JTextField charStatusY;
    public static javax.swing.JTextField charXBox;
    public static javax.swing.JTextField charYBox;
    public static javax.swing.JTextField charZBox;
    private javax.swing.JTabbedPane characterAddressTab;
    private javax.swing.JPanel clientAddressTab;
    public static javax.swing.JTextField colorRGB;
    public static javax.swing.JTextField currentTargetID;
    private javax.swing.JMenuItem exitMenu;
    public static javax.swing.JCheckBox foodCheck;
    public static javax.swing.JTextField gameWindowWidth;
    public static javax.swing.JTextField gameWindowX;
    public static javax.swing.JTextField gameWindowY;
    public static javax.swing.JComboBox handSelect;
    private javax.swing.JPanel healingTab;
    public static javax.swing.JComboBox hotKeySelect;
    public static javax.swing.JTextField hpHighBox;
    public static javax.swing.JCheckBox hpHighCheck;
    public static javax.swing.JTextField hpLowBox;
    public static javax.swing.JCheckBox hpLowCheck;
    private javax.swing.JPanel huntingTab;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JMenuItem loadScriptMenu;
    public static javax.swing.JCheckBox lootCheck;
    public static javax.swing.JTextField manaBox;
    public static javax.swing.JCheckBox manaCheck;
    public static javax.swing.JTextField manaCostBox;
    private javax.swing.JPanel monsterAddressTab;
    private javax.swing.JButton moveCenterButton;
    private javax.swing.JButton moveEastButton;
    private javax.swing.JButton moveNorthButton;
    private javax.swing.JButton moveSouthButton;
    private javax.swing.JButton moveWestButton;
    public static javax.swing.JTextField paperDollX;
    public static javax.swing.JTextField paperDollY;
    private javax.swing.JButton pauseAttackButton;
    private javax.swing.JCheckBox pauseCavebotCheck;
    public static javax.swing.JCheckBox pauseCheck;
    public static javax.swing.JCheckBox playerAlertCheck;
    public static javax.swing.JCheckBox potionCheck;
    private javax.swing.JMenuItem saveMenu;
    private javax.swing.JMenuItem saveScriptMenu;
    private javax.swing.JTextField sayBox;
    private javax.swing.JButton sayButton;
    public static javax.swing.JTextArea scriptArea;
    public static javax.swing.JCheckBox spellCheck;
    public static javax.swing.JCheckBox spellTrainerCheck;
    public javax.swing.JButton startButton;
    private javax.swing.JButton startCavebot;
    private javax.swing.JPanel trainingTab;
    private javax.swing.JButton unPauseAttack;
    private javax.swing.JButton useButton;
    // End of variables declaration//GEN-END:variables
}
