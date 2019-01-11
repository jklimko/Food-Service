# Food-Service

Application that consists of a client side (Front End) and a server side(Back End).

**Client Side** acts as an interface for an electronic cash register where the order is input and shows up on the display along with the cost of each item ordered. The app will also calculate change based on payment recieved and send the transaction data to the Server side.

**Server Side** consists of two classes. A server/interface class which creats an interface where the running total of profit is displayed. It also has a method which creates a new socket thread for each client that connects. The second class is a clientHandler class which is the thread that each client communicates with and also passes the transaction data back to the server class. 


#### @TO DO ####
* Improving client GUI
    - Add error messages and protection if something is wronge when "Complete transaction" buttton is pressed
*  DONE Pass more transacion data from clients to server(and update server gui accordingly)
* Save data in a type of SQL database
    - SQLite seems the way to go
* Create android app which has similar functionality as desktop app and connects tot he same backend
