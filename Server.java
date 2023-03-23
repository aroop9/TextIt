import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

class Server extends JFrame {

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    // Components for GUI
    private JLabel heading = new JLabel("Server Area"); 
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    // Constructor
    public Server(){
        try{
            server = new ServerSocket(7777);
            System.out.println("Server is ready for connection!");
            System.out.println("Waiting...");
            socket = server.accept();
            
            // Reading input stream from client socket
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Writing output stream to client socket for sending
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();

            startReading();
            startWriting();

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void handleEvents(){
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                //throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                //throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                //throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
                if(e.getKeyCode() == 10){
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me: " + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }
            
        });
    }

    private void createGUI(){
        this.setTitle("Server messeger(End)");
        this.setSize(600, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Components
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("chat1.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        heading.setBackground(Color.darkGray);
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        //messageInput.setBackground(Color.GREEN);

        // Frame's Layout
        this.setLayout(new BorderLayout());

        // Adding components to the frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        //messageArea.setCaretPosition(messageArea.getDocument().getLength()); // CHECK*****
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);
        
        this.setVisible(true);
    }

    public void startReading(){
        // Thread: Will read data from br and show us
        Runnable r1 = () -> {
            System.out.println("Reader started...");

            try{
                while(true){
                    String msg = br.readLine();
                    if(msg.equals("Exit") || msg.equals("exit")){
                        System.out.println("Client terminated the chat!");
                        JOptionPane.showMessageDialog(this, "Client terminated the chat!");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }

                    //System.out.println("Client: " + msg);
                    messageArea.append("Client: " + msg + "\n");
                }
            }
            catch(Exception e){
                //e.printStackTrace();
                System.out.println("Connection is clode!");
            }
        };
        new Thread(r1).start();
    }

    public void startWriting(){
        // Thread: Take data from server side and send it to the client
        Runnable r2 = () -> {
            System.out.println("Writer started...");
            
            try{
                while(!socket.isClosed()){
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    
                    out.println(content);
                    out.flush();

                    if(content.equals("exit") || content.equals("Exit")){
                        socket.close();
                        break;
                    }
                }
            }
            catch(Exception e){
                //e.printStackTrace();
                System.out.println("Connection is clode!");
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args){
        System.out.println("This is server... Going to start server!");
        new Server();
    }    
}











/*import java.net.*;
import java.io.*;

class Server {

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    public Server(){
        try{
            server = new ServerSocket(7777);
            System.out.println("Server is ready for connection!");
            System.out.println("Waiting...");
            socket = server.accept();
            
            // Reading input stream from client socket
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Writing output stream to client socket for sending
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void startReading(){
        // Thread: Will read data from br and show us
        Runnable r1 = () -> {
            System.out.println("Reader started...");

            try{
                while(true){
                    String msg = br.readLine();
                    if(msg.equals("Exit") || msg.equals("exit")){
                        System.out.println("Client terminated the chat!");
                        socket.close();
                        break;
                    }

                    System.out.println("Client: " + msg);
                }
            }
            catch(Exception e){
                //e.printStackTrace();
                System.out.println("Connection is clode!");
            }
        };
        new Thread(r1).start();
    }

    public void startWriting(){
        // Thread: Take data from server side and send it to the client
        Runnable r2 = () -> {
            System.out.println("Writer started...");
            
            try{
                while(!socket.isClosed()){
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    
                    out.println(content);
                    out.flush();

                    if(content.equals("exit") || content.equals("Exit")){
                        socket.close();
                        break;
                    }
                }
            }
            catch(Exception e){
                //e.printStackTrace();
                System.out.println("Connection is clode!");
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args){
        System.out.println("This is server... Going to start server!");
        new Server();
    }    
}
*/