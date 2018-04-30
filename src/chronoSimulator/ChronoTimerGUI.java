package chronoSimulator;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import java.awt.FlowLayout;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.SystemColor;
import java.awt.Font;
import javax.swing.border.BevelBorder;

import chronoTimer.ChronoTimer;

public class ChronoTimerGUI {
	
	private ChronoTimer ct;
	
	private boolean powerToggled = false;
	private boolean printerPower = false;
	private boolean activeRun = false;
	private boolean swapping = false;
	private boolean clearing = false;
	private boolean dnfing = false;
	private String numBuilder = ""; // default if user press # with no number
	
	private ArrayList<JRadioButton> connectChannels = new ArrayList<>();
	private ArrayList<JRadioButton> toggledChannels = new ArrayList<>();
	private ArrayList<JButton> trigButtons = new ArrayList<>();
	
	
	private String[] commands = {"RETURN", "NEWRUN", "ENDRUN", "EVENT","DNF", "CANCEL", "CLR",  "RESET" , "EXPORT" ,"PRINT"}; // pressing the "function" button will execute it
	private String[] eventOptions = {"IND","PARIND", "GRP"};
	private String currentCommand = "";
	private int commandIndex = 0;
	private int eventIndex = 0;
	//private JComboBox<String> commandComboBox = new JComboBox<String>(commands);
	
	
	// List of Commands to choose to perform
	
	
	// Frame and rootPanel
	private JFrame f;
	private JPanel upperPanel;
	
	// Power Panel - Top Left --------------------------------------------------
	private JPanel powerPanel;
	private JButton btnPower;
	
	// Sensor Panel - Top Middle --------------------------------------------------
	private JPanel sensorPanel;
	private JLabel lblNewLabel; // label for application title
	
	private JButton trig1;
	private JButton trig2;
	private JButton trig3;
	private JButton trig4;
	private JButton trig5;
	private JButton trig6;
	private JButton trig7;
	private JButton trig8;
	
	private JRadioButton enable1;
	private JRadioButton enable2;
	private JRadioButton enable3;
	private JRadioButton enable4;
	private JRadioButton enable5;
	private JRadioButton enable6;
	private JRadioButton enable7;
	private JRadioButton enable8;
	
	private JTextArea startText;
	private JTextArea startActive;
	private JTextArea finishText;
	private JTextArea finishActive;
	
	private JPanel startPanel;
	private JPanel startSensorPanel;
	private JPanel startActivePanel;
	private JPanel finishPanel;
	private JPanel finishSensorPanel;
	private JPanel finishActivePanel;
	
	// Printer Panel - Top Right --------------------------------------------------
	private JPanel printerPanel;
	private JPanel printerPowerPanel;
	
	private JButton btnPrinterPower;
	
	private JTextArea printerText;
	
	// NumberPad -  Bottom Right --------------------------------------------------
	private JPanel numPad;
	private JButton num1;
	private JButton num2;
	private JButton num3;
	private JButton num4;
	private JButton num5;
	private JButton num6;
	private JButton num7;
	private JButton num8;
	private JButton num9;
	private JButton num0;
	private JButton numPound;
	private JButton numStar;
	
	// Function Panel - Bottom Left ----------------------------------------------
	
	private JPanel functionPanel;
	private JPanel functionContainer;
	private JButton btnFunction;
	private JPanel arrowContainer;
	private JButton btnLeft;
	private JButton btnRight;
	private JButton btnDown;
	private JButton btnUp;
	private JPanel swapContainer;
	private JButton btnSwap;
	
	// Display Panel - Bottom Middle ----------------------------------------------
	
	private JPanel displayPanel;
	private JTextPane displayText;
	private JLabel displayLabel;
	
	// Lower Panel ----------------------------------------------
	
	private JPanel lowerPanel;
	
	// Channels - Lower Left ----------------------------------------------
	// Sensor list to choose from when connecting/disconnecting
	private String[] sensorTypes = {"EYE", "GATE", "PAD"}; // types of sensor to choose from
	private JComboBox<String> sensorComboBox = new JComboBox<String>(sensorTypes); // sensor drop box
	private JPanel channelPanel;
	private JPanel channelTextPanel;
	private JLabel chanLabel;
	private JPanel channelButtonPanel;
	private JPanel chanPanelUpper;
	private JLabel chanLabel1;
	private JLabel chanLabel3;
	private JLabel chanLabel5;
	private JLabel chanLabel7;
	private JRadioButton chan1;
	private JRadioButton chan3;
	private JRadioButton chan5;
	private JRadioButton chan7;
	private JPanel chanPanelLower;
	private JLabel chanLabel2;
	private JLabel chanLabel4;
	private JLabel chanLabel6;
	private JLabel chanLabel8;
	private JRadioButton chan2;
	private JRadioButton chan4;
	private JRadioButton chan6;
	private JRadioButton chan8;
	
