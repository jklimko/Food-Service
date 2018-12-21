package Food_Service_Package;

import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFrame;

public class FrontEnd extends JFrame {
   
	//keep track of money
	private static JTextField Payment;
    public static  double total = 0.00;
    private static double change;
    NumberFormat formatter = new DecimalFormat("#0.00");
    
    //keep track of units sold
    private static int burger = 0;
    private static int fries = 0;
    private static int drink = 0;
    private int transactionCount;
    
    //gui elements
    private static JTextArea TotalDisplay;
    private static JTextArea txtpnItemlist;
    private JButton btnChange;
   
    //server stuff
    private DataOutputStream output;
    	private String serverIP;
    	private Socket connection;
   
    

	//constructor
    	public FrontEnd(String host) {
		
		setTitle("Food Service");
		serverIP = host;
		JPanel panel = new JPanel();
		   
		//sets the basics of the frame     
	    getContentPane().setBackground(new Color(255, 255, 204));
	    setBounds(100, 100, 539, 693);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setLayout(null);
	        
	    //*Food service Frame title
	    JLabel lblFoodService = new JLabel("Food Service");
	    lblFoodService.setFont(new Font("Impact", Font.BOLD, 62));
	    lblFoodService.setBounds(6, 13, 350, 52);
	    add(lblFoodService);

	    //List of the selected items
	    txtpnItemlist = new JTextArea();
	    txtpnItemlist.setBackground(SystemColor.window);
	    txtpnItemlist.setBounds(350, 74, 180, 395);
	    add(txtpnItemlist);
	    txtpnItemlist.setEditable(false);
	    
	    //*Adds a "total" label
	    JLabel lblTotal = new JLabel("Total.................");
	    lblTotal.setBounds(350, 486, 100, 43);
	    getContentPane().add(lblTotal);
	        
	    //Displays total amount of money due
	    TotalDisplay = new JTextArea();
	    TotalDisplay.setBackground(SystemColor.window);
	    TotalDisplay.setBounds(453, 496, 77, 21);
	    add(TotalDisplay);
	    TotalDisplay.setEditable(false);
	     
	    //*Adds "payment" label
	    JLabel lblPayment = new JLabel("Payment............");
	    lblPayment.setBounds(350, 532, 105, 20);
	    getContentPane().add(lblPayment);
	      
	    //Field to enter payment given to cashier
	    Payment = new JTextField();
	    Payment.setBackground(SystemColor.window);
	    Payment.setBounds(449, 530, 83, 21);
	    getContentPane().add(Payment);
	    Payment.setColumns(10);
	     
	    //Text area to display change due to customer
	    JTextPane txtpnChangedisplay = new JTextPane();
	    txtpnChangedisplay.setBackground(SystemColor.window);
	    txtpnChangedisplay.setBounds(453, 570, 77, 21);
	    add(txtpnChangedisplay);
	    txtpnChangedisplay.setEditable(false);
	        
	    //Button to calculate change due to customer and displays to "txtpnChangeDisplay"
	    btnChange = new JButton("Change");
	    btnChange.setBounds(350, 570, 91, 21);
	    add(btnChange);
	    btnChange.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e){
	    			double a = Double.parseDouble( TotalDisplay.getText()); 	//gets total cost 
	    			double b = Double.parseDouble(Payment.getText()); 		//gets payment received
	    			double c = b - a;                						//calculates the change
	    			txtpnChangedisplay.setText('$' + String.valueOf(c));	               
	    			change = Double.valueOf(c);
	                
	    			//catch for insufficient payment
	    			if(b < a || b <= 0 || b > 1000) {
	    				JOptionPane.showMessageDialog(panel, "Payment not Correct", "Error", JOptionPane.ERROR_MESSAGE); 		
	    			}                
	    		}
	    });

	    JButton btnComplete = new JButton("Complete Teansaction");
	    btnComplete.setBounds(350, 605, 180, 60);
	    add(btnComplete);
	    btnComplete.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			sendMessage(total); 
	    			total = 0.00;
	    			txtpnItemlist.setText("");
	    			TotalDisplay.setText("");
	    			Payment.setText("");
	    			txtpnChangedisplay.setText(""); 
	    			incrementTrans();        //incriments total transactions completed (maybe put after payment error?)
	    		}
	    });
	        
