package Food_Service_Package;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.Font;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

//A GUI program is written as a subclass of Frame - the top-level container
//This subclass inherits all properties from Frame, e.g., title, icon, buttons, content-pane
public class BackEnd extends JFrame {

	// variables
	private double total = 0.00;
	private int transCount = 0;
	private int burgersSold = 0;
	private int friesSold = 0;
	private int drinksSold = 0;
	
	private JTextArea tfTotalDisplay;
	private JTextArea tfTotalTransDisplay;
	private JTextArea tfBurgersSoldDisplay;
	private JTextArea tfFriesSoldDisplay;
	private JTextArea tfDrinksSoldDisplay;
	private static JTextArea SystemDisplay;
	
	
	NumberFormat formatter = new DecimalFormat("#0.00");
	//NumberFormat formatterInt  = new IntFormat("#0");
	
	// Constructor to setup the GUI components
	public BackEnd() { 
		
		//frame
		setTitle("Backend");
		setSize(600,500);
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//Data Column label
		JLabel lblData;
		lblData = new JLabel("Data");
		lblData.setBounds(20, 5, 100, 25);
		lblData.setFont(new Font("Impact", Font.BOLD, 24));
		add(lblData);
		
		//"Total" label
		JLabel lblTotal;                   
		lblTotal = new JLabel("Total");   
		lblTotal.setBounds(20, 40, 100, 25);
		add(lblTotal);
		
		//displays total income from client
		tfTotalDisplay = new JTextArea(); // Declare and allocate an TextField instance called tfInput
		tfTotalDisplay.setBounds(20, 65, 150 , 18);		//where the textfield is located
		add(tfTotalDisplay);                          // "this" Container adds the TextField
		tfTotalDisplay.setEditable(false) ;          // Set to read-only
		tfTotalDisplay.setText(formatter.format(total));
		
		//"transaction Count" label
		JLabel lblTotalTrans;                     
		lblTotalTrans = new JLabel("Transation Count");  
		lblTotalTrans.setBounds(20, 90, 150, 25);
		add(lblTotalTrans);
		
		//displays total # of transactions from  all clients
		
		tfTotalTransDisplay = new JTextArea();
		tfTotalTransDisplay.setBounds(20, 115, 150 , 18);		
		add(tfTotalTransDisplay);                          
		tfTotalTransDisplay.setEditable(false) ;        
		tfTotalTransDisplay.setText("" + transCount);
		
		//"total Burgers Sold" label
		JLabel lblBurgerSold;                     
		lblBurgerSold = new JLabel("Total Burgers Sold");  
		lblBurgerSold.setBounds(20, 140, 150, 25);
		add(lblBurgerSold);
				
		//displays total # of transactions from  all clients
		tfBurgersSoldDisplay = new JTextArea();
		tfBurgersSoldDisplay.setBounds(20, 165, 150 , 18);		
		add(tfBurgersSoldDisplay);                          
		tfBurgersSoldDisplay.setEditable(false) ;        
		tfBurgersSoldDisplay.setText("" + burgersSold);
				
		//"total Fries Sold" label
		JLabel lblFriesSold;                     
		lblFriesSold = new JLabel("Total Fries Sold");  
		lblFriesSold.setBounds(20, 190, 150, 25);
		add(lblFriesSold);
						
		//displays total # of transactions from  all clients
		tfFriesSoldDisplay = new JTextArea();
		tfFriesSoldDisplay.setBounds(20, 215, 150 , 18);		
		add(tfFriesSoldDisplay);                          
		tfFriesSoldDisplay.setEditable(false) ;        
		tfFriesSoldDisplay.setText("" + friesSold);
		
		//"total Drinks Sold" label
		JLabel lblDrinksSold;                     
		lblDrinksSold = new JLabel("Total Drinks Sold");  
		lblDrinksSold.setBounds(20, 240, 150, 25);
		add(lblDrinksSold);
				
		//displays total # of transactions from  all clients
		tfDrinksSoldDisplay = new JTextArea();
		tfDrinksSoldDisplay.setBounds(20, 265, 150 , 18);		
		add(tfDrinksSoldDisplay);                          
		tfDrinksSoldDisplay.setEditable(false) ;        
		tfDrinksSoldDisplay.setText("" + drinksSold);
		
		
		
		//Connection Info Display label
		JLabel lblConnectionInfo;
		lblConnectionInfo = new JLabel("Connection Information");
		lblConnectionInfo.setBounds(300, 5, 250, 25);
		lblConnectionInfo.setFont(new Font("Impact", Font.BOLD, 24));
		add(lblConnectionInfo);
						
		// just to display connection notifications
		SystemDisplay = new JTextArea();
		SystemDisplay.setBounds(300, 40, 250, 350);
		add(SystemDisplay);
		SystemDisplay.setEditable(false);
		
	}
	
	//Server that is constantly listening for new client requests and will make a new thread if there is one. 
	public void startRunning() throws IOException {
		ServerSocket ss = new ServerSocket(1235); 
        
        // running infinite loop for getting client request 
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
	
	//updates a display to show any messages coming from the server
	private void showMessage(final String text) {
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						SystemDisplay.append(text);
					}
				}
		);
	}
	
	//adds any additions to the running totaal and calls the setDisplay method to update the gui
	public void setDisplay(double tempTotal, int tempTransCount, int tempBurgerSold, int tempFriesSold, int tempDrinksSold) {
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						total = total+tempTotal;
						tfTotalDisplay.setText(formatter.format(total));
						
						transCount = transCount + tempTransCount;
						tfTotalTransDisplay.setText("" + transCount);
						
						burgersSold = burgersSold + tempBurgerSold;
						tfBurgersSoldDisplay.setText("" + burgersSold);
						
						friesSold = friesSold + tempFriesSold; 
						tfFriesSoldDisplay.setText("" + friesSold);
						
						drinksSold = drinksSold + tempDrinksSold;
						tfDrinksSoldDisplay.setText("" + drinksSold);	
					}
				}
		);
	}
	
	
	// The entry main() method
	public static void main(String[] args) throws IOException {
		// Invoke the constructor (to setup the GUI) by allocating an instance and begins to run the server
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
				
				double tempTotal = dis.readDouble();
				
				int tempTransactionCount = dis.readInt();
				//System.out.println(tempTransactionCount);	
				
				int tempBurgerSold = dis.readInt();
				//System.out.println(tempBurgerSold);
				
				int tempFriesSold = dis.readInt();
				
				int tempDrinksSold = dis.readInt();
				
				BE.setDisplay(tempTotal, tempTransactionCount, tempBurgerSold, tempFriesSold, tempDrinksSold);
					            	   
				
				if(tempTotal <= -1){  
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
	
}; 