	// USB Panel - Lower Middle ----------------------------------------------
	
	private JPanel usbPanel;
	private JLabel usbLabel;
	private Thread dispThread;
	
	public ChronoTimerGUI( ChronoTimer ct) {
		this.ct = ct;
		f = new JFrame("A JFrame");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.setTitle("FastWatch");
	    f.setSize(800, 800);
	    f.setLocation(300,200);
	    
	    f.setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));
	    f.getContentPane().setLayout(new BorderLayout(0, 0));
	    
	    upperPanel = new JPanel();
	    upperPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
	    upperPanel.setLayout(new GridLayout(2, 3, 1, 0));
	    f.getContentPane().add(upperPanel);
	    
	    // Power Panel - Top Left -------------------------------------------------------------------
	    
	    powerPanel = new JPanel();
	    btnPower = new JButton("POWER");
	    btnPower.addActionListener(new PowerClickListener());
	    powerPanel.add(btnPower);
	    upperPanel.add(powerPanel);
	    
	    // Sensor Panel - Top Middle -------------------------------------------------------------------
	    
	    sensorPanel = new JPanel();
	    upperPanel.add(sensorPanel);
	    sensorPanel.setLayout(new GridLayout(3, 1, 0, 0));
	    
	    lblNewLabel = new JLabel("Fastwatch Chronotimer");
	    lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    sensorPanel.add(lblNewLabel);
	    
	    startSensorPanel = new JPanel();
	    sensorPanel.add(startSensorPanel);
	    startSensorPanel.setLayout(new GridLayout(2, 1, 0, 0));
	    
	    startPanel = new JPanel();
	    startSensorPanel.add(startPanel);
	    startPanel.setLayout(new GridLayout(0, 5, 0, 0));
	    
	    startText = new JTextArea();
	    startText.setBackground(SystemColor.control);
	    startText.setText("Start");
	    startPanel.add(startText);
	    
	    trig1 = new JButton("1");
	    trig1.addActionListener(new TrigClickListener());
	    trig1.setActionCommand("trig 1");
	    startPanel.add(trig1);
	    
	    trig3 = new JButton("3");
	    trig3.addActionListener(new TrigClickListener());
	    trig3.setActionCommand("trig 3");
	    startPanel.add(trig3);
	    
	    trig5 = new JButton("5");
	    trig5.addActionListener(new TrigClickListener());
	    trig5.setActionCommand("trig 5");
	    startPanel.add(trig5);
	    
	    trig7 = new JButton("7");
	    trig7.addActionListener(new TrigClickListener());
	    trig7.setActionCommand("trig 7");
	    startPanel.add(trig7);
	    
	    startActivePanel = new JPanel();
	    startSensorPanel.add(startActivePanel);
	    startActivePanel.setLayout(new GridLayout(0, 5, 0, 0));
	    
	    startActive = new JTextArea();
	    startActive.setBackground(SystemColor.control);
	    startActive.setText("Enable/Disable");
	    startActivePanel.add(startActive);
	    
	    enable1 = new JRadioButton("");
	    enable1.setHorizontalAlignment(SwingConstants.CENTER);
	    enable1.addActionListener(new EnableClickListener());
	    enable1.setActionCommand("TOG 1");
	    enable3 = new JRadioButton("");
	    enable3.setHorizontalAlignment(SwingConstants.CENTER);
	    enable3.addActionListener(new EnableClickListener());
	    enable3.setActionCommand("TOG 3");
	    enable5 = new JRadioButton("");
	    enable5.setHorizontalAlignment(SwingConstants.CENTER);
	    enable5.addActionListener(new EnableClickListener());
	    enable5.setActionCommand("TOG 5");
	    enable7 = new JRadioButton("");
	    enable7.setHorizontalAlignment(SwingConstants.CENTER);
	    enable7.addActionListener(new EnableClickListener());
	    enable7.setActionCommand("TOG 7");
	    startActivePanel.add(enable1);
	    startActivePanel.add(enable3);
	    startActivePanel.add(enable5);
	    startActivePanel.add(enable7);
	    
	    finishSensorPanel = new JPanel();
	    sensorPanel.add(finishSensorPanel);
	    finishSensorPanel.setLayout(new GridLayout(2, 1, 0, 0));
	    
	    finishPanel = new JPanel();
	    finishSensorPanel.add(finishPanel);
	    finishPanel.setLayout(new GridLayout(0, 5, 0, 0));
	    
	    finishText = new JTextArea();
	    finishText.setBackground(SystemColor.control);
	    finishText.setText("Finish");
	    finishPanel.add(finishText);
	    
	    trig2 = new JButton("2");
	    trig2.addActionListener(new TrigClickListener());
	    trig2.setActionCommand("trig 2");
	    finishPanel.add(trig2);
	    
	    trig4 = new JButton("4");
	    trig4.addActionListener(new TrigClickListener());
	    trig4.setActionCommand("trig 4");
	    finishPanel.add(trig4);
	    
	    trig6 = new JButton("6");
	    trig6.addActionListener(new TrigClickListener());
	    trig6.setActionCommand("trig 6");
	    finishPanel.add(trig6);
	    
	    trig8 = new JButton("8");
	    trig8.addActionListener(new TrigClickListener());
	    trig8.setActionCommand("trig 8");
	    finishPanel.add(trig8);
	    
	    finishActivePanel = new JPanel();
	    finishSensorPanel.add(finishActivePanel);
	    finishActivePanel.setLayout(new GridLayout(0, 5, 0, 0));
	    
	    finishActive = new JTextArea();
	    finishActive.setBackground(SystemColor.control);
	    finishActive.setText("Enable/Disable");
	    finishActivePanel.add(finishActive);
	    
	    enable2 = new JRadioButton("");
	    enable2.setHorizontalAlignment(SwingConstants.CENTER);
	    enable2.addActionListener(new EnableClickListener());
	    enable2.setActionCommand("TOG 2");
	    finishActivePanel.add(enable2);
	    
	    enable4 = new JRadioButton("");
	    enable4.setHorizontalAlignment(SwingConstants.CENTER);
	    enable4.addActionListener(new EnableClickListener());
	    enable4.setActionCommand("TOG 4");
	    finishActivePanel.add(enable4);
	    
	    enable6 = new JRadioButton("");
	    enable6.setHorizontalAlignment(SwingConstants.CENTER);
	    enable6.addActionListener(new EnableClickListener());
	    enable6.setActionCommand("TOG 6");
	    finishActivePanel.add(enable6);
	    
	    enable8 = new JRadioButton("");
	    enable8.setHorizontalAlignment(SwingConstants.CENTER);
	    enable8.addActionListener(new EnableClickListener());
	    enable8.setActionCommand("TOG 8");
	    finishActivePanel.add(enable8);
	    
	    // Printer Panel - Top Right -------------------------------------------------------------------
	    
	    printerPanel = new JPanel();
	    upperPanel.add(printerPanel);
	    printerPanel.setLayout(new BorderLayout(5, 5));
	    
	    printerPowerPanel = new JPanel();
	    printerPanel.add(printerPowerPanel, BorderLayout.NORTH);
	    
	    btnPrinterPower = new JButton("PRINTER POWER");
	    btnPrinterPower.addActionListener(new PrinterPowerClickListener());
	    printerPowerPanel.add(btnPrinterPower);
	    btnPrinterPower.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    	}
	    });
	    
	    printerText = new JTextArea();
	    printerText.setEditable(false);
	    printerText.setText("Printer is off.\n");
	    JScrollPane scrollPane = new JScrollPane(printerText);
	    printerPanel.add(scrollPane, BorderLayout.CENTER);
	    
	    // Function Panel - Bottom Left -------------------------------------------------------------------
	    
	    functionPanel = new JPanel();
	    upperPanel.add(functionPanel);
	    functionPanel.setLayout(new GridLayout(4, 1, 5, 5));
	    
	    functionContainer = new JPanel();
	    FlowLayout flowLayout = (FlowLayout) functionContainer.getLayout();
	    flowLayout.setAlignment(FlowLayout.LEFT);
	    functionPanel.add(functionContainer);
	    /*
	    functionPanel.add(commandComboBox);
	    commandComboBox.setSize(100, 200);
	    commandComboBox.setSelectedIndex(0);
	    commandComboBox.setVisible(true);
	    commandComboBox.addActionListener(new ActionListener(){

	    	@Override
			public void actionPerformed(ActionEvent e) {
				String command = commandComboBox.getSelectedItem().toString();
				for(int i = 0; i < commands.length; i++){
					if(command.equalsIgnoreCase(commands[i])){
						commandComboBox.setSelectedIndex(i);
					}
				}
			}
	    	
	    });
	    */
	    
	    btnFunction = new JButton("FUNCTION");
	    functionContainer.add(btnFunction);
	    btnFunction.setEnabled(false);
	    btnFunction.addActionListener(new ActionListener(){
	    
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO
				numBuilder = "";
				if(!currentCommand.equals("RETURN")) {
					if(currentCommand.equalsIgnoreCase("EVENT")){
						ct.execute(getTime() + " " + currentCommand + " " + eventOptions[eventIndex]);
					}else if(currentCommand.equalsIgnoreCase("CLR")){
						printToDisplay("Enter Racer Num to CLR: \n(# to Confirm or * to Cancel)");
						numBuilder = "";
						clearing = true;
					}else if(currentCommand.equalsIgnoreCase("DNF")){
						printToDisplay("Enter Lane Num to DNF: \n(# to Confirm or * to Cancel)");
						numBuilder = "";
						dnfing = true;
					}else if(currentCommand.equalsIgnoreCase("NEWRUN")){
						activeRun = true;
						ct.execute(getTime() + " " + currentCommand);
					}else if(currentCommand.equalsIgnoreCase("ENDRUN")){
						activeRun = false;
						ct.execute(getTime() + " " + currentCommand);
					}else if(currentCommand.equalsIgnoreCase("RESET")){
						ct.execute(getTime() + " " + currentCommand);
						ct.execute(getTime() + " POWER");
						resetGUI();
					}else{
						ct.execute(getTime() + " " + currentCommand);//commandComboBox.getSelectedItem().toString());
					}	
				}				
				commandIndex = 0;
				eventIndex = 0;
				if(!clearing&&!dnfing&&!currentCommand.equalsIgnoreCase("RESET")) startThread("action");
				currentCommand = "";
			}
	    	
	    });
	    
	    arrowContainer = new JPanel();
	    FlowLayout flowLayout_2 = (FlowLayout) arrowContainer.getLayout();
	    flowLayout_2.setAlignment(FlowLayout.RIGHT);
	    
	    functionPanel.add(arrowContainer);
	    	    
	    btnLeft = new JButton(String.valueOf('\u25C0'));
	    btnLeft.addActionListener(new ArrowClickListener());
	    btnLeft.setActionCommand("left");
	    arrowContainer.add(btnLeft);
	    
	    btnRight = new JButton(String.valueOf('\u25B6'));
	    btnRight.addActionListener(new ArrowClickListener());
	    btnRight.setActionCommand("right");
	    arrowContainer.add(btnRight);
	    
	    btnDown = new JButton(String.valueOf('\u25BC'));
	    btnDown.addActionListener(new ArrowClickListener());
	    btnDown.setActionCommand("down");
	    arrowContainer.add(btnDown);
	    
	    btnUp = new JButton(String.valueOf('\u25B2'));
	    btnUp.addActionListener(new ArrowClickListener());
	    btnUp.setActionCommand("up");
	    arrowContainer.add(btnUp);
	    
	    swapContainer = new JPanel();
	    FlowLayout flowLayout_1 = (FlowLayout) swapContainer.getLayout();
	    flowLayout_1.setAlignment(FlowLayout.LEFT);
	    functionPanel.add(swapContainer);
	    
	    btnSwap = new JButton("SWAP");
	    swapContainer.add(btnSwap);
	    btnSwap.setEnabled(false);
	    btnSwap.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				killThread();
				clearing = dnfing = false;
				currentCommand = "";
				printToDisplay("Lane to swap runners: \n(# to Confirm or * to Cancel)");
				numBuilder= "";
				swapping = true;
			}
	    	
	    });
	    
	    btnLeft.setEnabled(false);
		btnRight.setEnabled(false);
		btnUp.setEnabled(false);
		btnDown.setEnabled(false);
	    
	    // Display Panel - Bottom Middle -------------------------------------------------------------------
	    
	    displayPanel = new JPanel();
	    upperPanel.add(displayPanel);
	    displayPanel.setLayout(new BorderLayout(5, 5));
	    
	    displayText = new JTextPane();
	    displayText.setText("");
	    displayText.setEditable(false);
	    displayPanel.add(displayText, BorderLayout.CENTER);
	    
	    displayLabel = new JLabel("Queue / Running / Final Time");
	    displayLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    displayLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
	    displayPanel.add(displayLabel, BorderLayout.SOUTH);
	    
	    // Number Panel - Bottom Left -------------------------------------------------------------------
	    
	    numPad = new JPanel();
	    upperPanel.add(numPad);
	    numPad.setLayout(new GridLayout(4, 3, 0, 0));
	    
	    //Number Pad
	    num1 = new JButton("1");
	    num1.addActionListener(new NumpadClickListener());
	    num1.setActionCommand("1");
	    numPad.add(num1);
	    
	    num2 = new JButton("2");
	    num2.addActionListener(new NumpadClickListener());
	    num2.setActionCommand("2");
	    numPad.add(num2);
	    
	    num3 = new JButton("3");
	    num3.addActionListener(new NumpadClickListener());
	    num3.setActionCommand("3");
	    numPad.add(num3);
	    
	    num4 = new JButton("4");
	    num4.addActionListener(new NumpadClickListener());
	    num4.setActionCommand("4");
	    numPad.add(num4);
	    
	    num5 = new JButton("5");
	    num5.addActionListener(new NumpadClickListener());
	    num5.setActionCommand("5");
	    numPad.add(num5);
	    
	    num6 = new JButton("6");
	    num6.addActionListener(new NumpadClickListener());
	    num6.setActionCommand("6");
	    numPad.add(num6);
	    
	    num7 = new JButton("7");
	    num7.addActionListener(new NumpadClickListener());
	    num7.setActionCommand("7");
	    numPad.add(num7);
	    
	    num8 = new JButton("8");
	    num8.addActionListener(new NumpadClickListener());
	    num8.setActionCommand("8");
	    numPad.add(num8);
	    
	    num9 = new JButton("9");
	    num9.addActionListener(new NumpadClickListener());
	    num9.setActionCommand("9");
	    numPad.add(num9);
	    
	    numPound = new JButton("#");
	    numPound.addActionListener(new NumpadClickListener());
	    numPound.setActionCommand("#");
	    numPad.add(numPound);
	    
	    num0 = new JButton("0");
	    num0.addActionListener(new NumpadClickListener());
	    num0.setActionCommand("0");
	    numPad.add(num0);
	    
	    numStar = new JButton("*");
	    numStar.addActionListener(new NumpadClickListener());
	    numStar.setActionCommand("*");
	    numPad.add(numStar);
	    
	    
	    
	    // Lower Panel for connections and usb -------------------------------------------------------------------
	    
	    lowerPanel = new JPanel();
	    lowerPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
	    f.getContentPane().add(lowerPanel, BorderLayout.SOUTH);
	    lowerPanel.setLayout(new GridLayout(0, 3, 0, 0));
	    
	    // Channel Connection - Lower Left -------------------------------------------------------------------
	    
	    channelPanel = new JPanel();
	    lowerPanel.add(channelPanel);
	    channelPanel.setLayout(new BorderLayout(0, 0));
	    
	    channelTextPanel = new JPanel();
	    channelPanel.add(channelTextPanel, BorderLayout.WEST);
	    
	    chanLabel = new JLabel("CHAN");
	    channelTextPanel.add(chanLabel);
	    
	    // add a drop box of sensor types
	    channelTextPanel.add(sensorComboBox);
	    sensorComboBox.addActionListener(new ActionListener(){
	    	
			@Override
			public void actionPerformed(ActionEvent e) {
				String sensor = sensorComboBox.getSelectedItem().toString();
				for(int i = 0; i < sensorTypes.length; i++){
					if(sensor.equalsIgnoreCase(sensorTypes[i])){
						sensorComboBox.setSelectedIndex(i);
					}
				}
			}
	    });
	    sensorComboBox.setSelectedIndex(0);
	    sensorComboBox.setVisible(true);
	    
	    channelButtonPanel = new JPanel();
	    channelPanel.add(channelButtonPanel, BorderLayout.CENTER);
	    channelButtonPanel.setLayout(new GridLayout(2, 0, 0, 0));
	    
	    chanPanelUpper = new JPanel();
	    channelButtonPanel.add(chanPanelUpper);
	    chanPanelUpper.setLayout(new GridLayout(2, 4, 0, 0));
	    
	    chanLabel1 = new JLabel("1");
	    chanLabel1.setHorizontalAlignment(SwingConstants.CENTER);
	    chanPanelUpper.add(chanLabel1);
	    
	    chanLabel3 = new JLabel("3");
	    chanLabel3.setHorizontalAlignment(SwingConstants.CENTER);
	    chanPanelUpper.add(chanLabel3);
	    
	    chanLabel5 = new JLabel("5");
	    chanLabel5.setHorizontalAlignment(SwingConstants.CENTER);
	    chanPanelUpper.add(chanLabel5);
	    
	    chanLabel7 = new JLabel("7");
	    chanLabel7.setHorizontalAlignment(SwingConstants.CENTER);
	    chanPanelUpper.add(chanLabel7);
	    
	    chan1 = new JRadioButton("");
	    chan1.setHorizontalAlignment(SwingConstants.CENTER);
	    //chan1.addActionListener(new ChannelClickListener());
	    chan1.setActionCommand("CONN " + sensorComboBox.getSelectedItem() + " 1");
	    chanPanelUpper.add(chan1);
	    
	    chan3 = new JRadioButton("");
	    chan3.setHorizontalAlignment(SwingConstants.CENTER);
	    //chan3.addActionListener(new ChannelClickListener());
	    chan3.setActionCommand("CONN " + sensorComboBox.getSelectedItem() + " 3");
	    chanPanelUpper.add(chan3);
	    
	    chan5 = new JRadioButton("");
	    chan5.setHorizontalAlignment(SwingConstants.CENTER);
	    //chan5.addActionListener(new ChannelClickListener());
	    chan5.setActionCommand("CONN " + sensorComboBox.getSelectedItem() + " 5");
	    chanPanelUpper.add(chan5);
	    
	    chan7 = new JRadioButton("");
	    chan7.setHorizontalAlignment(SwingConstants.CENTER);
	    //chan7.addActionListener(new ChannelClickListener());
	    chan7.setActionCommand("CONN " + sensorComboBox.getSelectedItem() + " 7");
	    chanPanelUpper.add(chan7);
	    
	    chanPanelLower = new JPanel();
	    channelButtonPanel.add(chanPanelLower);
	    chanPanelLower.setLayout(new GridLayout(2, 4, 0, 0));
	    
	    chanLabel2 = new JLabel("2");
	    chanLabel2.setHorizontalAlignment(SwingConstants.CENTER);
	    chanPanelLower.add(chanLabel2);
	    
	    chanLabel4 = new JLabel("4");
	    chanLabel4.setHorizontalAlignment(SwingConstants.CENTER);
	    chanPanelLower.add(chanLabel4);
	    
	    chanLabel6 = new JLabel("6");
	    chanLabel6.setHorizontalAlignment(SwingConstants.CENTER);
	    chanPanelLower.add(chanLabel6);
	    
	    chanLabel8 = new JLabel("8");
	    chanLabel8.setHorizontalAlignment(SwingConstants.CENTER);
	    chanPanelLower.add(chanLabel8);
	    
	    chan2 = new JRadioButton("");
	    chan2.setHorizontalAlignment(SwingConstants.CENTER);
	    //chan2.addActionListener(new ChannelClickListener());
	    chan2.setActionCommand("CONN " + sensorComboBox.getSelectedItem() + " 2");
	    chanPanelLower.add(chan2);
	    
	    chan4 = new JRadioButton("");
	    chan4.setHorizontalAlignment(SwingConstants.CENTER);
	   // chan4.addActionListener(new ChannelClickListener());
	    chan4.setActionCommand("CONN " + sensorComboBox.getSelectedItem() + " 4");
	    chanPanelLower.add(chan4);
	    
	    chan6 = new JRadioButton("");
	    chan6.setHorizontalAlignment(SwingConstants.CENTER);
	    //chan6.addActionListener(new ChannelClickListener());
	    chan6.setActionCommand("CONN " + sensorComboBox.getSelectedItem() + " 6");
	    chanPanelLower.add(chan6);
	    
	    chan8 = new JRadioButton("");
	    chan8.setHorizontalAlignment(SwingConstants.CENTER);
	    //chan8.addActionListener(new ChannelClickListener());
	    chan8.setActionCommand("CONN " + sensorComboBox.getSelectedItem() + " 8");
	    chanPanelLower.add(chan8);
	    
	    // Array list of connected channels
	    connectChannels.add(chan1);
	    connectChannels.add(chan2);
	    connectChannels.add(chan3);
	    connectChannels.add(chan4);
	    connectChannels.add(chan5);
	    connectChannels.add(chan6);
	    connectChannels.add(chan7);
	    connectChannels.add(chan8);
	    
	    // Array list of toggled channels
	    toggledChannels.add(enable1);
	    toggledChannels.add(enable2);
	    toggledChannels.add(enable3);
	    toggledChannels.add(enable4);
	    toggledChannels.add(enable5);
	    toggledChannels.add(enable6);
	    toggledChannels.add(enable7);
	    toggledChannels.add(enable8);
	    
	    // Array list of trigger buttons
	    trigButtons.add(trig1);
	    trigButtons.add(trig2);
	    trigButtons.add(trig3);
	    trigButtons.add(trig4);
	    trigButtons.add(trig5);
	    trigButtons.add(trig6);
	    trigButtons.add(trig7);
	    trigButtons.add(trig8);
	   
	    
	    //Disable all buttons until power is turned on
	    for(JRadioButton j: connectChannels){
	    	j.setEnabled(false);
	    	
	    	//use anonymous class for each radio to see if they are selected or deselected
	    	j.addItemListener(new ItemListener(){

				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
			   	        ct.execute(getTime() + " CONN " + sensorComboBox.getSelectedItem() + " " + j.getActionCommand().substring(9)); // connect sensor
			   	    }
			   	    else if (e.getStateChange() == ItemEvent.DESELECTED) {
 			   	        ct.execute(getTime() + " DISC " + j.getActionCommand().substring(9)); // disconnect sensor
			   	    }
				}
	    	});
	    	
	    }
	    for(JRadioButton j: toggledChannels){
	    	j.setEnabled(false);
	    }
	    for(JButton j: trigButtons){
	    	j.setEnabled(false);
	    }
	    
	    
	    // USB port - Lower Middle -------------------------------------------------------------------
	    
	    usbPanel = new JPanel();
	    lowerPanel.add(usbPanel);
	    
	    usbLabel = new JLabel("USB PORT");
	    usbLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    usbPanel.add(usbLabel);
	    
	    // end -------------------------------------------------------------------------------------------
	    
	    f.setVisible(true);
		startThread("prog");
	}
	//for submit and exit
   	private class PowerClickListener implements ActionListener{
   		@Override
   		public void actionPerformed(ActionEvent e) {
   			ct.execute(getTime() + " POWER");
   			powerToggled = !powerToggled;
   				
   			for(int i = 0; i < 8; i++) {
   				connectChannels.get(i).setEnabled(powerToggled);
   				trigButtons.get(i).setEnabled(powerToggled);
   				toggledChannels.get(i).setEnabled(powerToggled);
   				if(powerToggled == false) {
   					toggledChannels.get(i).setSelected(false);
   				}
   				
   			}
   				btnLeft.setEnabled(powerToggled);
   				btnRight.setEnabled(powerToggled);
   				btnUp.setEnabled(powerToggled);
   				btnDown.setEnabled(powerToggled);
   				btnFunction.setEnabled(powerToggled);
   				btnSwap.setEnabled(powerToggled);
   				
   				if(powerToggled == true) {
   					printToDisplay(ct.DispUpdate(getTime()));
   				}else {
   					printToDisplay("");
   				}
	     }		
	}
   	
   	private class TrigClickListener implements ActionListener{
   		@Override
   		public void actionPerformed(ActionEvent e) {
	         //TODO trigger button functionality
   			String command = e.getActionCommand();
   			ct.execute(getTime() + " " + command);
	     }		
	}
   	
   	private class EnableClickListener implements ActionListener{
   		@Override
   		public void actionPerformed(ActionEvent e) {
   			String command = e.getActionCommand();
   			ct.execute(getTime() + " " + command);
	     }		
	}
   	
   	
   	private class PrinterPowerClickListener implements ActionListener{
   		@Override
   		public void actionPerformed(ActionEvent e) {
	         //TODO numpad functionality - recieve either number, # or *
   			printerPower = !printerPower; // toggle printer power
   			if(printerPower == true) {
   				printerText.append("Printer is on.\n");// just to show that the printer is on
   			}else {
   				printerText.append("Printer is off.\n");;
   			}
	     }		
	}
   	
   	private class NumpadClickListener implements ActionListener{
   		@Override
   		public void actionPerformed(ActionEvent e) {
	         //TODO numpad functionality - receive either number, # or *
   			if(powerToggled){
   				currentCommand = "";
	   			killThread();
	   			String command = e.getActionCommand();
	   			if(command.equalsIgnoreCase("#")){
	   				if (swapping){
	   					ct.execute(getTime() + " SWAP " +numBuilder);
	   					swapping = false;
	   				}else if(clearing){
	   					ct.execute(getTime() + " CLR " +numBuilder);
	   					clearing = false;
	   				}else if(dnfing){
	   					ct.execute(getTime() + " DNF " +numBuilder);
	   					dnfing = false;
	   				}
	   				else{
	   					ct.execute(getTime() + " NUM " + numBuilder);
	   	   				numBuilder = ""; // reset
	   	   				printToDisplay(ct.DispUpdate(getTime()));
	   				}
	   				numBuilder = "";
	   				startThread("numpad");
	   			}else if(command.equalsIgnoreCase("*")){
	   				numBuilder = "";
	   				clearing = false;
	   				swapping = false;
	   				dnfing = false;
	   				startThread("numpad");
	   			}else{
	   				numBuilder += command;
	   				if (swapping){
	   					printToDisplay("Lane to swap runners: " + numBuilder+"\n(# to Confirm or * to Cancel)");
	   				}else if (clearing){
	   					printToDisplay("Enter Racer Num to CLR: " + numBuilder+"\n(# to Confirm or * to Cancel)");
	   				}else if (dnfing){
	   					printToDisplay("Enter Lane Num to DNF: " + numBuilder+"\n(# to Confirm or * to Cancel)");
	   				}else{
	   					printToDisplay("Add Racer: "+ numBuilder+"\n(# to Confirm or * to Cancel)");
	   				}
	   			}
   			}
	     }		
	}
   	
   	private class ArrowClickListener implements ActionListener{
   		@Override
   		public void actionPerformed(ActionEvent e) {
   			killThread();
   			boolean hasAction = false;
   			clearing = swapping = dnfing = false;
   			numBuilder = "";
   			String action = e.getActionCommand();
   			
   			switch(action) {
   			case "up":
   				//TODO
   				commandIndex--;
   				if(commandIndex < 0) {
   					commandIndex = commands.length - 1;
   				}
   				currentCommand = commands[commandIndex];
   				hasAction = true;
   				break;
   			case "down":
   				//TODO
   				commandIndex++;
   				if(commandIndex >= commands.length) {
   					commandIndex = 0;
   				}
   				currentCommand = commands[commandIndex];
   				hasAction = true;
   				break;
   			case "left":
   				if(currentCommand.equals("EVENT")) {
   					eventIndex--;
   	   				if(eventIndex < 0) {
   	   					eventIndex = eventOptions.length - 1;	   				
   	   				}
	   	   			hasAction = true;
   				}
   				break;
   			case "right":
   				if(currentCommand.equals("EVENT")) {
   					eventIndex++;
   	   				if(eventIndex >= eventOptions.length) {
   	   					eventIndex = 0;
   	   				}
   	   				hasAction = true;
   				}
   				break;
   			}
   			   			
   			String disp = "";
   			if(hasAction){
   				for(String s:commands){
   					if (currentCommand.equals(s)){
   						disp += " > " + s;
   					}else{
   						disp += "   " + s;
   					}
   					
   					if (s.equals("EVENT")){
   						disp += " " + eventOptions[eventIndex]+"\n";
   					}else{
   						disp += "\n";
   					}
   				}
   				printToDisplay(disp);
   			}
	     }		
	}
   	
   	private String getTime() {
		//Time Formated as HH:hh:ss
		//That is, Hour:Min:sec 
		String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
		return timeStamp;
	}
   	
   	public boolean printerPowerIsOn() {
   		return printerPower == true;
   	}
   	
   	public synchronized void printToDisplay(String text) {
        Runnable  runnable = new Runnable() {
            public void run(){
                displayText.setText(text);
            }
        };
        SwingUtilities.invokeLater(runnable);
    }
   	
   	public void printToPrinter(String s) {
   		printerText.append("\n----------------------\n" + s);
   	}
   	
   	public void killThread(){
   		if(dispThread!=null) dispThread.interrupt();
   	}
   	
   	public void startThread(String str){
   		if(activeRun==true){
	   		DispThread dt = new DispThread();	
	   		//dt.setSrc(str);
	   		dispThread = new Thread(dt);
	   		dispThread.start();
   		}else if(powerToggled){
   			printToDisplay("No Active Run");
   		}else printToDisplay("");
   	}
   	
   	private class DispThread implements Runnable{
   		boolean alive;
   		//String src;
   		
   		//public void setSrc(String str){src = str;}
   		
   		public void run(){
   			alive = true;
   			while (alive){
   				try{
   					//System.out.println(src);
   					Thread.sleep(25);
   					printToDisplay(ct.DispUpdate(getTime()));   					
   				}
				catch (InterruptedException e)
				{
					alive=false;
				}catch (Exception e){}
   			}
   			return;   			
   		}
   		
   	}
   	
   	private void resetGUI(){
   		powerToggled = false;
   		printerPower = false;
   		activeRun = false;
   		swapping = false;
   		clearing = false;
   		dnfing = false;
   		numBuilder = "";
	    for(JRadioButton j: connectChannels){
	    	j.setEnabled(false);
	    	j.setSelected(false);
	    }
	    for(JRadioButton j: toggledChannels){
	    	j.setEnabled(false);
	    	j.setSelected(false);
	    }
	    for(JButton j: trigButtons){
	    	j.setEnabled(false);
	    }
	    btnLeft.setEnabled(false);
		btnRight.setEnabled(false);
		btnUp.setEnabled(false);
		btnDown.setEnabled(false);
	    btnSwap.setEnabled(false);
	    btnFunction.setEnabled(false);
	    printToDisplay("");
   	}
}