//Food item buttons        

	    //Burger button
	    JButton btnBurger = new JButton("Burger");
	    btnBurger.setBounds(6, 77, 202, 190);
	    add(btnBurger);      
	    btnBurger.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			txtpnItemlist.append("Burger..........$3.00\r\n");
	    			setTotal(3.00);                
	    			incrimentBurger();
	    		}
	    });

	    //fries button
	    JButton btnFries = new JButton("Fries");
	    btnFries.setBounds(6, 279, 202, 190);
	    add(btnFries);         
	    btnFries.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			txtpnItemlist.append("Fries..........$2.00\r\n");
	    			setTotal(2.00);
	    			incrimentFries();
	    		}
	    });
	        
	    //Drink button
	    JButton btnDrink = new JButton("Drink");
	    btnDrink.setBounds(6, 480, 202, 185);
	    add(btnDrink);	        
	    btnDrink.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			txtpnItemlist.append("Drink..........$2.00\r\n");
	    			setTotal(2.00);             
	    			incrimentDrink();
	    		}
	    });  
	}  
	
	private void setTotal(double cost){
		total = total + cost;    
        TotalDisplay.setText(formatter.format(total));
        TotalDisplay.repaint(); 
	}
	
	public void incrimentBurger(){
		burger++;
	}

	public void incrimentFries(){
		fries++;
	}

	public void incrimentDrink() {
		drink++;
	}
 
	public void incrementTrans(){
		transactionCount++;
	}
	    
	public static double getTotal() {
		return total;
	}
	   
	public static int getBurger() {
		return burger;
	}

	public static int getFries() {
		return fries;
	}
	    
	public static int getDrink() {
		return drink;
	}
	    
	 
	    
	public void startRunning() {
		try {
			connectToServer();
			setupStreams();
			whileConnected();
		}catch(EOFException eofException) {
			showMessage("\n Client terminated connection");
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}finally {
			closeCrap();
		}
	}
	    
	private void connectToServer() throws IOException{
	    	showMessage("Attempting connection...\n");
	    	connection = new Socket(InetAddress.getByName(serverIP), 1235);
	    	connection.setTcpNoDelay(true);
	    	showMessage("Connected to: " + connection.getInetAddress().getHostName());
	}
	  
	//set up streams to send and receive messages
	private void setupStreams() throws IOException{
		output = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()));
		output.flush();
		showMessage("\n Streams Are good to go!\n");
	}
		
	private void whileConnected() throws IOException{
		ableToType(true);
		do {
			try {
					
			}catch(Exception e) {
				showMessage("\n wat");
			}
		}while(total !=-1);		
	}
		
	private void closeCrap() {
		ableToType(false);
		try {
			output.flush();
			output.close();
			connection.close();
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
		
	private void sendMessage(double total) {
		try {
			output.writeDouble(total);
			output.flush();	
		}catch(IOException ioException) {
			txtpnItemlist.append("\n Something went wrong!");
		}
	}
		
	private void showMessage(final String m) {
		SwingUtilities.invokeLater(
			new Runnable() {
				public void run() {
					txtpnItemlist.append(m);
				}
			}
		);
	}
		
	private void ableToType(final boolean tof) {
		SwingUtilities.invokeLater(
			new Runnable() {
				public void run() {
					Payment.setEditable(tof);
				}
			}
		);
	}
		
		
	public static void main(String[] args) {
		FrontEnd charlie;
		charlie = new FrontEnd("127.0.0.1");
		charlie.setVisible(true);
		charlie.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		charlie.startRunning();
		}
};
