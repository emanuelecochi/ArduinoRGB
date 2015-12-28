import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JSlider;

public class SerialJava {
	
	public static String[] listPort() {	
		// Salvo in portNames la lista delle porte seriali
		String[] portNames = SerialPortList.getPortNames();
		        
		if (portNames.length == 0) {
		    System.out.println("There are no serial-ports :(");
		    System.out.println("Press Enter to exit...");
		    try {
		        System.in.read();
		    } catch (IOException e) {
		         // TODO Auto-generated catch block
		          e.printStackTrace();
		    }
		    return null;
		}

		System.out.println("Choose the port:");
		for (int i = 0; i < portNames.length; i++){
		    System.out.println(i+1 + ": " + portNames[i]);
		}
		
		return portNames;
	}
	
	
    public static void main(String[] args) throws InterruptedException {
    	
        // Creo una finestra con uno slider
		JFrame window = new JFrame();
		JSlider slider = new JSlider();
		slider.setMaximum(1024);
		window.add(slider);
		window.pack();
		window.setVisible(true);
		slider.setValue(0);
    	
    	// Salvo in listPort la lista delle porte seriale presenti
    	String[] listPort = SerialJava.listPort();
    	
    	// Permetto la scelta della porta seriale da utilizzare
    	Scanner s = new Scanner(System.in);
		int chosenPort = s.nextInt();
        SerialPort serialPort = new SerialPort(listPort[chosenPort-1]);
        
        try {
        	// Apro la porta seriale
            System.out.println("Port opened: " + serialPort.openPort());
            // Imposto i parametri della porta seriale 
            System.out.println("Params setted: " + serialPort.setParams(9600, 8, 1, 0));
            Thread.sleep(3000);
            while (true) {
            	// Ottengo il valore dello slider
            	int value = slider.getValue();
            	byte[] data = new byte[2];
            	// Inserisco il valore in data e l'invio
            	data[0] = (byte)value;
            	data[1] = (byte)(value >> 8);
            	System.out.println("\"Inviato : " + value + " " + serialPort.writeBytes(data));
            }
            //System.out.println("Port closed: " + serialPort.closePort());
        }
        catch (SerialPortException ex){
            System.out.println(ex);
        }
        
    }
}
