import java.net.*;
import java.io.*;
 
class Client {
 
public static void main(String args[]) throws Exception {
  try {
  Socket c = new Socket("127.0.0.1", 1234);
  BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  
  String s = reader.readLine();
  int n = s.length();
  int character = reader.read();

  DataInputStream socketIn = new DataInputStream(c.getInputStream());
  DataOutputStream socketOut = new DataOutputStream(c.getOutputStream());
  
  socketOut.writeInt(n);
  socketOut.writeBytes(s);
  socketOut.writeByte(character);
  socketOut.flush();
  
  int nRez = socketIn.readInt();
  System.out.println(nRez);
  for(int i=0;i<nRez;i++) {
     System.out.print(socketIn.readInt() + " ");
  }
 
  System.out.println();
  reader.close();
  c.close();
  }  
  catch(IOException exception) {
    System.out.println("Eroare la conectare");
  }
}
 
}
