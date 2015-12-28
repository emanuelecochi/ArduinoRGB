import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.plaf.SliderUI;

import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import java.io.IOException;
import java.util.Scanner;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JComboBox;
import java.awt.Choice;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.Checkbox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;


public class ArduinoRGB {

	private JFrame frame;
	SerialPort serialPort;
	String[] listPort;
	boolean init = false;
	boolean rainbow = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ArduinoRGB window = new ArduinoRGB();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}		
			}
		});
	}
	
	public void serialInitialize() {
        try {
        	// Apro la porta seriale
            System.out.println("Port opened: " + serialPort.openPort());
            // Imposto i parametri della porta seriale 
            System.out.println("Params setted: " + serialPort.setParams(9600, 8, 1, 0));
            Thread.sleep(3000);
            init = true;
        }
        catch (SerialPortException | InterruptedException ex){
            System.out.println(ex);
        }
	}
	
	public void serialWrite(int[] rgbValue) {
		try {
				byte[] data = new byte[4];
				// data[0] = 1 definisce l'animazione singleLed 
				data[0] = (byte)1;
				// inserisco il valore dello slider rosso in data[0]
				data[1] = (byte)rgbValue[0];
				// inserisco il valore dello slider verde in data[1]
				data[2] = (byte)rgbValue[1];
				// inserisco il valore dello slider blu in data[2]
				data[3] = (byte)rgbValue[2];
				System.out.println("\"Inviato RGB: " + rgbValue[0] + "," + rgbValue[1] + "," + rgbValue[2] + " " + serialPort.writeBytes(data));
				//System.out.println("Port closed: " + serialPort.closePort());
		}
        catch (SerialPortException ex){
            System.out.println(ex);
        }
	}
	
	public void serialWrite(int rainbowValue) {
		try {
			byte[] data = new byte[4];
			// data[0] = 2 definisce l'animazione singleLed 
			data[0] = (byte)2;
			// inserisco il valore dello slider rosso in data[0]
			data[1] = (byte)rainbowValue;
			// inserisco il valore dello slider verde in data[1]
			data[2] = (byte)(rainbowValue >> 8);
			// data[3] = 0 è un valore non utilizzato
			data[3] = (byte)0;
			System.out.println("\"Inviato colore: " + rainbowValue + " " + serialPort.writeBytes(data));
			//System.out.println("Port closed: " + serialPort.closePort());			
		}
        catch (SerialPortException ex){
            System.out.println(ex);
        }
	}
	
	/**
	 * Create the application.
	 */
	public ArduinoRGB() {
		initialize();
		//serialInitialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		int[] rgb = new int[3];
		frame = new JFrame();
		frame.setBounds(100, 100, 499, 360);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		JSlider sliderRed = new JSlider();
		sliderRed.setEnabled(false);
		sliderRed.setMaximum(255);
		sliderRed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (init) {
				rgb[0] = sliderRed.getValue();
				serialWrite(rgb);
				//System.out.println("RGB : " + rgb[0] + " " + rgb[1] + " " +rgb[2]);
				}
			}
		});
		
		sliderRed.setValue(0);
		sliderRed.setBounds(93, 115, 353, 23);
		frame.getContentPane().add(sliderRed);
		
		JSlider sliderGreen = new JSlider();
		sliderGreen.setEnabled(false);
		sliderGreen.setMaximum(255);
		sliderGreen.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (init) {
				rgb[1] = sliderGreen.getValue();
				serialWrite(rgb);
				//System.out.println("RGB : " + rgb[0] + " " + rgb[1] + " " +rgb[2]);
				}
			}
		});
		sliderGreen.setValue(0);
		sliderGreen.setBounds(93, 149, 353, 23);
		frame.getContentPane().add(sliderGreen);
		
		JSlider sliderBlue = new JSlider();
		sliderBlue.setEnabled(false);
		sliderBlue.setMaximum(255);
		sliderBlue.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (init) {
				rgb[2] = sliderBlue.getValue();
				serialWrite(rgb);
				//System.out.println("RGB : " + rgb[0] + " " + rgb[1] + " " +rgb[2]);
				}
			}
		});
		sliderBlue.setValue(0);
		sliderBlue.setBounds(93, 183, 353, 23);
		frame.getContentPane().add(sliderBlue);
		
		JLabel lblNewLabel = new JLabel("Arduino RGB");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setBounds(204, 25, 99, 23);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblRed = new JLabel("RED");
		lblRed.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblRed.setForeground(Color.RED);
		lblRed.setHorizontalAlignment(SwingConstants.CENTER);
		lblRed.setBounds(20, 119, 46, 14);
		frame.getContentPane().add(lblRed);
		
		JLabel lblGreen = new JLabel("GREEN");
		lblGreen.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblGreen.setForeground(Color.GREEN);
		lblGreen.setHorizontalAlignment(SwingConstants.CENTER);
		lblGreen.setBounds(20, 153, 46, 14);
		frame.getContentPane().add(lblGreen);
		
		JLabel lblBlue = new JLabel("BLUE");
		lblBlue.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblBlue.setForeground(Color.BLUE);
		lblBlue.setHorizontalAlignment(SwingConstants.CENTER);
		lblBlue.setBounds(20, 187, 46, 14);
		frame.getContentPane().add(lblBlue);
		
		JLabel lblPortCom = new JLabel("Select Port");
		lblPortCom.setHorizontalAlignment(SwingConstants.CENTER);
		lblPortCom.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPortCom.setBounds(166, 71, 77, 14);
		frame.getContentPane().add(lblPortCom);
		
		JSlider sliderRainbow = new JSlider();
		sliderRainbow.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (init) {
					serialWrite(sliderRainbow.getValue());
					//System.out.println("RGB : " + rgb[0] + " " + rgb[1] + " " +rgb[2]);
					}
			}
		});
		sliderRainbow.setValue(0);
		sliderRainbow.setMaximum(1024);
		sliderRainbow.setEnabled(false);
		sliderRainbow.setBounds(93, 246, 353, 23);
		frame.getContentPane().add(sliderRainbow);
		
		Checkbox checkbox = new Checkbox("Rainbow");
		checkbox.setEnabled(false);
		checkbox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (checkbox.getState()) {
					sliderRainbow.setEnabled(true);
					serialWrite(sliderRainbow.getValue());
					sliderRed.setEnabled(false);
					sliderGreen.setEnabled(false);
					sliderBlue.setEnabled(false);
					sliderRed.setValue(0);
					sliderGreen.setValue(0);
					sliderBlue.setValue(0);
				}
					
				else {
					sliderRainbow.setEnabled(false);
					sliderRainbow.setValue(0);
					sliderRed.setEnabled(true);
					sliderGreen.setEnabled(true);
					sliderBlue.setEnabled(true);
					rgb[0] = 0;
					rgb[1] = 0;
					rgb[2] = 0;
					serialWrite(rgb);
				}
			}
		});
		checkbox.setBounds(238, 216, 68, 22);
		frame.getContentPane().add(checkbox);
		
		
		JLabel lblRainbow = new JLabel("RAINBOW");
		lblRainbow.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblRainbow.setBounds(20, 250, 63, 14);
		frame.getContentPane().add(lblRainbow);
		
		listPort = SerialPortList.getPortNames();
		Choice choice = new Choice();
		choice.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				serialPort = new SerialPort(choice.getSelectedItem());
				serialInitialize();
				if (init) {
					sliderRed.setEnabled(true);
					sliderGreen.setEnabled(true);
					sliderBlue.setEnabled(true);
					checkbox.setEnabled(true);
				}
			}
		});
		choice.setBounds(249, 68, 107, 20);
		for (String list: listPort)
			choice.add(list);
		frame.getContentPane().add(choice);
		
	}
}
