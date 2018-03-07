package Encryption;


import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;

public class CipherClient implements Serializable {


    public static void main(String[] args) throws Exception {
        Key key = KeyGenerator.getInstance("DES").generateKey();
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(""));
        out.writeObject(key);
        out.close();
        String message = "Professor";
        String host = "127.0.0.1";
        int port = 7999;
        Socket s = new Socket(host, port);
        Cipher ecipher = Cipher.getInstance("DES");
        ecipher.init(Cipher.ENCRYPT_MODE, key);
        ObjectOutputStream outputStream = new ObjectOutputStream(s.getOutputStream());
        byte[] bytes = ecipher.doFinal(message.getBytes());
        outputStream.writeInt(bytes.length);
        outputStream.write(bytes);
        outputStream.close();

    }


}