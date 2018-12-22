package Food_Service_Package;

import javax.swing.JFrame;  // Using Frame class in package java.awt
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

//A GUI program is written as a subclass of Frame - the top-level container
//This subclass inherits all properties from Frame, e.g., title, icon, buttons, content-pane
public class BackEnd extends JFrame {

	// private variables
	double total = 0.00;
	private JTextArea tfTotalDisplay;	
	private static JTextArea SystemDisplay;
	public double Total;
	
	NumberFormat formatter = new DecimalFormat("#0.00");
	
	// Constructor to setup the GUI components
	public BackEnd() { 
		
		//frame
		setTitle("Backend");
		setSize(300,500);
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//basic label
		JLabel lblTotal;                     // Declare an Label instance called lblInput
		lblTotal = new JLabel("Total");   // Construct by invoking a constructor via the new operator
		lblTotal.setBounds(75, 25, 100, 25);
		add(lblTotal);
		
		//displays total income from client
		tfTotalDisplay = new JTextArea(); // Declare and allocate an TextField instance called tfInput
		tfTotalDisplay.setBounds(125, 30, 150 , 18);		//where teh textfield is located
		add(tfTotalDisplay);                          // "this" Container adds the TextField
		tfTotalDisplay.setEditable(false) ;          // Set to read-only
		tfTotalDisplay.setText(formatter.format(total));
		
		// just to display connection notifications
		SystemDisplay = new JTextArea();
		SystemDisplay.setBounds(25, 75, 250, 350);
		add(SystemDisplay);
		SystemDisplay.setEditable(false);
		
	}
	
	public void startRunning() throws IOException {
		ServerSocket ss = new ServerSocket(1235); 
        
        // running infinite loop for getting 
        // client request 
	while (true){       	
		Socket s = null; 
				
		
		try { 
			// socket object to receive incoming client requests 
			s = ss.accept();
                  
			showMessage("New Client : \n" + s); 
                  
			// obtaining input and out streams 
			DataInputStream din = new DataInputStream(s.getInputStream()); 
			DataOutputStream dout = new DataOutputStream(s.getOutputStream()); 
                           
			showMessage("\nAssigning new thread"); 
                              
			// create a new thread object 
			Thread thread = new ClientHandler(s, din, dout, BackEnd.this); 
			
			// Invoking the start() method 
			thread.start();	
			
            }catch (Exception e){ 
            		s.close(); 
                e.printStackTrace(); 
            } 
        }
	}
	
	private void showMessage(final String text) {
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						SystemDisplay.append(text);
					}
				}
		);
	}
	
	public void setTotal(double temp) {
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						total = total+temp;
						setDisplay(total);
					}
				}
		);
	}
	
	public void setDisplay(double temp) {
		tfTotalDisplay.setText(formatter.format(temp));
	}
	
	//public void getTemp(double temp) {
	//	Total = Total + temp;
	//	System.out.println(Total);
	//}
	
	// The entry main() method
	public static void main(String[] args) throws IOException {
		// Invoke the constructor (to setup the GUI) by allocating an instance
		BackEnd BE = new BackEnd();
		BE.setVisible(true);
		BE.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BE.startRunning();
	}
};
	
// ClientHandler class 
class ClientHandler extends Thread  { 
	final DataInputStream dis; 
	final DataOutputStream dos; 
	final Socket s;
	BackEnd BE = new BackEnd();
		    
	// Constructor 
	public ClientHandler(Socket s, DataInputStream in, DataOutputStream dos, BackEnd backend) throws IOException { 
		this.s = s; 
		this.dis = in; 
		this.dos = dos;
		this.BE = backend;
	} 
		  
	@Override
	public void run() { 
		        
		//String to return; 
		while (true) { 
			try { 
		        
				double temp = dis.readDouble();
				BE.setTotal(temp);
				//System.out.println(BE.tempTotal);
		            	   
				if(temp == -1){  
					System.out.println("Client " + this.s + " sends exit..."); 
					System.out.println("Closing this connection."); 
					this.s.close(); 
					System.out.println("Connection closed"); 
					break; 
				} 

			} catch (IOException e) { 
	    			e.printStackTrace(); 
			} 
		} 
		          
		try{ 
			// closing resources 
			this.dis.close(); 
			this.dos.close(); 
		              
		}catch(IOException e){ 
			e.printStackTrace(); 
		} 
	}
		    
	//public double getTotal() {
	//	return(temp);
	//}
	
}; 



