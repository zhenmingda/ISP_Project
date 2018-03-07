package Encryption;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;

public class CipherServer implements Serializable {


    public static void main(String[] args) throws Exception {


        int port = 7999;
        ServerSocket server = new ServerSocket(port);
        Socket s = server.accept();
        FileInputStream fin = new FileInputStream("\\ISP_Project\\KeyFile.txt");
        ObjectInputStream ois = new ObjectInputStream(fin);
        Key key = (Key) ois.readObject();
        ois.close();

        Cipher dcipher = Cipher.getInstance("DES");

        dcipher.init(Cipher.DECRYPT_MODE, key);
        ObjectInputStream in = new ObjectInputStream(s.getInputStream());

        int len = in.readInt();
        byte[] result = new byte[len];
        in.readFully(result);

        result = dcipher.doFinal(result);

        System.out.println("The plaintext is"+" "+new String(result));


    }

}