package tankgame;


import tankgame.game.TRE;
import tankgame.menus.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class Launcher {

    /*
     * Main panel in JFrame, the layout of this panel
     * will be card layout, this will allow us to switch
     * to sub-panels depending on game state.
     */
    private JPanel mainPanel;
    /*
     * start panel will be used to view the start menu. It will contain
     * two buttons start and exit.
     */
    private JPanel startPanel, helpPanel, controls;
    /*
     * game panel is used to show our game to the screen. inside this panel
     * also contains the game loop. This is where out objects are updated and
     * redrawn. This panel will execute its game loop on a separate thread.
     * This is to ensure responsiveness of the GUI. It is also a bad practice to
     * run long running loops(or tasks) on Java Swing's main thread. This thread is
     * called the event dispatch thread.
     */
    private TRE gamePanel;
    /*
     * end panel is used to show the end game panel.  it will contain
     * two buttons restart and exit.
     */
    public JPanel endPanel, restartPanel;
    /*
     * JFrame used to store our main panel. We will also attach all event
     * listeners to this JFrame.
     */
    private JFrame jf;
    /*
     * CardLayout is used to manage our sub-panels. This is a layout manager
     * used for our game. It will be attached to the main panel.
     */
    private CardLayout cl;

    public Launcher(){
        jf = new JFrame();             // creating a new JFrame object
        jf.setTitle("Tank Wars Game"); // setting the title of the JFrame window.
        jf.setLocationRelativeTo(null); // this will open the window in the center of the screen.
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // when the GUI is closed, this will also shutdown the VM
    }

    private void initUIComponents(){
        mainPanel = new JPanel(); // create a new main panel
        startPanel = new StartMenuPanel(this); // create a new start panel
        gamePanel = new TRE(this); // create a new game panel
        gamePanel.gameInitialize(); // initialize game, but DO NOT start game
        endPanel = new GameOverPanel(this); // create a new end game pane;
        restartPanel = new EndGamePanel(this);
        helpPanel = new Help(this);
        controls = new ControlsPanel(this);
        cl = new CardLayout(); // creating a new CardLayout Panel
        jf.setResizable(false); //make the JFrame not resizable
        mainPanel.setLayout(cl); // set the layout of the main panel to our card layout
        mainPanel.add(startPanel, "start"); //add the start panel to the main panel
        mainPanel.add(gamePanel, "game");   //add the game panel to the main panel
        mainPanel.add(endPanel, "end");    // add the end game panel to the main panel
        mainPanel.add(restartPanel, "restart");    // add the end game panel to the main panel
        mainPanel.add(helpPanel, "help");    // add the end game panel to the main panel
        mainPanel.add(controls, "controls");    // add the end game panel to the main panel
        jf.add(mainPanel); // add the main panel to the JFrame
        setFrame("start"); // set the current panel to start panel
    }

    public void setFrame(String type){
        jf.setVisible(false); // hide the JFrame
        switch(type){
            case "start":
                // set the size of the jFrame to the expected size for the start panel
                jf.setSize(GameConstants.START_MENU_SCREEN_WIDTH,GameConstants.START_MENU_SCREEN_HEIGHT);
                break;
            case "game":
                // set the size of the jFrame to the expected size for the game panel
                jf.setSize(GameConstants.GAME_SCREEN_WIDTH,GameConstants.GAME_SCREEN_HEIGHT);
                //start a new thread for the game to run. This will ensure our JFrame is responsive and
                // not stuck executing the game loop.
                (new Thread(gamePanel)).start();
                break;
            case "restart":
                jf.setSize(GameConstants.END_MENU_SCREEN_WIDTH,GameConstants.END_MENU_SCREEN_HEIGHT);
                break;
            case "end":
                // set the size of the jFrame to the expected size for the end panel
                jf.setSize(GameConstants.END_MENU_SCREEN_WIDTH,GameConstants.END_MENU_SCREEN_HEIGHT);
                break;
            case "help":
                jf.setSize(1024, 768);
                break;
            case "controls":
                jf.setSize(1024, 768);
                break;

        }
        cl.show(mainPanel, type); // change current panel shown on main panel tp the panel denoted by type.
        jf.setVisible(true); // show the JFrame
    }


    public JFrame getJf() {
        return jf;
    }

    public void closeGame(){
        jf.dispatchEvent(new WindowEvent(jf, WindowEvent.WINDOW_CLOSING));
    }


    public static void main(String[] args) {
        Launcher launch = new Launcher();
        launch.initUIComponents();
    }


}
