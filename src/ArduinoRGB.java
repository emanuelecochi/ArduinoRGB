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


public class ArduinoRGB {

	private JFrame frame;
	SerialPort serialPort;

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
		
		// Salvo in listPort la lista delle porte seriale presenti
    	String[] listPort = SerialJava.listPort();
    	
    	// Permetto la scelta della porta seriale da utilizzare
    	Scanner s = new Scanner(System.in);
		int chosenPort = s.nextInt();
        serialPort = new SerialPort(listPort[chosenPort-1]);
        
        try {
        	// Apro la porta seriale
            System.out.println("Port opened: " + serialPort.openPort());
            // Imposto i parametri della porta seriale 
            System.out.println("Params setted: " + serialPort.setParams(9600, 8, 1, 0));
            Thread.sleep(3000);
        }
        catch (SerialPortException | InterruptedException ex){
            System.out.println(ex);
        }
	}
	
	public void serialWrite(int[] value) {
		try {
				byte[] data = new byte[3];
				// inserisco il valore dello slider rosso in data[0]
				data[0] = (byte)value[0];
				// inserisco il valore dello slider verde in data[1]
				data[1] = (byte)value[1];
				// inserisco il valore dello slider blu in data[2]
				data[2] = (byte)value[2];
				//data[1] = (byte)(value >> 8);
				System.out.println("\"Inviato RGB: " + value[0] + "," + value[1] + "," + value[2] + " " + serialPort.writeBytes(data));
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
		serialInitialize();
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		int[] rgb = new int[3];
		frame = new JFrame();
		frame.setBounds(100, 100, 499, 238);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		JSlider sliderRed = new JSlider();
		sliderRed.setEnabled(false);
		sliderRed.setMaximum(255);
		sliderRed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				rgb[0] = sliderRed.getValue();
				serialWrite(rgb);
				//System.out.println("RGB : " + rgb[0] + " " + rgb[1] + " " +rgb[2]);
			}
		});
		
		sliderRed.setValue(0);
		sliderRed.setBounds(93, 78, 353, 23);
		frame.getContentPane().add(sliderRed);
		
		JSlider sliderGreen = new JSlider();
		sliderGreen.setMaximum(255);
		sliderGreen.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				rgb[1] = sliderGreen.getValue();
				serialWrite(rgb);
				//System.out.println("RGB : " + rgb[0] + " " + rgb[1] + " " +rgb[2]);
			}
		});
		sliderGreen.setValue(0);
		sliderGreen.setBounds(93, 112, 353, 23);
		frame.getContentPane().add(sliderGreen);
		
		JSlider sliderBlue = new JSlider();
		sliderBlue.setMaximum(255);
		sliderBlue.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				rgb[2] = sliderBlue.getValue();
				serialWrite(rgb);
				//System.out.println("RGB : " + rgb[0] + " " + rgb[1] + " " +rgb[2]);
			}
		});
		sliderBlue.setValue(0);
		sliderBlue.setBounds(93, 146, 353, 23);
		frame.getContentPane().add(sliderBlue);
		
		JLabel lblNewLabel = new JLabel("Arduino RGB");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setBounds(211, 24, 99, 23);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblRed = new JLabel("RED");
		lblRed.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblRed.setForeground(Color.RED);
		lblRed.setHorizontalAlignment(SwingConstants.CENTER);
		lblRed.setBounds(20, 82, 46, 14);
		frame.getContentPane().add(lblRed);
		
		JLabel lblGreen = new JLabel("GREEN");
		lblGreen.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblGreen.setForeground(Color.GREEN);
		lblGreen.setHorizontalAlignment(SwingConstants.CENTER);
		lblGreen.setBounds(20, 116, 46, 14);
		frame.getContentPane().add(lblGreen);
		
		JLabel lblBlue = new JLabel("BLUE");
		lblBlue.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblBlue.setForeground(Color.BLUE);
		lblBlue.setHorizontalAlignment(SwingConstants.CENTER);
		lblBlue.setBounds(20, 150, 46, 14);
		frame.getContentPane().add(lblBlue);
		
	}
}
