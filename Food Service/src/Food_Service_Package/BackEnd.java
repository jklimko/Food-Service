package Food_Service_Package;

import javax.swing.JFrame;  // Using Frame class in package java.awt
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.io.*;
import java.net.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.NumberFormat;

//A GUI program is written as a subclass of Frame - the top-level container
//This subclass inherits all properties from Frame, e.g., title, icon, buttons, content-pane
public class BackEnd extends JFrame {

	// private variables
	double total = 0.00;
	private double tempTotal;
	private DataInputStream input;
	private ServerSocket server;
	private Socket connection;
	
	
	private static JTextArea tfTotalDisplay;
	
	NumberFormat formatter = new DecimalFormat("#0.00");
	// Constructor to setup the GUI components
	public BackEnd() { 
		
		setTitle("Backend");
		setSize(300,100);
		//setLayout(new FlowLayout());
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//basic total label
		JLabel lblTotal;                     // Declare an Label instance called lblInput
		lblTotal = new JLabel("Total");   // Construct by invoking a constructor via the new operator
		lblTotal.setBounds(75, 25, 100, 25);
		add(lblTotal);
		
		//displays total  income from client
		tfTotalDisplay = new JTextArea(); // Declare and allocate an TextField instance called tfInput
		tfTotalDisplay.setBounds(125, 25, 150 , 25);		//where teh textfield is located
		add(tfTotalDisplay);                          // "this" Container adds the TextField
		tfTotalDisplay.setEditable(false) ;          // Set to read-only
		tfTotalDisplay.setText(formatter.format(total));
		
	}
	
	//Set up and run the server
	public void StartRunning() {
		try {
			server = new ServerSocket(1234, 100);
			while(true) {
				try {
					waitForConnection();
					setupStreams();
					whileConnected();
				}catch(EOFException eofException) {
				}finally {
					closeCrap();
				}
			}
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	//Wait for connection and display connection info
	private void waitForConnection() throws IOException{
		showMessage("Awaiting Connection...");
		connection = server.accept();
		connection.setTcpNoDelay(true);
	}
	
	//getSteam to receive data
	private void setupStreams() throws IOException{
		input = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
		showMessage("Streams now setup");
	}
	
	//during conversation
	private void whileConnected() throws IOException{
		do {
			tempTotal = input.readDouble();
			setTotal(tempTotal);	
		}while(total != -1);
	}
	
	private void setTotal(double temp) {
		total = total+temp;
		tfTotalDisplay.setText(formatter.format(total));
	}
	
	private void closeCrap() {
		try {
			input.close();
			connection.close();
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	private void showMessage(final String text) {
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						tfTotalDisplay.setText(text);
					}
				});
	}
	
	// The entry main() method
	public static void main(String[] args) {
		// Invoke the constructor (to setup the GUI) by allocating an instance
		BackEnd BE = new BackEnd();
		BE.setVisible(true);
		BE.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BE.StartRunning();
	}
}
