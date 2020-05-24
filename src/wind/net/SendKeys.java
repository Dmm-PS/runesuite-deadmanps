package wind.net;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import wind.net.login.RS2Decoder;
 
public class SendKeys
{
 public static String modulus;
 public static String exponent;
    private static Socket socket;
 
    public static void sendKeys()
    {

        try
        {
 
            int port = 43591;
            @SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server Started and listening to the port 43593");
 
            //Server is running always. This is done using this while(true) loop
            while(true)
            {
                //Reading the message from the client
                socket = serverSocket.accept();
                modulus = String.valueOf(RS2Decoder.clientModulus)+"\n";
                exponent = String.valueOf(RS2Decoder.clientExponent)+"\n";
 
                //Sending the response back to the client.
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);
                bw.write(modulus);
                System.out.println("Modulus sent to the client is : "+modulus);
                bw.write(exponent);
                System.out.println("Exponent sent to the client is : "+exponent);
                bw.flush();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
    }
}
