package chronoSimulator;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicArrowButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JTextPane;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JRadioButton;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.SystemColor;
import java.awt.Font;
import javax.swing.border.BevelBorder;
import javax.swing.AbstractAction;
import javax.swing.Action;

public class ChronoTimerGUI {
	// Frame and rootPanel
	JFrame f;
	JPanel upperPanel;
	
	// Power Panel
	JPanel powerPanel;
	JButton btnPower;
	
	// Sensor Panel
	JPanel sensorPanel;
	JLabel lblNewLabel; // label for application title
	JPanel startSensorPanel;
	JPanel startPanel;
	JTextArea startText;
	
	JButton trig1;
	JButton trig2;
	JButton trig3;
	JButton trig4;
	JButton trig5;
	JButton trig6;
	JButton trig7;
	JButton trig8;
	
	
	// NumberPad
	JPanel numPad;
	JButton num1;
	JButton num2;
	JButton num3;
	JButton num4;
	JButton num5;
	JButton num6;
	JButton num7;
	JButton num8;
	JButton num9;
	JButton num0;
	JButton numPound;
	JButton numStar;
	
	
	public ChronoTimerGUI() {
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
	    
	    powerPanel = new JPanel();
	    
	    
	    btnPower = new JButton("POWER");
	    btnPower.addActionListener(new PowerClickListener());
	    
	    powerPanel.add(btnPower);
	    
	    upperPanel.add(powerPanel);
	    
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
	    startPanel.add(trig1);
	    
	    trig3 = new JButton("3");
	    startPanel.add(trig3);
	    
	    trig5 = new JButton("5");
	    startPanel.add(trig5);
	    
	    trig7 = new JButton("7");
	    startPanel.add(trig7);
	    
	    JPanel startActivePanel = new JPanel();
	    startSensorPanel.add(startActivePanel);
	    startActivePanel.setLayout(new GridLayout(0, 5, 0, 0));
	    
	    JTextArea startEnableDisable = new JTextArea();
	    startEnableDisable.setBackground(SystemColor.control);
	    startEnableDisable.setText("Active");
	    startActivePanel.add(startEnableDisable);
	    
	    JRadioButton enable1 = new JRadioButton("");
	    enable1.setHorizontalAlignment(SwingConstants.CENTER);
	    JRadioButton enable3 = new JRadioButton("");
	    enable3.setHorizontalAlignment(SwingConstants.CENTER);
	    JRadioButton enable5 = new JRadioButton("");
	    enable5.setHorizontalAlignment(SwingConstants.CENTER);
	    JRadioButton enable7 = new JRadioButton("");
	    enable7.setHorizontalAlignment(SwingConstants.CENTER);
	    startActivePanel.add(enable1);
	    startActivePanel.add(enable3);
	    startActivePanel.add(enable5);
	    startActivePanel.add(enable7);
	    
	    JPanel finishSensorPanel = new JPanel();
	    sensorPanel.add(finishSensorPanel);
	    finishSensorPanel.setLayout(new GridLayout(2, 1, 0, 0));
	    
	    JPanel finishPanel = new JPanel();
	    finishSensorPanel.add(finishPanel);
	    finishPanel.setLayout(new GridLayout(0, 5, 0, 0));
	    
	    JTextArea finishText = new JTextArea();
	    finishText.setBackground(SystemColor.control);
	    finishText.setText("Finish");
	    finishPanel.add(finishText);
	    
	    trig2 = new JButton("2");
	    finishPanel.add(trig2);
	    
	    trig4 = new JButton("4");
	    finishPanel.add(trig4);
	    
	    trig6 = new JButton("6");
	    finishPanel.add(trig6);
	    
	    trig8 = new JButton("8");
	    finishPanel.add(trig8);
	    
	    JPanel finishActivePanel = new JPanel();
	    finishSensorPanel.add(finishActivePanel);
	    finishActivePanel.setLayout(new GridLayout(0, 5, 0, 0));
	    
	    JTextArea textArea_1 = new JTextArea();
	    textArea_1.setBackground(SystemColor.control);
	    textArea_1.setText("Active");
	    finishActivePanel.add(textArea_1);
	    
	    JRadioButton radioButton = new JRadioButton("");
	    radioButton.setHorizontalAlignment(SwingConstants.CENTER);
	    finishActivePanel.add(radioButton);
	    
	    JRadioButton radioButton_1 = new JRadioButton("");
	    radioButton_1.setHorizontalAlignment(SwingConstants.CENTER);
	    finishActivePanel.add(radioButton_1);
	    
	    JRadioButton radioButton_2 = new JRadioButton("");
	    radioButton_2.setHorizontalAlignment(SwingConstants.CENTER);
	    finishActivePanel.add(radioButton_2);
	    
	    JRadioButton radioButton_3 = new JRadioButton("");
	    radioButton_3.setHorizontalAlignment(SwingConstants.CENTER);
	    finishActivePanel.add(radioButton_3);
	    
	    JPanel printerPanel = new JPanel();
	    upperPanel.add(printerPanel);
	    printerPanel.setLayout(new BorderLayout(5, 5));
	    
	    JPanel printerPowerPanel = new JPanel();
	    printerPanel.add(printerPowerPanel, BorderLayout.NORTH);
	    
	    JButton btnPrinterPower = new JButton("PRINTER POWER");
	    printerPowerPanel.add(btnPrinterPower);
	    btnPrinterPower.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    	}
	    });
	    
	    JTextArea printerText = new JTextArea();
	    printerText.setText("Something from printer");
	    printerPanel.add(printerText, BorderLayout.CENTER);
	    
	    JPanel functionPanel = new JPanel();
	    upperPanel.add(functionPanel);
	    functionPanel.setLayout(new GridLayout(4, 1, 5, 5));
	    
	    JPanel functionContainer = new JPanel();
	    FlowLayout flowLayout = (FlowLayout) functionContainer.getLayout();
	    flowLayout.setAlignment(FlowLayout.LEFT);
	    functionPanel.add(functionContainer);
	    
	    JButton btnFunction = new JButton("FUNCTION");
	    functionContainer.add(btnFunction);
	    
	    JPanel arrowContainer = new JPanel();
	    FlowLayout flowLayout_2 = (FlowLayout) arrowContainer.getLayout();
	    flowLayout_2.setAlignment(FlowLayout.RIGHT);
	    
	    functionPanel.add(arrowContainer);
	    
	    JButton btnLeft = new JButton(String.valueOf('\u25C0'));
	    arrowContainer.add(btnLeft);
	    
	    JButton btnRight = new JButton(String.valueOf('\u25B6'));
	    arrowContainer.add(btnRight);
	    
	    JButton btnDown = new JButton(String.valueOf('\u25BC'));
	    arrowContainer.add(btnDown);
	    
	    JButton btnUp = new JButton(String.valueOf('\u25B2'));
	    arrowContainer.add(btnUp);
	    
	    JPanel swapContainer = new JPanel();
	    FlowLayout flowLayout_1 = (FlowLayout) swapContainer.getLayout();
	    flowLayout_1.setAlignment(FlowLayout.LEFT);
	    functionPanel.add(swapContainer);
	    
	    JButton btnSwap = new JButton("SWAP");
	    swapContainer.add(btnSwap);
	    
	    JPanel displayPanel = new JPanel();
	    upperPanel.add(displayPanel);
	    displayPanel.setLayout(new BorderLayout(5, 5));
	    
	    JTextPane displayText = new JTextPane();
	    displayText.setText("Something from racer queue");
	    displayText.setEditable(false);
	    displayPanel.add(displayText, BorderLayout.CENTER);
	    
	    JLabel displayLabel = new JLabel("Queue / Running / Final Time");
	    displayLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    displayLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
	    displayPanel.add(displayLabel, BorderLayout.SOUTH);
	    
	    numPad = new JPanel();
	    upperPanel.add(numPad);
	    numPad.setLayout(new GridLayout(4, 3, 0, 0));
	    
	    //Number Pad
	    num1 = new JButton("1");
	    numPad.add(num1);
	    
	    num2 = new JButton("2");
	    numPad.add(num2);
	    
	    num3 = new JButton("3");
	    numPad.add(num3);
	    
	    num4 = new JButton("4");
	    numPad.add(num4);
	    
	    num5 = new JButton("5");
	    numPad.add(num5);
	    
	    num6 = new JButton("6");
	    numPad.add(num6);
	    
	    num7 = new JButton("7");
	    numPad.add(num7);
	    
	    num8 = new JButton("8");
	    numPad.add(num8);
	    
	    num9 = new JButton("9");
	    numPad.add(num9);
	    
	    numPound = new JButton("#");
	    numPad.add(numPound);
	    
	    num0 = new JButton("0");
	    numPad.add(num0);
	    
	    numStar = new JButton("*");
	    numPad.add(numStar);
	    
	    JPanel lowerPanel = new JPanel();
	    lowerPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
	    f.getContentPane().add(lowerPanel, BorderLayout.SOUTH);
	    lowerPanel.setLayout(new GridLayout(0, 3, 0, 0));
	    
	    JPanel channelPanel = new JPanel();
	    lowerPanel.add(channelPanel);
	    channelPanel.setLayout(new BorderLayout(0, 0));
	    
	    JPanel channelTextPanel = new JPanel();
	    channelPanel.add(channelTextPanel, BorderLayout.WEST);
	    
	    JLabel chanLabel = new JLabel("CHAN");
	    channelTextPanel.add(chanLabel);
	    
	    JPanel channelButtonPanel = new JPanel();
	    channelPanel.add(channelButtonPanel, BorderLayout.CENTER);
	    channelButtonPanel.setLayout(new GridLayout(2, 0, 0, 0));
	    
	    JPanel chanPanelUpper = new JPanel();
	    channelButtonPanel.add(chanPanelUpper);
	    chanPanelUpper.setLayout(new GridLayout(2, 4, 0, 0));
	    
	    JLabel chanLabel1 = new JLabel("1");
	    chanLabel1.setHorizontalAlignment(SwingConstants.CENTER);
	    chanPanelUpper.add(chanLabel1);
	    
	    JLabel chanLabel3 = new JLabel("3");
	    chanLabel3.setHorizontalAlignment(SwingConstants.CENTER);
	    chanPanelUpper.add(chanLabel3);
	    
	    JLabel chanLabel5 = new JLabel("5");
	    chanLabel5.setHorizontalAlignment(SwingConstants.CENTER);
	    chanPanelUpper.add(chanLabel5);
	    
	    JLabel chanLabel7 = new JLabel("7");
	    chanLabel7.setHorizontalAlignment(SwingConstants.CENTER);
	    chanPanelUpper.add(chanLabel7);
	    
	    JRadioButton chan1 = new JRadioButton("");
	    chan1.setHorizontalAlignment(SwingConstants.CENTER);
	    chanPanelUpper.add(chan1);
	    
	    JRadioButton chan3 = new JRadioButton("");
	    chan3.setHorizontalAlignment(SwingConstants.CENTER);
	    chanPanelUpper.add(chan3);
	    
	    JRadioButton chan5 = new JRadioButton("");
	    chan5.setHorizontalAlignment(SwingConstants.CENTER);
	    chanPanelUpper.add(chan5);
	    
	    JRadioButton chan7 = new JRadioButton("");
	    chan7.setHorizontalAlignment(SwingConstants.CENTER);
	    chanPanelUpper.add(chan7);
	    
	    JPanel chanPanelLower = new JPanel();
	    channelButtonPanel.add(chanPanelLower);
	    chanPanelLower.setLayout(new GridLayout(2, 4, 0, 0));
	    
	    JLabel chanLabel2 = new JLabel("2");
	    chanLabel2.setHorizontalAlignment(SwingConstants.CENTER);
	    chanPanelLower.add(chanLabel2);
	    
	    JLabel chanLabel4 = new JLabel("4");
	    chanLabel4.setHorizontalAlignment(SwingConstants.CENTER);
	    chanPanelLower.add(chanLabel4);
	    
	    JLabel chanLabel6 = new JLabel("6");
	    chanLabel6.setHorizontalAlignment(SwingConstants.CENTER);
	    chanPanelLower.add(chanLabel6);
	    
	    JLabel chanLabel8 = new JLabel("8");
	    chanLabel8.setHorizontalAlignment(SwingConstants.CENTER);
	    chanPanelLower.add(chanLabel8);
	    
	    JRadioButton chan2 = new JRadioButton("");
	    chan2.setHorizontalAlignment(SwingConstants.CENTER);
	    chanPanelLower.add(chan2);
	    
	    JRadioButton chan4 = new JRadioButton("");
	    chan4.setHorizontalAlignment(SwingConstants.CENTER);
	    chanPanelLower.add(chan4);
	    
	    JRadioButton chan6 = new JRadioButton("");
	    chan6.setHorizontalAlignment(SwingConstants.CENTER);
	    chanPanelLower.add(chan6);
	    
	    JRadioButton chan8 = new JRadioButton("");
	    chan8.setHorizontalAlignment(SwingConstants.CENTER);
	    chanPanelLower.add(chan8);
	    
	    JPanel usbPanel = new JPanel();
	    lowerPanel.add(usbPanel);
	    
	    JLabel usbLabel = new JLabel("USB PORT");
	    usbLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    usbPanel.add(usbLabel);
	    f.setVisible(true);
	}
	//for submit and exit
   	private class PowerClickListener implements ActionListener{
   		@Override
   		public void actionPerformed(ActionEvent e) {
	         //TODO power button functionality
	     }		
	}
   	
   	private class TrigClickListener implements ActionListener{
   		@Override
   		public void actionPerformed(ActionEvent e) {
	         //TODO power button functionality
   			String command = e.getActionCommand();
	     }		
	}
}
