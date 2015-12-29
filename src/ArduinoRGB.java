import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Choice;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.Checkbox;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Toolkit;
import java.awt.Panel;
import javax.imageio.ImageIO;
import java.awt.Canvas;
import javax.swing.JPanel;
import javax.swing.ImageIcon;


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
	
	/**
	 * Opens the serial port previously selected
	 */
	public void serialInitialize() {
        try {
        	// open serial port
            System.out.println("Port opened: " + serialPort.openPort());
            // set the serial port parameters
            System.out.println("Params setted: " + serialPort.setParams(9600, 8, 1, 0));
            Thread.sleep(3000);
            init = true;
        }
        catch (SerialPortException | InterruptedException ex){
            System.out.println(ex);
        }
	}
	
	/**
	 * Send the array rgbValue that contains three components RGB
	 * @param rgbValue
	 */
	public void serialWrite(int[] rgbValue) {
		try {
				byte[] data = new byte[4];
				// data[0] = 'A' set animation singleLed 
				data[0] = (byte)'A';
				// insert the value of the slider red in data[0]
				data[1] = (byte)rgbValue[0];
				// insert the value of the slider green in data[1]
				data[2] = (byte)rgbValue[1];
				// insert the value of the slider blue in data[2]
				data[3] = (byte)rgbValue[2];
				System.out.println("\"Send RGB: " + rgbValue[0] + "," + rgbValue[1] + "," + rgbValue[2] + " " + serialPort.writeBytes(data));
				//System.out.println("Port closed: " + serialPort.closePort());
		}
        catch (SerialPortException ex){
            System.out.println(ex);
        }
	}
	
	/**
	 * Send rainbowValue used by Arduino to generate the rainbow effect
	 * @param rainbowValue
	 */
	public void serialWrite(int rainbowValue) {
		try {
			byte[] data = new byte[4];
			// data[0] = 'B' set animation rainbow 
			data[0] = (byte)'B';
			// insert rainbowValue in data[0]
			data[1] = (byte)rainbowValue;
			// insert rainbowValue shift right in data[1]
			data[2] = (byte)(rainbowValue >> 8);
			// data[3] = 0 is value not used
			data[3] = (byte)0;
			System.out.println("\"Send color: " + rainbowValue + " " + serialPort.writeBytes(data));
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
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		// create array rgb used to send the colors
		int[] rgb = new int[3];
		
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("I:\\Documenti\\Eclipse\\workspace\\ArduinoRGB\\image\\rgb.png"));
		frame.setBounds(100, 100, 499, 360);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		// definite and managed the sliderRed
		JSlider sliderRed = new JSlider();
		sliderRed.setEnabled(false);
		sliderRed.setMaximum(255);
		// send the rgb value to Arduino when there is a change state of sliderRed
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
		
		// definite and managed the sliderGreen
		JSlider sliderGreen = new JSlider();
		sliderGreen.setEnabled(false);
		sliderGreen.setMaximum(255);
		// send the rgb value to Arduino when there is a change state of sliderGreen
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
		
		// definite and managed the sliderBlue
		JSlider sliderBlue = new JSlider();
		sliderBlue.setEnabled(false);
		sliderBlue.setMaximum(255);
		// send the rgb value to Arduino when there is a change state of sliderBlue
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
		
		// definite and managed the sliderRainbow
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
		
		// definite and managed the title app
		JLabel lblNewLabel = new JLabel("Arduino RGB");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setBounds(203, 11, 99, 23);
		frame.getContentPane().add(lblNewLabel);
		
		// definite and managed the label red slider
		JLabel lblRed = new JLabel("RED");
		lblRed.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblRed.setForeground(Color.RED);
		lblRed.setHorizontalAlignment(SwingConstants.CENTER);
		lblRed.setBounds(20, 119, 46, 14);
		frame.getContentPane().add(lblRed);
		
		// definite and managed the label green slider
		JLabel lblGreen = new JLabel("GREEN");
		lblGreen.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblGreen.setForeground(Color.GREEN);
		lblGreen.setHorizontalAlignment(SwingConstants.CENTER);
		lblGreen.setBounds(20, 153, 46, 14);
		frame.getContentPane().add(lblGreen);
		
		// definite and managed the label blue slider
		JLabel lblBlue = new JLabel("BLUE");
		lblBlue.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblBlue.setForeground(Color.BLUE);
		lblBlue.setHorizontalAlignment(SwingConstants.CENTER);
		lblBlue.setBounds(20, 187, 46, 14);
		frame.getContentPane().add(lblBlue);
		
		// definite and managed the label "Select Port"
		JLabel lblPortCom = new JLabel("Select Port");
		lblPortCom.setHorizontalAlignment(SwingConstants.CENTER);
		lblPortCom.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPortCom.setBounds(46, 58, 77, 14);
		frame.getContentPane().add(lblPortCom);
		
		// definite and managed the checkboxRainbow
		Checkbox checkboxRainbow = new Checkbox("Rainbow");
		checkboxRainbow.setEnabled(false);
		checkboxRainbow.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				// if checkbox is on enable sliderRainbow and unable the signle color slider
				// and send serialWrite(sliderRainbow.getValue())
				if (checkboxRainbow.getState()) {
					sliderRainbow.setEnabled(true);
					// sending four times to be sure that Arduino read it, 
					// because the buffer Arduino might still be full
					serialWrite(sliderRainbow.getValue());
					serialWrite(sliderRainbow.getValue());
					serialWrite(sliderRainbow.getValue());
					serialWrite(sliderRainbow.getValue());
					sliderRed.setEnabled(false);
					sliderGreen.setEnabled(false);
					sliderBlue.setEnabled(false);
					sliderRed.setValue(0);
					sliderGreen.setValue(0);
					sliderBlue.setValue(0);
					// if checkbox is off enable the signle color slider and unable sliderRainbow 
					// and send serialWrite(rgb)
				} else {
					sliderRainbow.setEnabled(false);
					sliderRainbow.setValue(0);
					rgb[0] = 0;
					rgb[1] = 0;
					rgb[2] = 0;
					// sending four times to be sure that Arduino read it, 
					// because the buffer Arduino might still be full
					serialWrite(rgb);
					serialWrite(rgb);
					serialWrite(rgb);
					serialWrite(rgb);
					sliderRed.setEnabled(true);
					sliderGreen.setEnabled(true);
					sliderBlue.setEnabled(true);
				}
			}
		});
		checkboxRainbow.setBounds(238, 216, 68, 22);
		frame.getContentPane().add(checkboxRainbow);
		
		// definite and managed the label rainbow slider
		JLabel lblRainbow = new JLabel("RAINBOW");
		lblRainbow.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblRainbow.setBounds(20, 250, 63, 14);
		frame.getContentPane().add(lblRainbow);
		
		// definite and managed the button "Reset LED"
		JButton btnResetLed = new JButton("Reset LED");
		btnResetLed.setEnabled(false);
		btnResetLed.addMouseListener(new MouseAdapter() {
			@Override
			// if btnResetLed is clicked reset the sliders and send the color to Arduino
			public void mouseClicked(MouseEvent e) {
				if (checkboxRainbow.getState()) {
					sliderRainbow.setValue(0);
					serialWrite(0);
				} else {
					sliderRed.setValue(0);
					sliderGreen.setValue(0);
					sliderBlue.setValue(0);
					rgb[0] = 0;
					rgb[1] = 0;
					rgb[2] = 0;
					serialWrite(rgb);
				}
			}
		});
		btnResetLed.setBounds(346, 54, 100, 23);
		frame.getContentPane().add(btnResetLed);
		
		// create the list with the ports serial
		listPort = SerialPortList.getPortNames();
		Choice choice = new Choice();
		choice.addItemListener(new ItemListener() {
			// initialize the selected port and enable the sliders
			public void itemStateChanged(ItemEvent arg0) {
				serialPort = new SerialPort(choice.getSelectedItem());
				serialInitialize();
				if (init) {
					sliderRed.setEnabled(true);
					sliderGreen.setEnabled(true);
					sliderBlue.setEnabled(true);
					checkboxRainbow.setEnabled(true);	
					btnResetLed.setEnabled(true);
				}
			}
		});
		choice.setBounds(129, 55, 107, 20);
		frame.getContentPane().add(choice);
		for (String list: listPort)
			choice.add(list);

		// definite and managed the button "Updated"
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				listPort = SerialPortList.getPortNames();
				choice.removeAll();
				for (String list: listPort)
					choice.add(list);
			}
		});
		btnUpdate.setBounds(246, 54, 77, 23);
		frame.getContentPane().add(btnUpdate);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon("I:\\Documenti\\Eclipse\\workspace\\ArduinoRGB\\image\\sfumatureRGB.jpg"));
		lblNewLabel_1.setBounds(96, 270, 342, 23);
		frame.getContentPane().add(lblNewLabel_1);
		
	}
}
